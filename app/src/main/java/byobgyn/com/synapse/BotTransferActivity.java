package byobgyn.com.synapse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import byobgyn.com.synapse.elements.NodeAdapter;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Node;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.JsonDoc;

public class BotTransferActivity extends AppCompatActivity {
    private NodeAdapter nodeAdapter;
    private ArrayList<Node> nodes;
    private Bot bot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_transfer);

        bot = Parcels.unwrap(getIntent().getParcelableExtra("bot"));

        nodes = new ArrayList<>();
        nodeAdapter = new NodeAdapter(getApplicationContext(), R.layout.node_list_item, nodes);

        ListView list = (ListView)findViewById(R.id.nodeList);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Show transfer dialog
            }
        });
        list.setAdapter(nodeAdapter);

        final TextView filterText = (TextView)findViewById(R.id.txtFilter);
        Button filter = (Button) findViewById(R.id.btnFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeAdapter.getFilter().filter(filterText.getText());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SynapseManager.getTransferLocations(new SynapseTaskListener() {
            @Override
            public void TaskComplete(int messageId, JsonDoc response) {
                nodeAdapter.clear();
                for(JsonDoc doc: response.getEnumerator("systems")) {
                    nodeAdapter.add(Node.fromJson(doc));
                }
            }
        }, bot);
    }
}
