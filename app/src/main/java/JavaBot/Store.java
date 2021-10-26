package JavaBot;

import JavaBot.resources.WordAndTranslate;

import java.util.ArrayList;

public interface Store {
    ArrayList<WordAndTranslate> get(String name);
    void addT(String topic, ArrayList<WordAndTranslate> tuple);
    ArrayList<String> getTopicsName();
}
