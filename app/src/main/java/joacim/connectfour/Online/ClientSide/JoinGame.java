package joacim.connectfour.Online.ClientSide;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import joacim.connectfour.Game.Online;

/**
 * Created by joacim on 18/05/16.
 */
public class JoinGame extends Online {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler turnHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadTurn(msg.what);
            }

        };

        new Connector(turnHandler).start();


    }


}