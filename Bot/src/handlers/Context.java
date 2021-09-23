package handlers;

import java.util.ArrayList;
import java.util.Map;

public class Context {
    public Integer AnsweredQuestions;
    public Integer CorrectAnswers;
    public ContextEventManager manager;
    public String lastInput;

    private final Map<String, String> data;

    public Context(Map<String, String> data) {
        this.data = data;
        manager = new ContextEventManager(new ArrayList<>(data.keySet()));
        AnsweredQuestions = 0;
        CorrectAnswers = 0;
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