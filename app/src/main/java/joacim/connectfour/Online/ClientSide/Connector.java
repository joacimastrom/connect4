package joacim.connectfour.Online.ClientSide;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

import android.os.Handler;
import android.os.Message;

import joacim.connectfour.Online.Online;


public class Connector extends Thread{

    Handler turnHandler;
    InetAddress ia;
    DatagramSocket ds;
    String host;
    InetAddress destHost;
    String broadCastHost = "224.0.50.50";


    public Connector(Handler turnHandler) {
        this.turnHandler = turnHandler;

        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements())
            {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements())
                {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i instanceof Inet4Address && !i.isLoopbackAddress())
                    {
                        host = i.getHostAddress();
                    }
                }
            }
            ia = InetAddress.getByName(host);
            ds = new DatagramSocket(8080);
        } catch (IOException e) {

        }


    }


    public void run() {
        try {
            byte[] buffer = (ia.toString()).getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(broadCastHost), 8080);
            ds.send(packet);
            byte[] recvBuf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(recvBuf, recvBuf.length);
            ds.receive(recv);
            destHost = recv.getAddress();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!Thread.interrupted()) {
            Message msg = Message.obtain();
            try {
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                ds.receive(dp);
                msg.what = (dp.getData()[0]);
                turnHandler.sendMessage(msg);
                int move = Online.getData();
                byte[] send = {(byte) move};
                DatagramPacket sendPackage = new DatagramPacket(send,send.length, destHost, 8080);
                ds.send(sendPackage);
            } catch (Exception e) {
                System.out.println("IO error " + e);

            }
        }


        return;
    }

    public void killSockets() {
        if (ds.isConnected() || ds != null){
            ds.close();
        }
    }
}