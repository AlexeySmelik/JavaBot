package JavaBot;

import JavaBot.resources.WordAndTranslate;

import java.util.ArrayList;
import java.util.HashMap;

public class WordStore implements Store {
    private final HashMap<String, ArrayList<WordAndTranslate>> topics;

    public WordStore() {
        topics = new HashMap<>();
    }

    public ArrayList<String> getTopics(){
        return new ArrayList<>(topics.keySet());
    }

    @Override
    public ArrayList<WordAndTranslate> get(String topic) {
        return topics.get(topic);
    }

    @Override
    public void add(String theme, ArrayList<WordAndTranslate> tuple) {
        topics.put(theme, tuple);
    }
}
