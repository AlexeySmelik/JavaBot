package JavaBot;

import JavaBot.resources.WordAndTranslate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WordStore implements Store {
    private final HashMap<String, ArrayList<WordAndTranslate>> topics;
    private final ArrayList<String> topicsName;

    public WordStore() throws IOException {
        topics = new HashMap<>();
        HTMLParser parser = new HTMLParser();
        parser.parse(this);
        topicsName = new ArrayList<>(topics.keySet());
    }

    public ArrayList<String> getTopicsName(){
        return topicsName;
    }

    @Override
    public ArrayList<WordAndTranslate> get(String topicName) {
        return topics.get(topicName);
    }

    @Override
    public void addT(String theme, ArrayList<WordAndTranslate> tuple) {
        topics.put(theme, tuple);
    }
}
