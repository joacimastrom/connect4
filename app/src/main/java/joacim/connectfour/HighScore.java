package joacim.connectfour;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;
import android.view.ViewGroup;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;

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


        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("players", "defaultStringIfNothingFound");
        if (json != "defaultStringIfNothingFound") {


        players = gson.fromJson(json, new TypeToken<ArrayList<Player>>() {
        }.getType());

        ListView highscore = (ListView) findViewById(R.id.scoreList);
        if (players.size() < 5) {
            adapter = new HighScoreAdapter(this, new ArrayList<>(players));
        } else {
            adapter = new HighScoreAdapter(this, new ArrayList<>(players.subList(0, 5)));
        }
        highscore.setAdapter(adapter);

        }
        else {
            title = (TextView) findViewById(R.id.leaderboard);
            title.setText("No registred results");

        }



    }
}
