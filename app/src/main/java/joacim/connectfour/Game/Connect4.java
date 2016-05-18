package joacim.connectfour.Game;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.gson.Gson;

import java.util.Collections;

import joacim.connectfour.R;

import static java.lang.Math.min;

/**
 * Created by joacim on 24/01/16.
 */
public abstract class Connect4 extends Activity {

    protected int[][] nbrBoard;
    protected ImageView[][] imgBoard;
    protected TextView turnText;
    protected View boardView;
    protected Button reset;
    protected int rows;
    protected int cols;
    protected static final int EMPTY = 0;
    protected static final int GREEN = 1;
    protected static final int RED = 2;
    protected static final int YELLOW = 3;
    protected int turnFlag;
    protected String[] currPlayers;
    protected boolean winnerFound;
    protected boolean full;
    protected boolean tri;

    private ArrayList<Player> players;
    private String audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
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

    protected void setupSize() {
    }

    // Setup logical & graphical board and resetbutton
    protected void setupBoard() {
        setupSize();

        winnerFound = false;
        full = false;
        turnText = (TextView) findViewById(R.id.turn);
        reset = (Button) findViewById(R.id.reset);
        reset.setVisibility(View.GONE);
        // updateTurn();
        imgBoard = new ImageView[rows][cols];
        nbrBoard = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) boardView).getChildAt(i);
            for (int k = 0; k < cols; k++) {
                imgBoard[i][k] = (ImageView) row.getChildAt(k);
                nbrBoard[i][k] = EMPTY;
            }
        }

    }

    // Check if board is full
    protected boolean checkFull() {
        for (int i = 0; i < cols; i++) {
            if (nbrBoard[0][i] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    // Save audit log
    protected void saveAudit() {
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
    protected void savePlayers() {
        Gson gson = new Gson();
        String json = gson.toJson(players);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("players", json).commit();
    }


    // Find first empty slot in column and play
    protected void playTurn(int col) {
    }

    protected int findRow(int col) {
        for (int row = rows - 1; row >= 0; row--) {
            if (nbrBoard[row][col] == 0) {
                return row;

            }

        }
        return 0;
    }

    // Increment win for winner
    protected void addWin() {
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
    protected boolean checkWin(int row, int col) {
        if (checkVertical(row, col) || checkHorizontal(row, col) || checkTopDown(row, col) || checkBottomUp(row, col)) {
            return true;
        }
        return false;
    }

    // Fetch which column was clicked
    protected int getCol(float x) {
        int col = (int) x / imgBoard[0][0].getWidth();
        if (nbrBoard[0][col] == 0) {
            return col;
        }
        return -1;
    }

    // Updates turn indicator and flag
    protected void updateTurn() {
    }

    // Checks vertically on played row
    protected boolean checkVertical(int row, int col) {
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

    // Check horizontally on played row
    protected boolean checkHorizontal(int row, int col) {
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



    // Check diagonal top down
    protected boolean checkTopDown(int row, int col) {
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
    // Check diagonal bottom up
    protected boolean checkBottomUp(int row, int col) {
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
}
