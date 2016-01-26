package joacim.connectfour;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;

import static java.lang.Math.min;

/**
 * Created by joacim on 24/01/16.
 */
public class Game extends Activity{

    private int[][] nbrBoard;
    private ImageView[][] imgBoard;
    private View boardView;
    private int rows;
    private int cols;
    public static final int EMPTY = 0;
    private static final int GREEN = 1;
    private static final int RED = 2;
    private int turnFlag;
    private TextView turnText;
    private String[] player;
    int consecutive;
    boolean winnerFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newGame = getIntent();
        player = newGame.getStringArrayExtra("players");
        turnFlag = RED;
        winnerFound = false;
        setContentView(R.layout.connect_four);
        turnText = (TextView) findViewById(R.id.turn);
        updateTurn(turnFlag);
        rows = 6;
        cols = 7;
        boardView = findViewById(R.id.game_board);
        setupBoard();
        nbrBoard = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                nbrBoard[i][k] = EMPTY;
            }
        }

        boardView.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP){
                    int col = getCol(event.getX());
                    if (col != -1) {
                        playTurn(col);
                    }

                    return true;
                }

            return false;
        }
    });
    }

    public void setupBoard(){

        imgBoard = new ImageView[rows][cols];

        for (int i = 0; i < rows; i++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) boardView).getChildAt(i);
            for (int k = 0; k < cols; k++){
                imgBoard[i][k] = (ImageView) row.getChildAt(k);
            }
        }


    }

    public void playTurn(int col) {
        if (!winnerFound) {

            for (int row = rows - 1; row >= 0; row--) {
                if (nbrBoard[row][col] == 0) {
                    nbrBoard[row][col] = turnFlag;
                    if (turnFlag == GREEN) {
                        imgBoard[row][col].setImageResource(R.drawable.token_green);
                    } else {
                        imgBoard[row][col].setImageResource(R.drawable.token_red);
                    }
                    winnerFound = checkWin(turnFlag, row, col);
                    break;

                }

            }
            updateTurn(turnFlag);
        }
    }


    // effektivisera senare och kolla runt spelpjäs
    private boolean checkWin(int turnFlag, int row, int col) {
        consecutive = 0;
        // check vertical
        for (int i = rows-1; i >= 0; i--) {
            int temp = nbrBoard[i][col];
            if (temp == turnFlag){
                if (consecutive == 3) {
                    return true;
                }
                consecutive++;
            } else if (temp == 0) {
                break;
            } else {
                consecutive = 0;
            }
        }
        consecutive = 0;
        // check horizontal
        for (int i = 0; i < cols; i++) {
            int temp = nbrBoard[row][i];
            if (temp == turnFlag) {
                if (consecutive == 3) {
                    return true;
                }
                consecutive++;
            } else {
                consecutive = 0;
            }
        }
        // check diagonal top down
        consecutive = 0;
        int maxDelta = min(row, col);
        if (maxDelta > 0) {
            for (int i = 1; i <= maxDelta; i++) {
                if (nbrBoard[row - i][col - i] == turnFlag) {
                    if (consecutive == 3) {
                        return true;
                    }
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
        }
        maxDelta = min(rows-row-1, cols-col-1);
        for (int i = 0; i <= maxDelta; i++) {
            if (nbrBoard[row + i][col + i] == turnFlag) {
                if (consecutive == 3) {
                    return true;
                }
                consecutive++;
            } else {
                consecutive = 0;
            }

        }

        // check diagonal bottom up
        consecutive = 0;
        maxDelta = min((rows-row-1), col);
        if (maxDelta > 0) {
            for (int delta = maxDelta; delta > 0; delta--) {
                if (nbrBoard[row+delta][col-delta] == turnFlag){
                    if (consecutive == 3) {
                        return true;
                    }
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
        }
        maxDelta = min(row, cols-col-1);
        if (maxDelta > 0) {
            for (int delta = 0; delta >= maxDelta; delta++) {
                if (nbrBoard[row-delta][col+delta] == turnFlag){
                    if (consecutive == 3) {
                        return true;
                    }
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
        }

        return false;
        }


    public int getCol(float x) {
        int col = (int) x / imgBoard[0][0].getWidth();
        if (nbrBoard[0][col] == 0){
            return col;
        }
        return -1;
    }


    private void updateTurn(int flag) {
        if (winnerFound) {
            turnText.setText(player[turnFlag] + " wins!");
            return;
        }
        if (flag == GREEN) {
            turnFlag = RED;
            turnText.setTextColor(Color.RED);
        } else {
            turnFlag = GREEN;
            turnText.setTextColor(Color.parseColor("#30C043"));
        }
        turnText.setText(player[turnFlag] + "'s turn");
    }

    public void reset(View view) {
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++){
                nbrBoard[i][k] = EMPTY;
                imgBoard[i][k].setImageResource(R.drawable.token_empty);
            }
        }
        winnerFound = false;
        updateTurn(turnFlag);
    }

}
