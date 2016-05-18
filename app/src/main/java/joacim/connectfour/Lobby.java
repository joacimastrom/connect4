package joacim.connectfour;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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
    }


    public void joinGame(View view) {
    }
}
