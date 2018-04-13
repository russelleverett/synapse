package byobgyn.com.synapse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;

import byobgyn.com.synapse.elements.BotMissionDialog;
import byobgyn.com.synapse.elements.MissionAdapter;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Mission;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.JsonDoc;

public class BotMissionActivity extends AppCompatActivity implements SynapseTaskListener {
    private Bot bot;
    private BotMissionDialog dialog;
    private MissionAdapter missionAdapter;
    private ArrayList<Mission> missions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_mission);

        bot = Parcels.unwrap(getIntent().getParcelableExtra("bot"));

        missions = new ArrayList<>();
        missionAdapter = new MissionAdapter(getApplicationContext(), R.layout.mission_list_item, missions);

        ListView missionList = (ListView)findViewById(R.id.mission_list);
        missionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Mission mission = missions.get(position);
                if(mission != null) {
                    dialog = new BotMissionDialog(BotMissionActivity.this, bot, mission);
                    dialog.show();
                }
            }
        });
        missionList.setAdapter(missionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SynapseManager.getMissions(this, bot);
    }

    @Override
    public void TaskComplete(int messageId, JsonDoc response) {
        if(messageId == SynapseManager.MISSION_OPTIONS) {
            missionAdapter.clear();
            for(JsonDoc doc: response.getEnumerator("missions")) {
                missionAdapter.add(Mission.fromJson(doc));
            }
        }
        else if(messageId == SynapseManager.MISSION_ACCEPTED) {
            dialog.dismiss();
            if(response.has("bot")) {
                this.setResult(messageId, null);
                finish();
            }
            else if(response.has("missions")) {
                missionAdapter.clear();
                for(JsonDoc doc: response.getEnumerator("missions")) {
                    missionAdapter.add(Mission.fromJson(doc));
                }
                Toast.makeText(BotMissionActivity.this, "Mission no longer available, refreshing mission list.", Toast.LENGTH_LONG).show();
            }
            else if (response.has("message")) {
                Toast.makeText(BotMissionActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
            }
        }
    }
}
