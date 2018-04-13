package byobgyn.com.synapse.elements;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Mission;
import byobgyn.com.synapse.entities.Player;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;

public class BotMissionDialog extends Dialog implements View.OnClickListener {
    private Bot bot;
    private Mission mission;
    private SynapseTaskListener listener;

    public BotMissionDialog(Context context, Bot bot, Mission mission) {
        super(context);

        listener = (SynapseTaskListener)context;
        this.bot = bot;
        this.mission = mission;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bot_mission);

        TextView txtName = (TextView)findViewById(R.id.txtName);
        TextView txtDestination = (TextView)findViewById(R.id.txtDestination);
        TextView txtRank = (TextView)findViewById(R.id.txtRank);
        TextView txtDuration = (TextView)findViewById(R.id.txtDuration);
        TextView txtRewards = (TextView)findViewById(R.id.txtRewards);

        txtName.setText(mission.getMissionText());
        txtDestination.setText(String.format("%s [%sLY]", mission.getDestinationName(), mission.getDistance()));
        txtRank.setText(String.format("Rank %s", mission.getRank()));
        txtDuration.setText("10+ MINS");

        String rewards = String.format("%s CR", mission.getCredits());
        if(mission.getTags() != 0)
            rewards += String.format(", %s TAGS", mission.getTags());
        txtRewards.setText(rewards);

        Button cancel = (Button)findViewById(R.id.btnCancel);
        Button confirm = (Button)findViewById(R.id.btnConfirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnConfirm:
                SynapseManager.acceptMission(listener, bot, mission);
                break;
            default:
                dismiss();
        }
    }
}
