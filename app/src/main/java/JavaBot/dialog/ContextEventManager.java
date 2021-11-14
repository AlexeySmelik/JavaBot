package JavaBot.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextEventManager implements EventManager<Context> {
    private final Map<String, List<EventListener<Context>>> listeners;

    public ContextEventManager(List<String> operations) {
        listeners = new HashMap<>();
        for (var operation : operations)
            listeners.put(operation, new ArrayList<>());
    }

    public void add(String eventType, EventListener<Context> listener) {
        listeners.get(eventType).add(listener);
    }

    public void remove(String eventType, EventListener<Context> listener) {
        listeners.get(eventType).remove(listener);
    }

    public void notify(String eventType, Context context) {
        for (var listener : listeners.get(eventType))
            listener.update(eventType, context);
    }
}

