package byobgyn.com.synapse.elements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
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

            TextView shipRating = (TextView)v.findViewById(R.id.bot_rating);
            shipRating.setText(String.format("Rating: %s", bot.getRating()));

//            TextView botIntegrity = (TextView)v.findViewById(R.id.bot_integrity);
//            if(bot.getIntegrity() != 100)
//                botIntegrity.setText(String.format("Integrity: %s", bot.getIntegrity() + "%"));

            TextView botStatus = (TextView)v.findViewById(R.id.bot_location);
            ProgressBar missionProgress = (ProgressBar)v.findViewById(R.id.missionProgress);
            missionProgress.setVisibility(View.GONE);
            switch(bot.getStatusId()) {
                case 1:
                    botStatus.setText(R.string.mission_prep);
                    break;
                case 2:
                    if(bot.getTimeToComplete() <= 0) {
                        botStatus.setText(R.string.booting);
                    }
                    else {
                        botStatus.setText(String.format("TRANSFERRING TO %s", bot.getNode().getName()));
                        missionProgress.setVisibility(View.VISIBLE);
                    }
                    break;
                case 3:
                    botStatus.setText(R.string.destroyed);
                    break;
                case 4:
                    botStatus.setText(R.string.shutting_down);
                    break;
                case 5: // ON MISSION
                    botStatus.setText(bot.getMission().getStatusUpdate());
                    missionProgress.setMax(bot.getMission().getTarget());
                    missionProgress.setProgress(bot.getMission().getProgress());
                    missionProgress.setVisibility(View.VISIBLE);
                    break;
                case 6: // DAMAGED
                    botStatus.setText(R.string.damaged);
                    break;
                case 7: // MISSION SUMMARY
                    botStatus.setText(R.string.mission_complete);
                    break;
                default: // IDLE
                    if(bot.getNode() != null)
                        botStatus.setText(String.format("Orbiting: %s", bot.getNode().getName()));
                    else botStatus.setText("Orbiting: UNKNOWN");
            }
        }

        return v;
    }
}
