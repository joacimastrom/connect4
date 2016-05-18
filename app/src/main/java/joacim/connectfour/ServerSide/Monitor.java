package joacim.connectfour.ServerSide;
public class Monitor {
    int turn;
    int currentMove = -1;
    public Monitor() {
        turn = 1;
    }
    public synchronized int waitTurn(ServerThread c) {
        while (!(c.id == turn)) {
            try {
                System.out.println("väntar");
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