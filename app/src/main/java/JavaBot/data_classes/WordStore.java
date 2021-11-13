package JavaBot.data_classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordStore implements Store {
    private final HashMap<String, List<Word>> topics;

    public WordStore() {
        topics = new HashMap<>();
    }

    public ArrayList<String> getTopics(){
        return new ArrayList<>(topics.keySet());
    }

    @Override
    public List<Word> get(String topic) {
        return topics.get(topic);
    }

    @Override
    public void add(String theme, List<Word> word) {
        topics.put(theme, word);
    }
}
