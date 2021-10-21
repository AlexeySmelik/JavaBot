package JavaBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WordStore implements IStore {
    private final HashMap<String, ArrayList<ITuple>> topics;
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
    public ArrayList<ITuple> get(String topicName) {
        return topics.get(topicName);
    }

    @Override
    public void addT(String theme, ArrayList<ITuple> tuple) {
        topics.put(theme, tuple);
    }
}
