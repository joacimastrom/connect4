package joacim.connectfour.Online.ServerSide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import joacim.connectfour.Online.Online;

/**
 * Created by joacim on 18/05/16.
 */
public class HostGame extends Online {

    HostThread host;
    ServerThread server;


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
        host = new HostThread(turnHandler, mon);
        host.start();
        server = new ServerThread(mon);

        new LongOperation().execute(server);

    }


    @Override
    public void finish() {
        host.interrupt();
        server.interrupt();
        server.killSockets();
        super.finish();
    }

    private class LongOperation extends AsyncTask<ServerThread, Void, Void> {

        @Override
        protected Void doInBackground(ServerThread... params) {
                params[0].start();
            return null;
        }
    }

}
