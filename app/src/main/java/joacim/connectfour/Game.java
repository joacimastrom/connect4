package joacim.connectfour;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;

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
    private String[] currPlayers;
    int consecutive;
    private boolean winnerFound;
    private boolean full;
    ArrayList<Player> players;
    private Button reset;
    private String audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        Intent newGame = getIntent();
        currPlayers = newGame.getStringArrayExtra("currPlayers");
        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("players", "defaultStringIfNothingFound");
        audit = PreferenceManager.getDefaultSharedPreferences(this).getString("audit", "");
        players = gson.fromJson(json, new TypeToken<ArrayList<Player>>(){}.getType());

        setupBoard();
    }


    private boolean checkFull() {
        for (int i = 0; i<cols; i++){
            if (nbrBoard[0][i] == EMPTY) {
                return false;
            }
        }

        return true;
    }


    private void saveAudit() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("audit", audit).commit();

    }

    private void savePlayers(){
        Gson gson = new Gson();
        String json = gson.toJson(players);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("players", json).commit();
    }

    public void setupBoard(){
        turnFlag = RED;
        winnerFound = false;
        full = false;
        setContentView(R.layout.connect_four);
        turnText = (TextView) findViewById(R.id.turn);
        reset = (Button) findViewById(R.id.reset);
        reset.setVisibility(View.GONE);
        updateTurn(turnFlag);
        rows = 6;
        cols = 7;
        boardView = findViewById(R.id.game_board);
        imgBoard = new ImageView[rows][cols];
        nbrBoard = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) boardView).getChildAt(i);
            for (int k = 0; k < cols; k++){
                imgBoard[i][k] = (ImageView) row.getChildAt(k);
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

    audit = audit + "New game\n";

    }

    public void playTurn(int col) {
        if (!winnerFound && !full) {

            for (int row = rows - 1; row >= 0; row--) {
                if (nbrBoard[row][col] == 0) {
                    nbrBoard[row][col] = turnFlag;
                    if (turnFlag == GREEN) {
                        imgBoard[row][col].setImageResource(R.drawable.green);
                    } else {
                        imgBoard[row][col].setImageResource(R.drawable.red);
                    }
                    audit = audit + currPlayers[turnFlag] + " played row: " + row + ", col: " + col + "\n";
                    saveAudit();
                    if (checkWin(turnFlag, row, col)) {
                        winnerFound = true;
                    } else if (checkFull()) {
                        full = true;
                    }
                break;

                }

            }
            updateTurn(turnFlag);
        }
    }

    private void addWin(){
        for (Player p : players) {
            if (p.getName().equals(currPlayers[turnFlag])){
                p.regWin();
                break;
            }
        }
        Collections.sort(players);
        savePlayers();

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
            if (nbrBoard[row][i] == turnFlag) {
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
            for (int i = maxDelta; i > 0; i--) {
                if (nbrBoard[row - i][col - i] == turnFlag) {
                    if (consecutive == 3) {
                        return true;
                    }
                    consecutive++;
                } else {
                    consecutive = 0;
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
            for (int delta = 0; delta <= maxDelta; delta++) {
                if (nbrBoard[row-delta][col+delta] == turnFlag){
                    if (consecutive == 3) {
                        return true;
                    }
                    consecutive++;
                } else {
                    consecutive = 0;
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
            reset.setVisibility(View.VISIBLE);
            addWin();
            audit = audit + currPlayers[turnFlag] + " wins!\n\n";
            saveAudit();
            turnText.setText(currPlayers[turnFlag] + " wins!");
            return;
        }
        if (full) {turnText.setText("Nobody wins");
            reset.setVisibility(View.VISIBLE);
            turnText.setTextColor(Color.WHITE);
            audit = audit + "Board is full.\n\n";
            saveAudit();
            return;
        }
        if (flag == GREEN) {
            turnFlag = RED;
            turnText.setTextColor(Color.RED);
        } else {
            turnFlag = GREEN;
            turnText.setTextColor(Color.parseColor("#30C043"));
        }
        turnText.setText(currPlayers[turnFlag] + "'s turn");
    }

    public void reset(View view) {
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++){
                nbrBoard[i][k] = EMPTY;
                imgBoard[i][k].setImageResource(R.drawable.empty);
            }
        }
        winnerFound = false;
        full = false;
        updateTurn(turnFlag);
        view.setVisibility(View.GONE);
        audit = audit + "New game\n";
        saveAudit();

    }

}
