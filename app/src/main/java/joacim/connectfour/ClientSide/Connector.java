package joacim.connectfour.ClientSide;

import java.io.*;
import java.net.*;
import android.os.Handler;
import android.os.Message;

import joacim.connectfour.Game.Online;


public class Connector extends Thread{

        Handler turnHandler;
        int id;
        Socket s;

        Connector(Handler turnHandler) {
            this.turnHandler = turnHandler;
            try {
                s = new Socket("192.168.1.22", 8080);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            while (true) {
                ObjectOutputStream oos = null;
                ObjectInputStream ois = null;
                Message msg = Message.obtain();
                try {
                    ois = new ObjectInputStream(s.getInputStream());
                    int response = ois.readInt();
                    msg.what = response;
                    turnHandler.sendMessage(msg);
                    oos = new ObjectOutputStream(s.getOutputStream());
                    int move = Online.getData();
                    oos.writeInt(move);
                    oos.flush();


                } catch (Exception e) {
                    System.out.println("IO error " + e);

                }
            }
        }
}
