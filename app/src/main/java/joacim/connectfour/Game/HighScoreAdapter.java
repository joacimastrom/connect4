package joacim.connectfour.Game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import joacim.connectfour.R;


// Custom adapter to populate listview
public class HighScoreAdapter extends BaseAdapter{

    ArrayList<Player> players;
    Context context;

    public HighScoreAdapter(Context context, List<Player> players) {
        this.players = (ArrayList) players;
        this.context = context;
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.myrow, null);
        }

        // Sets and populates the textfields
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView wins = (TextView) convertView.findViewById(R.id.wins);
        name.setText((position+1) + ".  " + players.get(position).getName());
        wins.setText("" + players.get(position).getWins());
        return convertView;
    }
}