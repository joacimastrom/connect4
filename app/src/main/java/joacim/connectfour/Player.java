package joacim.connectfour;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by joacim on 25/01/16.
 */
public class Player implements Comparable<Player>{
    String name;
    int wins;

    public Player(String name) {
        this.name = name;
        wins = 0;
    }

    public Player(Parcel p) {
        this.name = p.readString();
        this.wins = p.readInt();
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


    // Custom comparator for player object
    @Override
    public int compareTo(Player c) {
        if (wins > c.getWins()) {
            return -1;
        } else if (wins < c.getWins()) {
            return 1;
        } else {
            return 0;
        }

    }

    public void resetWins() {
        wins = 0;
    }
}