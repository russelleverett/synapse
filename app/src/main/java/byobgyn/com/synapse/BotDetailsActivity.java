package byobgyn.com.synapse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import byobgyn.com.synapse.elements.BotUpgradeDialog;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Component;
import byobgyn.com.synapse.entities.enums.ComponentType;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.JsonDoc;
import byobgyn.com.synapse.utils.StringUtils;

public class BotDetailsActivity extends AppCompatActivity implements View.OnClickListener, SynapseTaskListener {
    private BotUpgradeDialog dialog;
    private TextView botName;
    private TextView botNode;
    private TextView botRating;
    private TextView txtCpu;
    private TextView txtMemory;
    private TextView txtStorage;
    private TextView txtBandwidth;
    private TextView txtValue;
    private TextView txtInsurance;
    private Bot bot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_details);

        bot = Parcels.unwrap(getIntent().getParcelableExtra("bot"));
        botName = (TextView)findViewById(R.id.bot_type);
        botNode = (TextView)findViewById(R.id.bot_location);
        botRating = (TextView)findViewById(R.id.bot_rating);
        txtCpu = (TextView)findViewById(R.id.cpu);
        txtMemory = (TextView)findViewById(R.id.memory);
        txtStorage = (TextView)findViewById(R.id.storage);
        txtBandwidth = (TextView)findViewById(R.id.bandwidth);
        txtValue = (TextView)findViewById(R.id.bot_value);
        txtInsurance = (TextView)findViewById(R.id.bot_insurance);

        Button btnTransfer = (Button)findViewById(R.id.btnTransfer);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BotTransferActivity.class);
                intent.putExtra("bot", Parcels.wrap(bot));
                startActivityForResult(intent, SynapseManager.TRANSFER_OPTIONS);
            }
        });

        Button btnMissions = (Button)findViewById(R.id.btnMissions);
        btnMissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BotMissionActivity.class);
                intent.putExtra("bot", Parcels.wrap(bot));
                startActivityForResult(intent, SynapseManager.MISSION_OPTIONS);
            }
        });

        Button btnUpgradeCpu = (Button)findViewById(R.id.btnUpgradeCpu);
        Button btnUpgradeMemory = (Button)findViewById(R.id.btnUpgradeMemory);
        Button btnUpgradeStorage = (Button)findViewById(R.id.btnUpgradeStorage);
        Button btnUpgradeBandwidth = (Button)findViewById(R.id.btnUpgradeBandwidth);

        btnUpgradeCpu.setOnClickListener(this);
        btnUpgradeMemory.setOnClickListener(this);
        btnUpgradeStorage.setOnClickListener(this);
        btnUpgradeBandwidth.setOnClickListener(this);

        btnUpgradeCpu.setTag(ComponentType.CPU);
        btnUpgradeMemory.setTag(ComponentType.Memory);
        btnUpgradeStorage.setTag(ComponentType.Storage);
        btnUpgradeBandwidth.setTag(ComponentType.Bandwidth);
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateBotData(bot);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SynapseManager.TRANSFER_OPTIONS && resultCode == SynapseManager.TRANSFER_COMPLETE)
            this.finish();
        else if (requestCode == SynapseManager.MISSION_OPTIONS && resultCode == SynapseManager.MISSION_ACCEPTED)
            this.finish();
    }

    public void onClick(View view) {
        ComponentType type = (ComponentType)view.getTag();
        SynapseManager.getUpgradeOptions(this, bot, type);
    }

    private void populateBotData(Bot bot) {
        if(bot == null)
            return;

        botName.setText(bot.getName());
        botNode.setText(bot.getNode().getName());
        botRating.setText(bot.getRating());
        txtCpu.setText(bot.getCpu().getClassRating());
        txtMemory.setText(bot.getMemory().getClassRating());
        txtStorage.setText(bot.getStorage().getClassRating());
        txtBandwidth.setText(bot.getBandwidth().getClassRating());
        txtValue.setText(StringUtils.convert(bot.getTotalCost()));
        txtInsurance.setText(StringUtils.convert(bot.getInsuranceCost()));
    }

    public void TaskComplete(int messageId, JsonDoc response) {
        if(messageId == SynapseManager.UPGRADE_OPTIONS) {
            ArrayList<Component> components = new ArrayList<>();
            Component current = null;
            for (JsonDoc componentData : response.getEnumerator("components")) {
                Component component = Component.fromJson(componentData);
                if (component.getId().equals(bot.getCpu().getId()) ||
                        component.getId().equals(bot.getMemory().getId()) ||
                        component.getId().equals(bot.getStorage().getId()) ||
                        component.getId().equals(bot.getBandwidth().getId())) {
                    current = component;
                    continue;
                }

                components.add(component);
            }

            dialog = new BotUpgradeDialog(this, bot, current, components);
            dialog.show();
        }
        else if(messageId == SynapseManager.UPGRADE_COMPLETE) {
            if(response.has("bot")) {
                this.bot = Bot.fromJson(response.get("bot"));
                populateBotData(bot);
            }
            dialog.dismiss();
        }
    }
}
