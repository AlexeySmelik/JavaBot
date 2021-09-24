package handlers;

import java.util.ArrayList;
import java.util.Map;

public class Context {
    public final Integer chat_id;
    public ContextEventManager manager;

    private final Map<String, Object> data;
    private String message;

    public Context(Integer chat_id, Map<String, Object> data) {
        this.chat_id = chat_id;
        this.data = data;
        var operations = new ArrayList<>(data.keySet());
        operations.add("message");
        manager = new ContextEventManager(operations);
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

    public void changeMessage(String newMessage) {
        message = newMessage;
        manager.notify("message", this);
    }

    public String getMessage() {
        return message;
    }
}