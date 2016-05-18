package joacim.connectfour.Online.ServerSide;

import joacim.connectfour.Online.GameThread;

public class Monitor {
    int turn;
    int currentMove = -1;
    public Monitor() {
        turn = 1;
    }


    public synchronized int waitTurn(GameThread c) {
        while (!(c.id == turn)) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return currentMove;
    }


    public synchronized void addTurn(int response) {
        turn = (turn + 1) % 2;
        currentMove = response;
        notifyAll();
    }


    public synchronized void changeTurn() {
        turn = (turn + 1) % 2;
        notifyAll();
    }
}