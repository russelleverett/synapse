package byobgyn.com.synapse.utils;

public abstract class EventListener {
    private boolean capture;
    private boolean once;

    boolean isCapture() {
        return capture;
    }

    void setCapture(boolean capture) {
        this.capture = capture;
    }

    boolean isOnce() {
        return once;
    }

    void setOnce(boolean once) {
        this.once = once;
    }

    public abstract boolean handleEvent(Event event);
}
