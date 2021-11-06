package JavaBot;

import JavaBot.resources.WordAndTranslate;

import java.util.List;

public interface Store {
    List<WordAndTranslate> get(String name);
    void add(String topic, List<WordAndTranslate> tuple);
    List<String> getTopics();
}
