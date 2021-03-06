package joacim.connectfour.Game;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import joacim.connectfour.R;

/**
 * Created by joacim on 26/01/16.
 */
public class HighScore extends Activity{

    ArrayList<Player> players;
    HighScoreAdapter adapter;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);
        getActionBar().hide();

        // Load player database
        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("players", "default");
        if (json != "default") {
            players = gson.fromJson(json, new TypeToken<ArrayList<Player>>() {
            }.getType());
        } else {             players = new ArrayList<>(); }



            // Lists all players if less than 10, otherwise 10 best, or none if empty
        ListView highscore = (ListView) findViewById(R.id.scoreList);
        if (players.size() < 10) {
            adapter = new HighScoreAdapter(this, new ArrayList<>(players));
        } else {
            adapter = new HighScoreAdapter(this, new ArrayList<>(players.subList(0, 10)));
        }
        highscore.setAdapter(adapter);
    }


    // Clears win counts
    public void clearScore(View view) {
        for (Player p : players) {
            p.resetWins();
        }
        savePlayers();
        adapter.notifyDataSetChanged();
    }


    // Clears player history
    public void clearUsers(View view) {
        players.removeAll(players);
        savePlayers();
        finish();
        startActivity(getIntent());
    }

    // Saves player database
    private void savePlayers(){
        Gson gson = new Gson();
        String json = gson.toJson(players);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("players", json).commit();
    }
}
