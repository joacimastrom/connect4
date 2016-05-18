package joacim.connectfour.Online.ClientSide;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

import android.os.Handler;
import android.os.Message;

import joacim.connectfour.Game.Online;


public class Connector extends Thread{

        Handler turnHandler;
        Socket s;

        public Connector(Handler turnHandler) {
            this.turnHandler = turnHandler;
            InetAddress localhost = null;
            try {
                localhost = InetAddress.getLocalHost();

            // this code assumes IPv4 is used
            byte[] ip = localhost.getAddress();
            for (int i = 1; i <= 254; i++)
            {
                ip[3] = (byte)i;
                InetAddress address = InetAddress.getByAddress(ip);
                s = new Socket(address, 8080);

            }
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
