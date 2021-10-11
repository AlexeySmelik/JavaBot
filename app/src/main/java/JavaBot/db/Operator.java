package JavaBot.db;

import JavaBot.resources.WordAndTranslate;

import java.util.List;

public interface Operator {
    void add(String user_id);
    List<WordAndTranslate> getWords(String user_id);
    void updateWords(String user_id, List<WordAndTranslate> newWords);
}
