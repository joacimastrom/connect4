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
public class Connect4 extends Activity {

    private int[][] nbrBoard;
    private ImageView[][] imgBoard;
    private TextView turnText;
    private View boardView;
    private Button reset;
    private int rows;
    private int cols;
    private static final int EMPTY = 0;
    private static final int GREEN = 1;
    private static final int RED = 2;
    private static final int YELLOW = 3;
    private int turnFlag;
    private String[] currPlayers;
    private boolean winnerFound;
    private boolean full;
    private boolean tri;

    private ArrayList<Player> players;
    private String audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        audit = PreferenceManager.getDefaultSharedPreferences(this).getString("audit", "");
        setupPlayers();
        setupBoard();
    }

    // Load player history
    private void setupPlayers() {
        Intent newGame = getIntent();
        currPlayers = newGame.getStringArrayExtra("currPlayers");
        Gson gson = new Gson();
        // Retrieve stored playerlist(JSON string) and convert to ArrayList<Player>
        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("players", "default");
        if (!json.equals("default")){
            players = gson.fromJson(json, new TypeToken<ArrayList<Player>>() {
            }.getType());
        } else {
            players = new ArrayList<>();
        }


        // Check number of players
        if (currPlayers.length == 4) { tri = true; turnFlag = YELLOW; }
        else { tri = false; turnFlag = RED;}

        for (int i = 1; i <= currPlayers.length - 1; i++) {
            if (!checkPlayer(currPlayers[i])) {
                players.add(new Player(currPlayers[i]));
                savePlayers();
            }
        }
    }

    // Check if players are already in database
    private boolean checkPlayer(String name) {
        for (Player p : players) {
            if (name.equals(p.getName())) {
                return true;
            }
        }
        return false;
    }

    // Setup logical & graphical board and resetbutton
    public void setupBoard() {
        if (currPlayers[0].equals("Small")){
            setContentView(R.layout.connect_four);
            boardView = findViewById(R.id.game_board);
            rows = 6;
            cols = 7;
        } else {
            setContentView(R.layout.connect_four_large);
            boardView = findViewById(R.id.game_board_large);
            rows = 7;
            cols = 10;

        }

        winnerFound = false;
        full = false;
        turnText = (TextView) findViewById(R.id.turn);
        reset = (Button) findViewById(R.id.reset);
        reset.setVisibility(View.GONE);
        updateTurn();
        imgBoard = new ImageView[rows][cols];
        nbrBoard = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) boardView).getChildAt(i);
            for (int k = 0; k < cols; k++) {
                imgBoard[i][k] = (ImageView) row.getChildAt(k);
                nbrBoard[i][k] = EMPTY;
            }
        }
        boardView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int col = getCol(event.getX());
                    if (col != -1) {
                        playTurn(col);
                    }
                    return true;
                }
                return false;
            }
        });
        audit = audit + "\nNew game\n";

    }

    // Check if board is full
    private boolean checkFull() {
        for (int i = 0; i < cols; i++) {
            if (nbrBoard[0][i] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    // Save audit log
    private void saveAudit() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("audit", audit).commit();

    }

    // Audit if cancelled game
    @Override
    public void finish() {
        audit = audit + "Game cancelled\n";
        saveAudit();
        super.finish();
    }

    // Save player state
    private void savePlayers() {
        Gson gson = new Gson();
        String json = gson.toJson(players);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("players", json).commit();
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
                case YELLOW:
                    imgBoard[row][col].setImageResource(R.drawable.yellow);
                    break;
            }
            audit = audit + currPlayers[turnFlag] + " played row: " + row + ", col: " + col + "\n";
            saveAudit();
            if (checkWin(row, col)) {
                winnerFound = true;
            } else if (checkFull()) {
                full = true;
            }
            updateTurn();
        }

    }

    private int findRow(int col) {
        for (int row = rows - 1; row >= 0; row--) {
            if (nbrBoard[row][col] == 0) {
                return row;

            }

        }
        return 0;
    }

    // Increment win for winner
    private void addWin() {
        for (Player p : players) {
            if (p.getName().equals(currPlayers[turnFlag])) {
                p.regWin();
                break;
            }
        }
        Collections.sort(players);
        savePlayers();

    }

    // Checks all possibilities connected to most recent play
    private boolean checkWin(int row, int col) {
        if (checkVertical(row, col) || checkHorizontal(row, col) || checkTopDown(row, col) || checkBottomUp(row, col)) {
            return true;
        }
        return false;
    }

    // Fetch which column was clicked
    public int getCol(float x) {
        int col = (int) x / imgBoard[0][0].getWidth();
        if (nbrBoard[0][col] == 0) {
            return col;
        }
        return -1;
    }

    // Updates turn indicator and flag
    private void updateTurn() {
        if (winnerFound) {
            reset.setVisibility(View.VISIBLE);
            addWin();
            audit = audit + currPlayers[turnFlag] + " wins!\n";
            saveAudit();
            turnText.setText(currPlayers[turnFlag] + " wins!");
            return;
        }
        if (full) {
            turnText.setText("Nobody wins");
            reset.setVisibility(View.VISIBLE);
            turnText.setTextColor(Color.WHITE);
            audit = audit + "Board is full.\n";
            saveAudit();
            return;
        }

        switch (turnFlag) {
            case GREEN:
                turnFlag = RED;
                turnText.setTextColor(Color.RED);
                break;
            case RED:
                if (tri) {
                    turnFlag = YELLOW;
                    turnText.setTextColor(Color.YELLOW);
                } else {
                    turnFlag = GREEN;
                    turnText.setTextColor(Color.parseColor("#30C043"));
                }
                break;
            case YELLOW:
                turnFlag = GREEN;
                turnText.setTextColor(Color.parseColor("#30C043"));
                break;
        }
        turnText.setText(currPlayers[turnFlag] + "'s turn");
    }


    // Resets the board
    public void reset(View view) {
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                nbrBoard[i][k] = EMPTY;
                imgBoard[i][k].setImageResource(R.drawable.empty);
            }
        }
        winnerFound = false;
        full = false;
        updateTurn();
        view.setVisibility(View.GONE);
        audit = audit + "\nNew game\n";
        saveAudit();

    }


    // Check diagonal bottom up
    private boolean checkBottomUp(int row, int col) {
        int consecutive = 0;

        int maxDelta = min((rows - row - 1), col);
        if (maxDelta > 0) {
            for (int delta = maxDelta; delta > 0; delta--) {
                if (nbrBoard[row + delta][col - delta] == turnFlag) {
                    if (consecutive == 3) {
                        return true;
                    }
                    consecutive++;
                } else {
                    consecutive = 0;
                }
            }
        }
        maxDelta = min(row, cols - col - 1);
        for (int delta = 0; delta <= maxDelta; delta++) {
            if (nbrBoard[row - delta][col + delta] == turnFlag) {
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

    // Check diagonal top down
    private boolean checkTopDown(int row, int col) {
        int consecutive = 0;
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
        maxDelta = min(rows - row - 1, cols - col - 1);
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
        return false;
    }

    // Check horizontally on played row
    private boolean checkHorizontal(int row, int col) {
        int consecutive = 0;
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
        return false;
    }

    // Checks vertically on played row
    private boolean checkVertical(int row, int col) {
        int consecutive = 0;

        for (int i = rows - 1; i >= 0; i--) {
            int temp = nbrBoard[i][col];
            if (temp == turnFlag) {
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
        return false;
    }
}
