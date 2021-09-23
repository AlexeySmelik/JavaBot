package handlers;

import java.util.ArrayList;
import java.util.Map;

public class Context {
    public ContextEventManager manager;

    private final Map<String, String> data;

    public Context(Map<String, String> data) {
        this.data = data;
        manager = new ContextEventManager(new ArrayList<>(data.keySet()));
    }

    public String get(String name) {
        return data.getOrDefault(name, null);
    }

    public void set(String name, String value) {
        if (!data.containsKey(name))
            return;
        data.replace(name, value);
        manager.notify(name, this);
    }
}