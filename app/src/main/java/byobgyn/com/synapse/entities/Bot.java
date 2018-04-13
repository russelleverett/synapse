package byobgyn.com.synapse.entities;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Minutes;
import org.parceler.Parcel;

import byobgyn.com.synapse.utils.JsonDoc;

@Parcel(Parcel.Serialization.BEAN)
public class Bot {
    private int id;
    private String name;
    private int sizeClass;
    private int baseStorage;
    private Component cpu;
    private Component memory;
    private Component storage;
    private Component bandwidth;
    private Node node;
    private String retailCost;
    private String totalCost;
    private int statusId;
    private String signalStrength;
    private int timeToComplete;
    private String repairCost;
    private String insuranceCost;
    private int integrity;
    private Mission mission;

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
    public int getSizeClass() {
        return sizeClass;
    }
    public void setSizeClass(int sizeClass) {
        this.sizeClass = sizeClass;
    }
    public int getBaseStorage() {
        return baseStorage;
    }
    public void setBaseStorage(int baseStorage) {
        this.baseStorage = baseStorage;
    }
    public Node getNode() {
        return node;
    }
    public void setNode(Node node) {
        this.node = node;
    }
    public String getRating() {
        int overall = (getRating(cpu) + getRating(memory) + getRating(storage) + getRating(bandwidth)) / 4;
        float integrityHit = integrity / 100f;
        overall = Math.round(overall * integrityHit);
        if (overall >= 90)
            return "A";
        else if (overall >= 80)
            return "B";
        else if (overall >= 70)
            return "C";
        else if (overall >= 60)
            return "D";
        else return "E";
    }
    public Component getCpu() {
        return cpu;
    }
    public void setCpu(Component cpu) {
        this.cpu = cpu;
    }
    public Component getMemory() {
        return memory;
    }
    public void setMemory(Component memory) {
        this.memory = memory;
    }
    public Component getStorage() {
        return storage;
    }
    public void setStorage(Component storage) {
        this.storage = storage;
    }
    public Component getBandwidth() {
        return bandwidth;
    }
    public void setBandwidth(Component bandwidth) {
        this.bandwidth = bandwidth;
    }
    public String getRetailCost() {
        return retailCost;
    }
    public void setRetailCost(String retailCost) {
        this.retailCost = retailCost;
    }
    public String getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(String totalCost) {
        if(totalCost != null) {
            if(totalCost.contains("."))
                this.totalCost = totalCost.substring(0, totalCost.indexOf("."));
            else this.totalCost = totalCost;
        }
    }
    public int getStatusId() {
        return statusId;
    }
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
    public String getSignalStrength() {
        return signalStrength;
    }
    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }
    public String getInsuranceCost() {
        return insuranceCost;
    }
    public void setInsuranceCost(String insuranceCost) {
        this.insuranceCost = insuranceCost;
    }
    public int getTimeToComplete() {
        return timeToComplete;
    }
    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }
    public String getRepairCost() {
        return repairCost;
    }
    public void setRepairCost(String repairCost) {
        if(repairCost != null && repairCost.contains("."))
            this.repairCost = repairCost.substring(0, repairCost.indexOf("."));
        this.repairCost = repairCost;
    }
    public int getIntegrity() {
        return integrity;
    }
    public void setIntegrity(int integrity) {
        this.integrity = integrity;
    }
    public Mission getMission() {
        return mission;
    }
    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public static Bot fromJson(JsonDoc doc) {
        Bot rvalue = new Bot();
        rvalue.setId(doc.getInt("Id"));
        rvalue.setName(doc.getString("Name"));
        rvalue.setSizeClass(doc.getInt("SizeClass"));
        rvalue.setStatusId(doc.getInt("Status"));
        rvalue.setBaseStorage(doc.getInt("BaseStorage"));
        rvalue.setSignalStrength(doc.getString("SignalStrength"));
        rvalue.setIntegrity(doc.getInt("Integrity"));

        // components
        rvalue.setCpu(Component.fromJson(doc.get("CPU")));
        rvalue.setMemory(Component.fromJson(doc.get("Memory")));
        rvalue.setStorage(Component.fromJson(doc.get("Storage")));
        rvalue.setBandwidth(Component.fromJson(doc.get("Bandwidth")));

        // location
        if(doc.has("Node")) {
            JsonDoc nodeDoc = doc.get("Node");
            if(nodeDoc != null) {
                rvalue.setNode(Node.fromJson(nodeDoc));
            }
        }

        // mission
        if(doc.has("Mission")) {
            JsonDoc missionNode = doc.get("Mission");
            if(missionNode != null)
                rvalue.setMission(Mission.fromJson(missionNode));
        }

        // cost
        rvalue.setRetailCost(doc.getString("RetailCost"));
        rvalue.setTotalCost(doc.getString("TotalValue"));
        rvalue.setRepairCost(doc.getString("RepairCost"));
        rvalue.setInsuranceCost(doc.getString("InsuranceCost"));

        // completion date
        String completionDate = doc.getString("CompletionDate");
        if(completionDate != null) {
            DateTime completionTime = DateTime.parse(completionDate);
            rvalue.setTimeToComplete(Minutes.minutesBetween(DateTime.now(DateTimeZone.UTC), completionTime).getMinutes());
        }

        return rvalue;
    }

    private int getRating(Component component) {
        switch (component.getRating()) {
            case "A":
                return 90;
            case "B":
                return 80;
            case "C":
                return 70;
            case "D":
                return 60;
            default:
                return 50;
        }
    }
}
