package byobgyn.com.synapse.entities;

import org.parceler.Parcel;

import byobgyn.com.synapse.entities.enums.ComponentType;
import byobgyn.com.synapse.utils.JsonDoc;

@Parcel(Parcel.Serialization.BEAN)
public class Component {
    private String id;
    private int sizeClass;
    private String rating;
    private String credits;
    private String tags;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getSizeClass() {
        return sizeClass;
    }
    public void setSizeClass(int sizeClass) {
        this.sizeClass = sizeClass;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getCredits() {
        return credits;
    }
    public void setCredits(String credits) {
        if(credits.contains("."))
            this.credits = credits.substring(0, credits.indexOf('.'));
        else this.credits = credits;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getClassRating() { return this.sizeClass + this.rating; }
    public ComponentType getComponentType() {
        if(id.startsWith("p"))
            return ComponentType.CPU;
        else if (id.startsWith("t"))
            return ComponentType.Memory;
        else if (id.startsWith("F"))
            return ComponentType.Storage;
        else return ComponentType.Bandwidth;
    }

    public static Component fromJson(JsonDoc doc) {
        Component rvalue = new Component();
        rvalue.setId(doc.getString("Id"));
        rvalue.setSizeClass(doc.getInt("Class"));
        rvalue.setRating(doc.getString("Rating"));
        rvalue.setCredits(doc.getString("Credits"));
        rvalue.setTags(doc.getString("Tags"));
        return rvalue;
    }
}
