package joacim.connectfour;


/**
 * Created by joacim on 25/01/16.
 */
public class Player implements Comparable<Player> {
    String name;
    int wins;


    public Player(String name) {
        this.name = name;
        wins = 0;
    }

    public int getWins() {
        return wins;
    }

    public String getName() {
        return name;
    }

    public void regWin() {
        wins++;
    }

    @Override
    public int compareTo(Player c) {
        if (wins > c.getWins()) {
            return 1;
        } else if (wins < c.getWins()) {
            return -1;
        } else {
            return 0;
        }

    }
}
