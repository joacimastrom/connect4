package joacim.connectfour.Online.ServerSide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import joacim.connectfour.Game.Online;

/**
 * Created by joacim on 18/05/16.
 */
public class HostGame extends Online {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler turnHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadTurn(msg.what);
            }

        };

        Monitor mon = new Monitor();
        new HostThread(turnHandler, mon).start();
        new LongOperation().execute(mon);

    }

    private class LongOperation extends AsyncTask<Monitor, Void, Void> {

        Monitor m;

       // public LongOperation(Monitor mon) {
       //     super();
       //     m = mon;
       // }

        @Override
        protected Void doInBackground(Monitor... params) {
            ServerSocket ss = null;
            try {
                Socket socket;
                ss = new ServerSocket(8080);
                socket = ss.accept();
                new ServerThread(params[0], socket).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
