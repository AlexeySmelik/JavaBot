package JavaBot.resources;

import JavaBot.handlers.ContextEventManager;
import JavaBot.handlers.EventListener;
import JavaBot.handlers.EventManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Context {
    public final Integer id;

    private final EventManager<Context> manager;
    private final Map<String, Object> data;

    public Context(Integer id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
        var operations = data == null ? new HashSet<String>() : new HashSet<>(data.keySet());
        manager = new ContextEventManager(new ArrayList<>(operations));
    }

    public Object get(String name) {
        return data.getOrDefault(name, null);
    }

    public void update(String name, Object value) {
        if (!data.containsKey(name))
            return;
        data.replace(name, value);
        manager.notify(name, this);
    }

    public void addEventListener(String eventType, EventListener<Context> listener) {
        manager.add(eventType, listener);
    }

    public void removeEventListener(String eventType, EventListener<Context> listener) {
        manager.remove(eventType, listener);
    }
}
