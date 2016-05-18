package joacim.connectfour.Game;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;

import joacim.connectfour.ClientSide.Connector;
import joacim.connectfour.R;


public abstract class Online extends Connect4 {

    private boolean yourTurn;
    private Integer response;
    public static int nextMove = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        yourTurn = false;
        setupBoard();
        final Handler turnHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadTurn(msg.what);
            }

        };

        new Connector(turnHandler).start();

    }


    public synchronized static int getData() {
        while(nextMove == -1){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int tempMove = nextMove;
        nextMove = -1;
        return tempMove;
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
                        nextMove = col;

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