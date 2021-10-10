package JavaBot.handlers;

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
        List<EventListener<Context>> users = listeners.get(eventType);
        users.add(listener);
    }

    public void remove(String eventType, EventListener<Context> listener) {
        List<EventListener<Context>> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, Context context) {
        List<EventListener<Context>> users = listeners.get(eventType);
        for (EventListener<Context> listener : users)
            listener.update(eventType, context);
    }
}

