package byobgyn.com.synapse.entities;

import byobgyn.com.synapse.utils.EventDispatcher;
import byobgyn.com.synapse.events.PlayerEvent;
import byobgyn.com.synapse.utils.JsonDoc;

public class Player {
    private String identifier;
    private String tags;
    private String credits;
    private int slots;
    private int notoriety;
    private int combat;
    private int commerce;

    private static Player sInstance;
    public static void initialize(JsonDoc doc) {
        sInstance = new Player();
        sInstance.setIdentifier(doc.getString("Identifier"));
        sInstance.setTags(doc.getString("Tags"));
        sInstance.setCredits(doc.getString("Credits"));
        sInstance.setSlots(doc.getInt("Slots"));
        sInstance.setNotoriety(doc.getInt("Notoriety"));
        sInstance.setCombat(doc.getInt("Combat"));
        sInstance.setCommerce(doc.getInt("Commerce"));

        EventDispatcher.dispatchEvent(new PlayerEvent(PlayerEvent.PLAYER_UPDATED, sInstance));
    }

    public static Player getInstance() { return sInstance; }

    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getCredits() {
        return credits.substring(0, credits.indexOf('.'));
    }
    public void setCredits(String credits) {
        this.credits = credits;
    }
    public int getSlots() {
        return slots;
    }
    public void setSlots(int slots) {
        this.slots = slots;
    }
    public int getNotoriety() {
        return notoriety;
    }
    public void setNotoriety(int notoriety) {
        this.notoriety = notoriety;
    }
    public int getCombat() {
        return combat;
    }
    public void setCombat(int combat) {
        this.combat = combat;
    }
    public int getCommerce() {
        return commerce;
    }
    public void setCommerce(int commerce) {
        this.commerce = commerce;
    }
}
