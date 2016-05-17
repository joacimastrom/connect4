package joacim.connectfour;

import java.io.*;
import java.net.*;
import android.os.Handler;
import android.os.Message;


public class Connector extends Thread{

    Socket socket;
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
                    ois = new ObjectInputStream(socket.getInputStream());
                    int response = ois.readInt();
                    msg.what = response;
                    turnHandler.sendMessage(msg);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    Online.getData();
                    // oos.writeInt(checkFirst);
                    oos.flush();

                    if(response == -3){
                        response = ois.readInt();
                    }
                    System.out.println(response);
                    oos.writeInt(response + 1);
                    //m.addTurn(response);

                } catch (Exception e) {
                    System.out.println("IO error " + e);
                } finally {
                    try {
                        oos.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
}
