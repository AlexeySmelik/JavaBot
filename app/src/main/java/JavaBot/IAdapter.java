package JavaBot;

import JavaBot.db.Operator;

import java.util.ArrayList;
import java.util.HashMap;

public interface IAdapter {
    IStore store = null;
    ArrayList<String> getTopics();
    ArrayList<QuestionForm> GetUserQuestions(String topic, Operator learned, Integer maxQuestions, WordStore dictByTopics, String userId);
}
