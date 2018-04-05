package byobgyn.com.synapse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import byobgyn.com.synapse.elements.SlotPurchaseDialog;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.elements.BotAdapter;
import byobgyn.com.synapse.entities.Player;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.JsonDoc;

public class BotSelectActivity extends AppCompatActivity implements SynapseTaskListener {
    private BotAdapter botAdapter;
    private ArrayList<Bot> mBots;

    private TextView availableBots;
    private SwipeRefreshLayout refreshBots;
    private SlotPurchaseDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_select);

        availableBots = (TextView)findViewById(R.id.available_bots);

        // handle the list view
        ListView botList = (ListView) findViewById(R.id.list);
        botList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Bot bot = mBots.get(position - 1);
                if(bot != null) {
                    if(!bot.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), BotDetailsActivity.class);
                        intent.putExtra("bot", Parcels.wrap(bot));
                        startActivity(intent);
                    }
                    else {
                        // TODO: PURCHASE BOT
                    }
                }
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.bot_list_item_header, botList, false);
        botList.addHeaderView(header);

        // bind my data source
        mBots = new ArrayList<>();
        botAdapter = new BotAdapter(getApplicationContext(), R.layout.bot_list_item, mBots);
        botList.setAdapter(botAdapter);

        // slot upgradeComponent
        FloatingActionButton purchaseSlot = (FloatingActionButton) findViewById(R.id.purchase_slot);
        purchaseSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cost = Player.getInstance().getSlots() * 100;
                dialog = new SlotPurchaseDialog(BotSelectActivity.this, cost);
                dialog.show();
            }
        });
        purchaseSlot.setEnabled(Player.getInstance().getSlots() <= 10);

        // refresh bot list
        refreshBots = (SwipeRefreshLayout)findViewById(R.id.refresh_list);
        refreshBots.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SynapseManager.loadBotList(BotSelectActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBotList();
    }

    private void loadBotList() {
        refreshBots.setRefreshing(true);
        SynapseManager.loadBotList(this);
    }

    public void TaskComplete(int messageId, JsonDoc response) {
        if(messageId == SynapseManager.BOT_LOAD_COMPLETE) {
            botAdapter.clear();
            for (JsonDoc botDoc : response.getEnumerator("bots")) {
                botAdapter.add(Bot.fromJson(botDoc));
            }

            // set bot count
            availableBots.setText(String.format("AVAILABLE BOTS: %s / %s", botAdapter.getCount(), Player.getInstance().getSlots()));
            refreshBots.setRefreshing(false);
        }
        else if (messageId == SynapseManager.PURCHASE_SLOT) {
            loadBotList();
            if(dialog != null) {
                dialog.dismiss();
            }
        }
    }
}
