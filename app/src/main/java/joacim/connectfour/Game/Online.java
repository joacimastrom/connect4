package joacim.connectfour.Game;


import android.graphics.Color;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;

import joacim.connectfour.R;


public abstract class Online extends Connect4 {

    protected boolean yourTurn;
    protected Integer response;
    public static int nextMove = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        yourTurn = false;
        setupBoard();

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
    protected void loadTurn(Integer event) {
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
            if (turnFlag == RED) {
                turnText.setText("You lose!");
            } else {
                turnText.setText("You win!");
            }
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
        if (turnFlag == RED) {
            turnText.setText("Waiting");
        } else {
            turnText.setText("Your turn");
        }
    }

}