package joacim.connectfour.ServerSide;
import java.io.*;
import java.net.*;


class ServerThread extends Thread {
    Socket socket;
    Monitor m;
    int id;
    ServerThread(Socket socket, Monitor m, int id) {
        this.socket = socket;
        this.m = m;
        this.id = id;
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