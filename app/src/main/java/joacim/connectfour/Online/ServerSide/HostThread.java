package joacim.connectfour.Online.ServerSide;

import android.os.Handler;
import android.os.Message;

import joacim.connectfour.Online.Online;
import joacim.connectfour.Online.GameThread;

/**
 * Created by joacim on 18/05/16.
 */
public class HostThread extends GameThread {


    Handler turnHandler;
    Monitor m;
    int id;

    public HostThread (Handler turnHandler, Monitor m) {
        this.turnHandler = turnHandler;
        this.m = m;
        id = 0;
    }

    public void run() {
        while (true) {
            Message msg = Message.obtain();
            try {
                int move = m.waitTurn(this);
                msg.what = move;
                turnHandler.sendMessage(msg);
                move = Online.getData();
                m.addTurn(move);



            } catch (Exception e) {
                System.out.println("IO error " + e);

            }
        }
    }
}
