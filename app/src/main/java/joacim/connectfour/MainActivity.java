package joacim.connectfour;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.text.InputFilter;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.preference.PreferenceManager;


public class MainActivity extends Activity {

    private ArrayList<Player> players;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("players", "defaultStringIfNothingFound");

        if (!json.equals("defaultStringIfNothingFound")) {
            players = gson.fromJson(json, new TypeToken<ArrayList<Player>>() {
            }.getType());
        } else {
            players = new ArrayList<Player>();
        }
    }

    private void savePlayers(){
        Gson gson = new Gson();
        String json = gson.toJson(players);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("players", json).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add players");
        alert.setMessage("Who's playing? (Max 10 characters)");

        // Setup input window
        final EditText player1 = new EditText(this);
        int maxLength = 10;
        player1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        player1.setHint("Player 1");
        final EditText player2 = new EditText(this);
        player2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        player2.setHint("Player 2");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(player1);
        layout.addView(player2);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String[] newPlayers;
                Player p1 = null;
                Player p2 = null;
                newPlayers = new String[]{"",player1.getText().toString().trim(), player2.getText().toString().trim()};
                for (int i = 1; i<3; i++) {
                    if (newPlayers[i].equals("")) {
                        newPlayers[i] = "Player " + i;
                    }
                }

                for (Player p : players) {
                    if (p.getName().equals(newPlayers[1])) {
                        p1 = p;
                    } else if (p.getName().equals(newPlayers[2])) {
                        p2 = p;
                    }

                }
                if (p1 == null) {
                    players.add(new Player(newPlayers[1]));
                }
                if (p2 == null) {
                    players.add(new Player(newPlayers[2]));
                }
                savePlayers();
                Intent playGame = new Intent(MainActivity.this, Game.class);
                playGame.putExtra("currPlayers", newPlayers);
                startActivity(playGame);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }

    public void showHighscore(View view) {
        Intent showScore = new Intent(MainActivity.this, HighScore.class);
        startActivity(showScore);
    }

    public void showAudit(View view) {
        Intent audit = new Intent(MainActivity.this, Audit.class);
        startActivity(audit);
    }
}
