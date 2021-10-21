package JavaBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import JavaBot.db.Operator;
import JavaBot.resources.WordAndTranslate;

public class Adapter {

    public ArrayList<QuestionForm> GetUserQuestions(Integer maxQuestions, ArrayList<WordAndTranslate> showedWords, String userId, WordStore wordStore, Operator operatorDb, String topic) {
        var wordsToAsk = new ArrayList<WordAndTranslate>();
        for(var i = 0; i < showedWords.size() && wordsToAsk.size() < maxQuestions; i++) {
            wordsToAsk.add(new WordAndTranslate(showedWords.get(i).getWord(), showedWords.get(i).getTranslate()));
        }
        for(var i = 0; i < wordStore.get(topic).size() && wordsToAsk.size() < maxQuestions; i++){
            var word = new WordAndTranslate(wordStore.get(topic).get(i).getWord(), wordStore.get(topic).get(i).getTranslate());
            if(!operatorDb.getWords(userId).contains(word)){
                wordsToAsk.add(word);
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
