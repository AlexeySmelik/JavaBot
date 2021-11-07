package JavaBot.db;

import JavaBot.resources.Word;

import java.util.List;

public interface Operator {
    void add(String user_id);
    List<Word> getWords(String user_id);
    void updateWords(String user_id, List<Word> newWords);
}
