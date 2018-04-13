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
    private String notorietyRank;
    private String combatRank;
    private String commerceRank;
    private int notorietyProgress;
    private int combatProgress;
    private int commerceProgress;

    private static Player sInstance;
    public static void initialize(JsonDoc doc) {
        sInstance = new Player();
        sInstance.setIdentifier(doc.getString("Identifier"));
        sInstance.setTags(doc.getString("Tags"));
        sInstance.setCredits(doc.getString("Credits"));
        sInstance.setSlots(doc.getInt("Slots"));
        sInstance.setNotoriety(doc.getInt("Notoriety"));
        sInstance.setNotorietyRank(doc.getString("NotorietyRank"));
        sInstance.setNotorietyProgress(doc.getInt("NotorietyProgress"));
        sInstance.setCombat(doc.getInt("Combat"));
        sInstance.setCombatRank(doc.getString("CombatRank"));
        sInstance.setCombatProgress(doc.getInt("CombatProgress"));
        sInstance.setCommerce(doc.getInt("Commerce"));
        sInstance.setCommerceRank(doc.getString("CommerceRank"));
        sInstance.setCommerceProgress(doc.getInt("CommerceProgress"));

        EventDispatcher.dispatchEvent(new PlayerEvent(PlayerEvent.PLAYER_UPDATED, sInstance));
    }

    public static Player getInstance() { return sInstance; }

    public String getIdentifier() {
        return identifier;
    }
    private void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getTags() {
        return tags;
    }
    private void setTags(String tags) {
        this.tags = tags;
    }
    public String getCredits() {
        return credits.substring(0, credits.indexOf('.'));
    }
    private void setCredits(String credits) {
        this.credits = credits;
    }
    public int getSlots() {
        return slots;
    }
    private void setSlots(int slots) {
        this.slots = slots;
    }
    public int getNotoriety() {
        return notoriety;
    }
    private void setNotoriety(int notoriety) {
        this.notoriety = notoriety;
    }
    public int getCombat() {
        return combat;
    }
    private void setCombat(int combat) {
        this.combat = combat;
    }
    public int getCommerce() {
        return commerce;
    }
    private void setCommerce(int commerce) {
        this.commerce = commerce;
    }
    public String getNotorietyRank() {
        return notorietyRank;
    }
    private void setNotorietyRank(String notorietyRank) {
        this.notorietyRank = notorietyRank;
    }
    public String getCombatRank() {
        return combatRank;
    }
    private void setCombatRank(String combatRank) {
        this.combatRank = combatRank;
    }
    public String getCommerceRank() {
        return commerceRank;
    }
    private void setCommerceRank(String commerceRank) {
        this.commerceRank = commerceRank;
    }
    public int getNotorietyProgress() {
        return notorietyProgress;
    }
    private void setNotorietyProgress(int notorietyProgress) {
        this.notorietyProgress = notorietyProgress;
    }
    public int getCombatProgress() {
        return combatProgress;
    }
    private void setCombatProgress(int combatProgress) {
        this.combatProgress = combatProgress;
    }
    public int getCommerceProgress() {
        return commerceProgress;
    }
    private void setCommerceProgress(int commerceProgress) {
        this.commerceProgress = commerceProgress;
    }

    public int getRank(int facetId) {
        if(facetId == 0)
            return Integer.parseInt(combatRank.substring(combatRank.indexOf(" ") + 1));
        else if (facetId == 1)
            return Integer.parseInt(commerceRank.substring(commerceRank.indexOf(" ") + 1));
        else return Integer.parseInt(notorietyRank.substring(notorietyRank.indexOf(" ") + 1));
    }
}
