package joacim.connectfour.Game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import joacim.connectfour.Online.ClientSide.JoinGame;
import joacim.connectfour.R;
import joacim.connectfour.Online.ServerSide.HostGame;

/**
 * Created by joacim on 18/05/16.
 */
public class Lobby extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);
        getActionBar().hide();
    }


    public void hostGame(View view) {
        Intent host = new Intent(Lobby.this, HostGame.class);
        startActivity(host);

    }


    public void joinGame(View view) {
        Intent join = new Intent(Lobby.this, JoinGame.class);
        startActivity(join);
    }
}
