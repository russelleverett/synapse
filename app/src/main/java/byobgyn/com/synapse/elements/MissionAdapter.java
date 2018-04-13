package byobgyn.com.synapse.elements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.entities.Mission;

public class MissionAdapter extends ArrayAdapter<Mission> {
    private ArrayList<Mission> missions;
    private LayoutInflater inflater;

    public MissionAdapter(Context context, int textViewResourceId, ArrayList<Mission> missions) {
        super(context, textViewResourceId, missions);
        this.missions = missions;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.mission_list_item, null);

            holder = new ViewHolder();
            holder.missionText = (TextView)convertView.findViewById(R.id.mission_text);
            holder.missionRewards = (TextView)convertView.findViewById(R.id.mission_rewards);
            holder.missionDestination = (TextView)convertView.findViewById(R.id.mission_destination);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        Mission mission = this.missions.get(position);
        if(mission != null) {
            holder.missionText.setText(mission.getMissionText());
            holder.missionRewards.setText(mission.getRewards());
            holder.missionDestination.setText(mission.getDestination());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView missionText;
        TextView missionRewards;
        TextView missionDestination;
    }
}
