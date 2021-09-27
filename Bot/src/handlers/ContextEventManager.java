package handlers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextEventManager implements EventManager<Context> {
    private final Map<String, List<EventListener>> listeners;

    public ContextEventManager(@NotNull List<String> operations) {
        listeners = new HashMap<>();
        for (var operation : operations)
            listeners.put(operation, new ArrayList<>());
    }

    public void add(@NotNull String eventType, @NotNull EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void remove(@NotNull String eventType, @NotNull EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(@NotNull String eventType, Context context) {
        List<EventListener> users = listeners.get(eventType);
        for (EventListener listener : users)
            listener.update(eventType, context);
    }
}
