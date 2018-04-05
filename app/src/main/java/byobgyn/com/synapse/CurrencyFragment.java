package byobgyn.com.synapse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import byobgyn.com.synapse.entities.Player;
import byobgyn.com.synapse.utils.Event;
import byobgyn.com.synapse.utils.EventDispatcher;
import byobgyn.com.synapse.utils.EventListener;
import byobgyn.com.synapse.events.PlayerEvent;
import byobgyn.com.synapse.utils.StringUtils;

public class CurrencyFragment extends Fragment {
    public CurrencyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);

        final TextView tags = (TextView)view.findViewById(R.id.tags);
        final TextView credits = (TextView)view.findViewById(R.id.credits);

        Player player = Player.getInstance();
        if(player != null) {
            tags.setText(StringUtils.convert(player.getTags()));
            credits.setText(StringUtils.convert(player.getCredits()));
        }

        EventDispatcher.addEventListener(PlayerEvent.PLAYER_UPDATED, new EventListener() {
            @Override
            public boolean handleEvent(Event event) {
                PlayerEvent playerEvent = (PlayerEvent)event;
                tags.setText(StringUtils.convert(playerEvent.getPlayer().getTags()));
                credits.setText(StringUtils.convert(playerEvent.getPlayer().getCredits()));
                return true;
            }
        });

        return view;
    }
}
