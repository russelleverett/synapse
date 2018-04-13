package byobgyn.com.synapse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;

import byobgyn.com.synapse.elements.BotTransferDialog;
import byobgyn.com.synapse.elements.NodeAdapter;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Node;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.JsonDoc;

public class BotTransferActivity extends AppCompatActivity implements SynapseTaskListener {
    private BotTransferDialog dialog;
    private NodeAdapter nodeAdapter;
    private ArrayList<Node> nodes;
    private Bot bot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_transfer);

        // keep the keyboard hidden until we want it
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bot = Parcels.unwrap(getIntent().getParcelableExtra("bot"));

        nodes = new ArrayList<>();
        nodeAdapter = new NodeAdapter(getApplicationContext(), R.layout.node_list_item, nodes);

        ListView list = (ListView)findViewById(R.id.nodeList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Node node = nodes.get(position);
                if(node != null) {
                    dialog = new BotTransferDialog(BotTransferActivity.this, bot, node);
                    dialog.show();
                }
            }
        });
        list.setAdapter(nodeAdapter);

        final TextView filterText = (TextView)findViewById(R.id.txtFilter);
        Button filter = (Button) findViewById(R.id.btnFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynapseManager.getTransferLocations(BotTransferActivity.this, bot, filterText.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SynapseManager.getTransferLocations(this, bot, null);
    }

    public void TaskComplete(int messageId, JsonDoc response) {
        if(messageId == SynapseManager.TRANSFER_OPTIONS) {
            nodeAdapter.clear();
            for(JsonDoc doc: response.getEnumerator("systems")) {
                nodeAdapter.add(Node.fromJson(doc));
            }
        }
        else if(messageId == SynapseManager.TRANSFER_COMPLETE) {
            if(response.has("bot")) {
                this.setResult(messageId, null);
                dialog.dismiss();
                finish();
            }
            else if (response.has("message")) {
                dialog.dismiss();
                Toast.makeText(BotTransferActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
            }
        }
    }
}
