package JavaBot.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Context {
    public final Integer chat_id;
    public ContextEventManager manager;

    private final Map<String, Object> data;
    private String message;

    public Context(Integer chat_id, Map<String, Object> data) {
        this.chat_id = chat_id;
        this.data = data;
        var operations = data == null ? new HashSet<String>() : new HashSet<>(data.keySet());
        operations.add("updateMessage");
        manager = new ContextEventManager(new ArrayList<>(operations));
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

    public void updateMessage(String newMessage) {
        message = newMessage;
        manager.notify("updateMessage", this);
    }

    public Integer getChat_id() {
        return chat_id;
    }

    public String getMessage() {
        return message;
    }
}
