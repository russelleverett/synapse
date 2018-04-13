package byobgyn.com.synapse.elements;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import byobgyn.com.synapse.R;

public class SynapseDialog extends Dialog implements View.OnClickListener {
    private String caption;
    private String body;
    private Integer amount;
    private Integer tags;
    private View.OnClickListener listener;
    private boolean showCancel;

    public void setOnConfirmListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setTags(int tags) {
        this.tags = tags;
    }

    public SynapseDialog(Context context, String caption, String body) {
        this(context, caption, body, true);
    }
    public SynapseDialog(Context context, String caption, String body, boolean showCancel) {
        super(context);
        this.caption = caption;
        this.body = body;
        this.showCancel = showCancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_synapse);

        TextView txtCaption = (TextView)findViewById(R.id.txtCaption);
        txtCaption.setText(caption);

        TextView txtBody = (TextView)findViewById(R.id.txtBody);
        txtBody.setText(body);

        TextView txtAmount = (TextView)findViewById(R.id.txtAmount);
        if(amount != null && amount != 0) {
            txtAmount.setVisibility(View.VISIBLE);
            txtAmount.setText(String.format("%s CR", amount));
        }
        else txtAmount.setVisibility(View.GONE);

        TextView txtTags = (TextView)findViewById(R.id.txtTags);
        if(tags != null && tags != 0) {
            txtTags.setVisibility(View.VISIBLE);
            txtTags.setText(String.format("%s TAGS", tags));
        }
        else txtTags.setVisibility(View.GONE);

        Button cancel = (Button)findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);
        if(!this.showCancel) cancel.setVisibility(View.GONE);

        Button confirm = (Button)findViewById(R.id.btnConfirm);
        confirm.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnConfirm:
                if(listener != null)
                    listener.onClick(view);
                else dismiss();
                break;
            default:
                dismiss();
        }
    }
}
