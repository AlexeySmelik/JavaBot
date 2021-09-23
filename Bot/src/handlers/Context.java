package handlers;

import java.util.ArrayList;
import java.util.Map;

public class Context {
    public final Integer chat_id;
    public String message;
    public ContextEventManager manager;

    private final Map<String, Object> data;

    public Context(String message, Integer chat_id, Map<String, Object> data) {
        this.message = message;
        this.chat_id = chat_id;
        this.data = data;
        manager = new ContextEventManager(new ArrayList<>(data.keySet()));
    }

    public Object get(String name) {
        return data.getOrDefault(name, null);
    }

    public void set(String name, Object value) {
        if (!data.containsKey(name))
            return;
        data.replace(name, value);
        manager.notify(name, this);
    }
}