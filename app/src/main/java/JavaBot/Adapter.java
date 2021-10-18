package JavaBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class Adapter {
    //private final DictionaryRepositoryByTopics dictByTopics;
    public ArrayList<String> getTopics(){
        var res = new ArrayList<String>();
        res.add("Car");
        res.add("Food");
        return res;
    }





    public ArrayList<QuestionForm> GetUserQuestions(String topic, HashMap<String, LearnedWords> learned, Integer maxQuestions, HashMap<String, ArrayList<WordAndTranslate>> dictByTopics) {
        topic = topic.toLowerCase(Locale.ROOT);
        topic = topic.substring(0, 1).toUpperCase() + topic.substring(1);
        var learnedWords = learned.get(topic);
        var wordsToAsk = new ArrayList<WordAndTranslate>();
        if(learnedWords == null || learnedWords.BadlyLearnedWords.size() == 0 && learnedWords.NormallyLearnedWords.size() == 0 && learnedWords.WellLearnedWords.size() == 0){
            return null;
        }
        if(learnedWords.BadlyLearnedWords != null){
            for(var i = 0; i < learnedWords.BadlyLearnedWords.size() && wordsToAsk.size() < maxQuestions; i++) {
                wordsToAsk.add(learnedWords.BadlyLearnedWords.get(i));
            }
        }

        if(learnedWords.NormallyLearnedWords != null){
            for(var i = 0; i < learnedWords.NormallyLearnedWords.size() && wordsToAsk.size() < maxQuestions; i++) {
                wordsToAsk.add(learnedWords.NormallyLearnedWords.get(i));
            }
        }

        var wordsByTopic = dictByTopics.get(topic);
        for(var i = 0; i < wordsByTopic.size() && wordsToAsk.size() < maxQuestions; i++) {
            if(!learnedWords.WellLearnedWords.contains(wordsByTopic.get(i)) &&
                    !learnedWords.NormallyLearnedWords.contains(wordsByTopic.get(i)) &&
                    !learnedWords.BadlyLearnedWords.contains(wordsByTopic.get(i))){
                learnedWords.BadlyLearnedWords.add(wordsByTopic.get(i));
                wordsToAsk.add(wordsByTopic.get(i));
            }
        }
        return MakeQuestions(wordsToAsk);
    }

    private ArrayList<QuestionForm> MakeQuestions(ArrayList<WordAndTranslate> words)
    {
        var maxQuestions = words.size();
        var result = new ArrayList<QuestionForm>();
        for(var i = 0; i < maxQuestions; i++) {
            var word = words.get(i);
            result.add(new QuestionForm(word.getTranslate(), word.getWord()));
        }
        return result;
    }
}
