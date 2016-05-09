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
import android.text.InputFilter;
import android.widget.RadioButton;
import android.widget.Toast;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
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

    // Two human players
    public void twoPlayers(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add players");
        alert.setMessage("Who's playing? (Max 10 characters)");

        // Setup player input window
        final EditText player1 = new EditText(this);
        // Max character input
        int maxLength = 10;
        player1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        player1.setHint("Green");
        final EditText player2 = new EditText(this);
        player2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        player2.setHint("Red");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(player1);
        layout.addView(player2);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String[] newPlayers;
                newPlayers = new String[]{"",player1.getText().toString().trim(), player2.getText().toString().trim()};
                for (int i = 1; i<3; i++) {
                    if (newPlayers[i].equals("")) {
                        newPlayers[i] = "Player " + i;
                    }
                }
                setSize(newPlayers);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        });
        alert.show();
    }

    // Three human players
    public void threePlayers(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add players");
        alert.setMessage("Who's playing? (Max 10 characters)");

        // Setup player input window
        final EditText player1 = new EditText(this);
        // Max character input
        int maxLength = 10;
        player1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        player1.setHint("Green");
        final EditText player2 = new EditText(this);
        player2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        player2.setHint("Red");
        final EditText player3 = new EditText(this);
        player3.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        player3.setHint("Yellow");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(player1);
        layout.addView(player2);
        layout.addView(player3);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String[] newPlayers;
                newPlayers = new String[]{"",player1.getText().toString().trim(), player2.getText().toString().trim(), player3.getText().toString().trim()};
                for (int i = 1; i<4; i++) {
                    if (newPlayers[i].equals("")) {
                        newPlayers[i] = "Player " + i;
                    }
                }
                setSize(newPlayers);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        });

        alert.show();

    }

    private void setSize(final String[] newPlayers) {
        final String[] sizes = new String[]{"Small", "Large"};
        AlertDialog.Builder sizeQ = new AlertDialog.Builder(this)
                .setTitle("Choose board size")
                .setSingleChoiceItems(sizes, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newPlayers[0] = sizes[which];
                        Intent playGame = new Intent(MainActivity.this, Connect4.class);
                        playGame.putExtra("currPlayers", newPlayers);
                        startActivity(playGame);
                        dialog.dismiss();

                    }

                });

        sizeQ.show();





    }
    // Highscore menu button action
    public void showHighscore(View view) {
        Intent showScore = new Intent(MainActivity.this, HighScore.class);
        startActivity(showScore);
    }

    // Audit menu button action
    public void showAudit(View view) {
        Intent audit = new Intent(MainActivity.this, Audit.class);
        startActivity(audit);
    }

    public void onlinePlay(View view) {
        Intent online = new Intent(MainActivity.this, Online.class);
        startActivity(online);

    }
}