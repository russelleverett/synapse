package byobgyn.com.synapse.elements;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;

public class SlotPurchaseDialog extends Dialog implements View.OnClickListener {
    private int mAmount;
    private SynapseTaskListener mTaskListener;

    public SlotPurchaseDialog(Context context, int amount) {
        super(context);

        mTaskListener = (SynapseTaskListener)context;
        mAmount = amount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_slot_purchase);

        TextView txtAmount = (TextView)findViewById(R.id.txtAmount);
        txtAmount.setText(String.format("Purchase additional slot for %s TAGS?", mAmount));

        Button confirm = (Button)findViewById(R.id.btnConfirm);
        Button cancel = (Button)findViewById(R.id.btnCancel);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnConfirm:
                SynapseManager.purchaseSlot(mTaskListener);
                break;
            default:
                dismiss();
        }
    }
}
