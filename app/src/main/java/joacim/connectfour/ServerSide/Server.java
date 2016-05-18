package joacim.connectfour.ServerSide;
import java.io.IOException;
import java.net.*;
public class Server {
    public Server(int port){
        Monitor mon = new Monitor();
        new ServerThread(mon, 0).start();
        try {
            ServerSocket ss = new ServerSocket(port);
            Socket accSock = ss.accept();
            new ServerThread(accSock, mon, 1).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}