package joacim.connectfour;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import android.graphics.Color;
import android.os.AsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;




public class Online extends Connect4 {

    private boolean yourTurn;
    private Integer response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yourTurn = false;
        setupBoard();

        try {
            response = new Connect().execute(6).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        loadTurn(response);

    }

    private void loadTurn(Integer event) {
        if (event == -1) {
            updateTurn();
        } else {
            playTurn(event);
        }
    }



    protected void setupSize() {
        setContentView(R.layout.connect_four);
        boardView = findViewById(R.id.game_board);
        rows = 6;
        cols = 7;
        turnFlag = RED;

    }


    protected void setupBoard(){
        super.setupBoard();
        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP ) {
                    int col = getCol(event.getX());
                    if (col != -1 && yourTurn) {
                        playTurn(col);
                        try {
                            int nextMove = new Connect().execute(col).get();
                            loadTurn(nextMove);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }


    // Find first empty slot in column and play
    public void playTurn(int col) {
        if (!winnerFound && !full) {
            int row = findRow(col);
            nbrBoard[row][col] = turnFlag;
            switch (turnFlag) {
                case GREEN:
                    imgBoard[row][col].setImageResource(R.drawable.green);
                    break;
                case RED:
                    imgBoard[row][col].setImageResource(R.drawable.red);
                    break;
                }

        if (checkWin(row, col)) {
                winnerFound = true;
        } else if (checkFull()) {
                full = true;
            }
            updateTurn();
        }
    }

    // Updates turn indicator and flag
    protected void updateTurn() {

        if (winnerFound) {
            turnText.setText(turnFlag + " wins!");
            return;
        }
        if (full) {
            turnText.setText("Nobody wins");
            reset.setVisibility(View.VISIBLE);
            turnText.setTextColor(Color.WHITE);
            return;
        }
        switch (turnFlag) {
            case GREEN:
                turnFlag = RED;
                turnText.setTextColor(Color.RED);
                break;
            case RED:
                turnFlag = GREEN;
                turnText.setTextColor(Color.parseColor("#30C043"));
                break;
        }
        yourTurn = !yourTurn;
        turnText.setText(turnFlag + "'s turn");
    }

    private class Connect extends AsyncTask<Integer, Void, Integer> {
        Socket s;

        @Override
        protected Integer doInBackground(Integer... param) {
            try {
                if(s == null || !s.isConnected()) {
                    s = new Socket("192.168.1.22", 8080);
                }
                passTurn(param[0], s);
                return readTurn(s);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return -2;
        }

        protected void passTurn(int turn, Socket s) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(
                        s.getOutputStream());
                oos.writeInt(turn);
                oos.flush();
            } catch (Exception e) {
                System.out.println("IO error " + e);
            }
        }

        protected int readTurn(Socket s) throws IOException, ClassNotFoundException {
            int msg;
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            msg =  ois.readInt();
            return msg;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

