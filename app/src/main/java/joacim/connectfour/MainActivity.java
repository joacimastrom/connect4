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
import java.util.ArrayList;


import java.util.HashMap;


public class MainActivity extends Activity {


    private ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    private void setup() {

        TextView title = (TextView) findViewById(R.id.textView);
        Button play = (Button) findViewById(R.id.multiPlay);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        players = new ArrayList<Player>();
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
        alert.setMessage("Who's playing?");

        // Set an EditText view to get user input
        final EditText player1 = new EditText(this);
        player1.setHint("Player 1");
        final EditText player2 = new EditText(this);
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
                newPlayers = new String[]{"",player1.getText().toString(), player2.getText().toString()};
                for (Player p : players) {
                    if (p.getName() == newPlayers[1]) {
                        p1 = p;
                    } else if (p.getName() == newPlayers[2]) {
                        p2 = p;
                    }

                }
                if (p1 == null) {
                    players.add(new Player(newPlayers[1]));
                }
                if (p2 == null) {
                    players.add(new Player(newPlayers[2]));
                }


                Intent playGame = new Intent(MainActivity.this, Game.class);
                playGame.putExtra("players", newPlayers);
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
}
