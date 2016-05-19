package joacim.connectfour.Online.ServerSide;
import java.io.*;
import java.net.*;

import joacim.connectfour.Online.GameThread;


class ServerThread extends GameThread {
    MulticastSocket ms;
    Monitor m;
    InetAddress ia;
    DatagramSocket socket;
    private String clientAddress;

    public ServerThread(Monitor m) {
        this.m = m;
        id = 1;
    }


    public void run() {
        try {
        ms = new MulticastSocket(8080);
        ia = InetAddress.getByName("224.0.50.50");
        ms.joinGroup(ia);

        byte[] start = new byte[1024];
        DatagramPacket init= new DatagramPacket(start, start.length);
        ms.receive(init);
        clientAddress = init.getAddress().getHostAddress();
        start = "hello".getBytes();
        ms.close();
        socket = new DatagramSocket(8080);
        init = new DatagramPacket(start, start.length, InetAddress.getByName(clientAddress), 8080);
        socket.send(init);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!this.interrupted()) {
            try {
                int checkFirst = m.waitTurn(this);
                byte[] data = {(byte) checkFirst};
                DatagramPacket dp = new DatagramPacket(data, data.length,  InetAddress.getByName(clientAddress), 8080);
                socket.send(dp);

                data = new byte[1];
                dp = new DatagramPacket(data, data.length);
                socket.receive(dp);

                m.addTurn(dp.getData()[0]);
            } catch (Exception e) {
                System.out.println("IO error " + e);
            }
        }

        return;
    }

    public void killSockets() {
        if (ms != null) {
            ms.close();
        }
        if (socket != null){
            socket.close();
        }
    }


}