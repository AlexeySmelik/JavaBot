package JavaBot;

import java.util.ArrayList;
import java.util.HashMap;

public interface IAdapter {
    IStore store = null;
    ArrayList<String> getTopics();
    ArrayList<QuestionForm> GetUserQuestions(String topic,
                                             HashMap<String, LearnedWords> learned,
                                             Integer maxQuestions);
}
