import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DictionaryRepositoryByTopics {
    public final Map<String, ArrayList<WordAndTranslate>> DictionaryByTopics;

    public DictionaryRepositoryByTopics() throws IOException {
        HTMLParser parser = new HTMLParser();
        DictionaryByTopics = parser.parse();
    }

    public ArrayList<String> getTopics(){
        ArrayList<String> topics = new ArrayList<>();
        for (String topic : DictionaryByTopics.keySet()){
            topics.add(topic);
        }
        return topics;
    }
}
