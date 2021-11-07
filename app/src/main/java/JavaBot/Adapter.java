package JavaBot;

import java.util.ArrayList;
import JavaBot.db.Operator;
import JavaBot.resources.Word;

public class Adapter {
    public ArrayList<QuestionForm> getUserQuestions(
            Integer maxQuestions,
            ArrayList<Word> showedWords,
            String userId, WordStore wordStore,
            Operator operatorDb,
            String topic
    ) {
        var wordsToAsk = new ArrayList<Word>();
        for(var i = 0; i < showedWords.size() && wordsToAsk.size() < maxQuestions; i++) {
            wordsToAsk.add(showedWords.get(i));
        }
        for(var i = 0; i < wordStore.get(topic).size() && wordsToAsk.size() < maxQuestions; i++){
            var word = wordStore.get(topic).get(i);
            if(!operatorDb.getWords(userId).contains(word)){
                wordsToAsk.add(word);
            }
        }
        return makeQuestions(wordsToAsk);
    }

    private ArrayList<QuestionForm> makeQuestions(ArrayList<Word> words)
    {
        var maxQuestions = words.size();
        var result = new ArrayList<QuestionForm>();
        for (Word word : words) {
            result.add(new QuestionForm(word.getTranslation(), word.getHeading()));
        }
        return result;
    }
}
