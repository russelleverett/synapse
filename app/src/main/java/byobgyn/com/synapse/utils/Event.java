package byobgyn.com.synapse.utils;

public class Event {
    private String eventId;
    private boolean cancellable = true;
    private boolean bubbles = true;
    private boolean defaultPrevented = false;
    private boolean propagationStopped = false;

    public String getEventId() {
        return eventId;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public boolean bubbles() {
        return bubbles;
    }

    public boolean isDefaultPrevented() {
        return defaultPrevented;
    }

    public boolean isPropagationStopped() {
        return propagationStopped;
    }

    public Event(String eventId) {
        this(eventId, true, true);
    }

    public Event(String eventId, boolean cancellable, boolean bubbles) {
        this.eventId = eventId;
        this.cancellable = cancellable;
        this.bubbles = bubbles;
    }

    public void preventDefault() {
        defaultPrevented = true;
    }

    public void stopPropagation() {
        propagationStopped = true;
    }
}
