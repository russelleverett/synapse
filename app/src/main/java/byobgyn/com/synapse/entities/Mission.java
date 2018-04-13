package byobgyn.com.synapse.entities;

import org.parceler.Parcel;

import byobgyn.com.synapse.utils.JsonDoc;
import byobgyn.com.synapse.utils.StringUtils;

@Parcel(Parcel.Serialization.BEAN)
public class Mission {
    private int id;
    private String identifier;
    private String name;
    private String description;
    private int playerId;
    private int locationId;
    private int destinationId;
    private String destinationName;
    private String distance;
    private int factionId;
    private int missionTypeId;
    private int rank;
    private String credits;
    private int tags;
    private int progress;
    private int target;
    private String successChance;
    private int statusId;
    private String statusUpdate;

    // mission type
    private int successRate;
    private int facetId;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getIdentifier() {
        return identifier;
    }
    private void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getPlayerId() {
        return playerId;
    }
    private void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public int getLocationId() {
        return locationId;
    }
    private void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    public int getDestinationId() {
        return destinationId;
    }
    private void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
    public String getDestinationName() {
        return destinationName;
    }
    private void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
    public String getDistance() {
        return distance;
    }
    private void setDistance(String distance) {
        this.distance = distance;
    }
    public int getFactionId() {
        return factionId;
    }
    private void setFactionId(int factionId) {
        this.factionId = factionId;
    }
    public int getMissionTypeId() {
        return missionTypeId;
    }
    private void setMissionTypeId(int missionTypeId) {
        this.missionTypeId = missionTypeId;
    }
    public int getRank() {
        return rank;
    }
    private void setRank(int rank) {
        this.rank = rank;
    }
    public String getCredits() {
        return credits;
    }
    public void setCredits(String credits) {
        if(credits != null && credits.contains("."))
            this.credits = credits.substring(0, credits.indexOf("."));
        else this.credits = credits;
    }
    public int getTags() {
        return tags;
    }
    public void setTags(int tags) {
        this.tags = tags;
    }
    public String getMissionText() {
        return name + description;
    }
    public String getRewards() {
        return String.format("%s CR", StringUtils.convert(credits));
    }
    public String getDestination() {
        return String.format("%s [%sLY]", destinationName, distance);
    }
    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public int getTarget() {
        return target;
    }
    public void setTarget(int target) {
        this.target = target;
    }
    public String getSuccessChance() {
        return successChance;
    }
    public void setSuccessChance(String successChance) {
        if(successChance != null && successChance.contains("."))
            this.successChance = successChance.substring(0, successChance.indexOf("."));
        else this.successChance = successChance;
    }
    public int getStatusId() {
        return statusId;
    }
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
    public String getStatusUpdate() {
        return statusUpdate;
    }
    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }

    // Mission Type
    public int getFacetId() {
        return facetId;
    }
    private void setFacetId(int facetId) {
        this.facetId = facetId;
    }

    public int calculateSuccess(Player player, Bot bot) {
        int playerMissionOffset = ((rank - player.getRank(facetId)) * 2);
        int botOffset = (69 - bot.getRating().getBytes()[0]);
        return successRate - playerMissionOffset + botOffset;
    }

    public static Mission fromJson(JsonDoc doc) {
        Mission mission = new Mission();
        mission.setIdentifier(doc.getString("Identifier"));
        mission.setName(doc.getString("Name"));
        mission.setDescription(doc.getString("Description"));
        mission.setPlayerId(doc.getInt("PlayerId"));
        mission.setLocationId(doc.getInt("LocationId"));
        mission.setDestinationId(doc.getInt("DestinationId"));
        mission.setDestinationName(doc.getString("DestinationName"));
        mission.setDistance(doc.getString("Distance"));
        mission.setFactionId(doc.getInt("FactionId"));
        mission.setMissionTypeId(doc.getInt("MissionTypeId"));
        mission.setRank(doc.getInt("Rank"));
        mission.setCredits(doc.getString("Credits"));
        mission.setTags(doc.getInt("Tags"));
        mission.setProgress(doc.getInt("Progress"));
        mission.setTarget(doc.getInt("Target"));
        mission.setSuccessChance(doc.getString("SuccessChance"));
        mission.setStatusId(doc.getInt("MissionStatus"));
        mission.setStatusUpdate(doc.getString("StatusUpdate"));

        if(doc.has("MissionType")) {
            JsonDoc missionType = doc.get("MissionType");
            mission.setFacetId(missionType.getInt("FacetId"));
        }

        return mission;
    }
}
