package JavaBot;

import JavaBot.resources.WordAndTranslate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordStore implements Store {
    private final HashMap<String, List<WordAndTranslate>> topics;

    public WordStore() {
        topics = new HashMap<>();
    }

    public ArrayList<String> getTopics(){
        return new ArrayList<>(topics.keySet());
    }

    @Override
    public List<WordAndTranslate> get(String topic) {
        return topics.get(topic);
    }

    @Override
    public void add(String theme, List<WordAndTranslate> tuple) {
        topics.put(theme, tuple);
    }
}
