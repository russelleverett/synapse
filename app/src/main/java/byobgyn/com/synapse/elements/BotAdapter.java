package byobgyn.com.synapse.elements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.entities.Bot;

public class BotAdapter extends ArrayAdapter<Bot> {
    private ArrayList<Bot> bots;

    public BotAdapter(Context context, int textViewResourceId, ArrayList<Bot> bots) {
        super(context, textViewResourceId, bots);
        this.bots = bots;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.bot_list_item, null);
        }

        Bot bot = bots.get(position);
        if(bot != null) {
            TextView botName = (TextView)v.findViewById(R.id.bot_name);
            botName.setText(bot.getName());

            TextView shipLocation = (TextView)v.findViewById(R.id.bot_location);
            shipLocation.setText(bot.getNode().getName());

            TextView shipRating = (TextView)v.findViewById(R.id.bot_rating);
            shipRating.setText(bot.getRating());
        }

        return v;
    }
}
