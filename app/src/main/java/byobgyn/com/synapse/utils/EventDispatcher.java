package byobgyn.com.synapse.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDispatcher {
    private static EventDispatcher sInstance;
    private HashMap<String, ArrayList<EventListener>> mEventListeners;

    static {
        sInstance = new EventDispatcher();
    }

    private EventDispatcher() {
        mEventListeners = new HashMap<>();
    }

    static public void addEventListener(String eventId, EventListener listener) {
        addEventListener(eventId, listener, false, false);
    }

    static public void addEventListener(String eventId, EventListener listener, boolean useCapture) {
        addEventListener(eventId, listener, useCapture, false);
    }

    static public void addEventListener(String eventId, EventListener listener, boolean useCapture, boolean once) {
        listener.setCapture(useCapture);
        listener.setOnce(once);

        if(!sInstance.mEventListeners.containsKey(eventId))
            sInstance.mEventListeners.put(eventId, new ArrayList<EventListener>());
        sInstance.mEventListeners.get(eventId).add(listener);
    }

    static public void dispatchEvent(Event event) {
        String eventId = event.getEventId();
        if(sInstance.mEventListeners.containsKey(eventId)) {
            ArrayList<EventListener> captures = new ArrayList<>();
            ArrayList<EventListener> normals = new ArrayList<>();

            for(EventListener listener: sInstance.mEventListeners.get(eventId)) {
                if(listener.isCapture())
                    captures.add(listener);
                else normals.add(listener);
            }

            ArrayList<EventListener> listeners = new ArrayList<>(captures);
            listeners.addAll(normals);

            ArrayList<EventListener> removals = new ArrayList<>();
            try {
                for (EventListener listener : listeners) {
                    if (!event.isPropagationStopped()) {
                        if(listener.isOnce()) {
                            removals.add(listener);
                        }

                        if((!listener.handleEvent(event) && event.isCancellable()) || !event.bubbles())
                            return;
                    }
                }
            }
            finally {
                for(EventListener listener: removals)
                    removeEventListener(eventId, listener);
            }
        }
    }

    static public void removeEventListener(String eventId, EventListener listener) {
        if(sInstance.mEventListeners.containsKey(eventId)) {
            sInstance.mEventListeners.get(eventId).remove(listener);
        }
    }
}
