package byobgyn.com.synapse.events;

import byobgyn.com.synapse.entities.Player;
import byobgyn.com.synapse.utils.Event;

public class PlayerEvent extends Event {
    private Player player;

    public static final String PLAYER_UPDATED = "player.updated";

    public Player getPlayer() {
        return player;
    }

    public PlayerEvent(String eventId, Player player) {
        super(eventId, false, true);
        this.player = player;
    }
}
