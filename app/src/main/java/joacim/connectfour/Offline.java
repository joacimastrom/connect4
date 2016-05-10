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
public class Offline extends Connect4{

    private ArrayList<Player> players;
    private String audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected void setupBoard(){
        super.setupBoard();
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
        audit = audit + "\nNew game\n";
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

    // Updates turn indicator and flag
    protected void updateTurn() {
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

}
