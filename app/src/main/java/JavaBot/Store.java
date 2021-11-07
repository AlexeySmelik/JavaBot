package JavaBot;

import JavaBot.resources.Word;

import java.util.List;

public interface Store {
    List<Word> get(String name);
    void add(String topic, List<Word> words);
    List<String> getTopics();
}
