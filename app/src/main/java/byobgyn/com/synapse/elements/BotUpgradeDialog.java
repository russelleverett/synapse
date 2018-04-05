package byobgyn.com.synapse.elements;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.math.BigDecimal;
import java.util.ArrayList;

import byobgyn.com.synapse.R;
import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Component;
import byobgyn.com.synapse.entities.Player;
import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.StringUtils;

public class BotUpgradeDialog extends Dialog implements View.OnClickListener {
    private Bot mBot;
    private Component mCurrent;
    private ArrayList<Component> mComponents;
    private SynapseTaskListener mTaskListener;

    private final Drawable tagImage;
    private final Drawable creditImage;
    private final int textColor;
    private final int backgroundColor;

    public BotUpgradeDialog(Context context, Bot bot, Component current, ArrayList<Component> components) {
        super(context);

        mTaskListener = (SynapseTaskListener)context;
        mBot = bot;
        mCurrent = current;
        mComponents = components;
        tagImage = ContextCompat.getDrawable(context, R.drawable.tag);
        creditImage = ContextCompat.getDrawable(getContext(), R.drawable.credits);
        textColor = ContextCompat.getColor(getContext(), R.color.highlight);
        backgroundColor = ContextCompat.getColor(getContext(), R.color.highlight_alpha);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bot_upgrade);

        ToggleButton currency = (ToggleButton)findViewById(R.id.credit_tag_toggle);
        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateRadioButtons();
            }
        });

        populateRadioButtons();

        Button cancel = (Button)findViewById(R.id.btnCancel);
        Button confirm = (Button)findViewById(R.id.btnConfirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private void populateRadioButtons() {
        ToggleButton currency = (ToggleButton)findViewById(R.id.credit_tag_toggle);

        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 7);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.upgradeChoice);
        if(radioGroup != null) {
            radioGroup.removeAllViews();

            BigDecimal playerCredits = new BigDecimal(currency.isChecked() ? Player.getInstance().getTags() : Player.getInstance().getCredits());
            for (Component c : mComponents) {
                BigDecimal compAmount = new BigDecimal(currency.isChecked() ? c.getTags() : c.getCredits());
                compAmount = compAmount.subtract(new BigDecimal(currency.isChecked() ? mCurrent.getTags() : mCurrent.getCredits()));

                boolean currencyOverride = false;
                if (currency.isChecked() && compAmount.compareTo(BigDecimal.ZERO) < 0) {
                    currencyOverride = true;
                    compAmount = new BigDecimal(c.getCredits()).subtract(new BigDecimal(mCurrent.getCredits()));
                }

                boolean canAfford = currencyOverride || compAmount.compareTo(playerCredits) < 0;
                String amount = compAmount.toString();
                if (amount.contains("."))
                    amount = amount.substring(0, amount.indexOf('.'));
                amount = StringUtils.convert(amount) + (currency.isChecked() && !currencyOverride ? " TAGS" : " CR");

                Drawable icon = currency.isChecked() && !currencyOverride ? tagImage : creditImage;

                RadioButton rb = new RadioButton(getContext());
                rb.setLayoutParams(layoutParams);
                rb.setText(String.format("%s %s\r\n%s\r\n%s", c.getComponentType(), c.getClassRating(), "description", amount));
                rb.setTag(c);
                rb.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                rb.setCompoundDrawablePadding(40);
                rb.setTextColor(textColor);
                rb.setBackgroundColor(backgroundColor);
                rb.setPadding(20, 20, 20, 20);
                rb.setEnabled(canAfford);
                radioGroup.addView(rb);
            }
        }
    }

    public void onClick(View view) {
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.upgradeChoice);
        ToggleButton currency = (ToggleButton)findViewById(R.id.credit_tag_toggle);

        switch(view.getId()) {
            case R.id.btnConfirm:
                RadioButton rb = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                Component selected = (Component)rb.getTag();
                if(currency.isChecked())
                    selected.setCredits("0");
                else selected.setTags("0");

                SynapseManager.upgradeComponent(mTaskListener, mBot, selected);
                break;
            default:
                dismiss();
        }
    }
}
