package joacim.connectfour.Online.ServerSide;
import java.io.*;
import java.net.*;

import joacim.connectfour.Online.GameThread;


class ServerThread extends GameThread {
    Socket socket;
    Monitor m;

    ServerThread(Socket socket, Monitor m) {
        this.socket = socket;
        this.m = m;
        id = 1;
    }
    public ServerThread(Monitor m, int id) {
        this.m = m;
        this.id = id;
    }
    public void run() {
        while (true) {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                int checkFirst = m.waitTurn(this);
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeInt(checkFirst);
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());
                int response = ois.readInt();
                if(response == -3){
                    response = ois.readInt();
                }
                System.out.println(response);
                oos.writeInt(response);
                //	m.changeTurn();
                m.addTurn(response);
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