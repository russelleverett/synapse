package byobgyn.com.synapse.entities;

import org.parceler.Parcel;

import byobgyn.com.synapse.utils.JsonDoc;

@Parcel(Parcel.Serialization.BEAN)
public class Node {
    private int id;
    private String name;
    private String government;
    private String security;
    private String economy;
    private String state;
    private String population;

    // for transfer options
    private String distance;
    private String cost;
    private int duration;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getGovernment() {
        return government;
    }
    public void setGovernment(String government) {
        this.government = government;
    }
    public String getSecurity() {
        return security;
    }
    public void setSecurity(String security) {
        this.security = security;
    }
    public String getEconomy() {
        return economy;
    }
    public void setEconomy(String economy) {
        this.economy = economy;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getPopulation() {
        return population;
    }
    public void setPopulation(String population) {
        this.population = population;
    }

    public static Node fromJson(JsonDoc doc) {
        Node node = new Node();
        node.setId(doc.getInt("Id"));
        node.setName(doc.getString("Name"));
        node.setGovernment(doc.getString("Government"));
        node.setSecurity(doc.getString("Security"));
        node.setPopulation(doc.getString("Population"));
        node.setState(doc.getString("State"));
        node.setEconomy(doc.getString("Economy"));

        if(doc.has("Distance")) {
            node.setDistance(doc.getString("Distance"));
            node.setCost(doc.getString("Cost"));
            node.setDuration(doc.getInt("Time"));
        }
        return node;
    }
}
