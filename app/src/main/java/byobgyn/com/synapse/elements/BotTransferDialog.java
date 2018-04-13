package byobgyn.com.synapse.elements;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Node;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.StringUtils;

public class BotTransferDialog extends Dialog implements View.OnClickListener {
    private Bot bot;
    private Node node;
    private SynapseTaskListener taskListener;

    public BotTransferDialog(Context context, Bot bot, Node node) {
        super(context);

        taskListener = (SynapseTaskListener)context;
        this.bot = bot;
        this.node = node;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bot_transfer);

        TextView txtName = (TextView)findViewById(R.id.txtName);
        TextView txtSecurity = (TextView)findViewById(R.id.txtSecurity);
        TextView txtGovernment = (TextView)findViewById(R.id.txtGovernment);
        TextView txtPopulation = (TextView)findViewById(R.id.txtPopulation);
        TextView txtEconomy = (TextView)findViewById(R.id.txtEconomy);
        TextView txtState = (TextView)findViewById(R.id.txtState);
        TextView txtDistance = (TextView)findViewById(R.id.txtDistance);
        TextView txtCost = (TextView)findViewById(R.id.txtCost);

        txtName.setText(node.getName());
        txtSecurity.setText(node.getSecurity());
        txtGovernment.setText(node.getGovernment());
        txtPopulation.setText(StringUtils.convert(node.getPopulation()));
        txtEconomy.setText(node.getEconomy());
        txtState.setText(node.getState());
        txtDistance.setText(String.format("%s LY", node.getDistance()));
        txtCost.setText(String.format("%s CR", StringUtils.convert(node.getCost())));

        Button cancel = (Button)findViewById(R.id.btnCancel);
        Button confirm = (Button)findViewById(R.id.btnConfirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnConfirm:
                SynapseManager.transferBot(taskListener, bot, node);
                break;
            default:
                dismiss();
        }
    }
}
