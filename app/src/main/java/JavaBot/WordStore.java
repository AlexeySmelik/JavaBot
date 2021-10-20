package JavaBot;
import JavaBot.resources.WordAndTranslate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordStore implements IStore{
    public HashMap<String, ArrayList<ITuple>> DictionaryByTopics;

    public WordStore() throws IOException {
        DictionaryByTopics = new HashMap<>();
        HTMLParser parser = new HTMLParser();
        parser.parse(this);
    }

    public ArrayList<String> getTopics(){
        return new ArrayList<>(DictionaryByTopics.keySet());
    }

    @Override
    public IStore get() {
        return null;
    }

    @Override
    public void addT(String theme, ArrayList<ITuple> tuple) {
        DictionaryByTopics.put(theme, tuple);
    }
}
