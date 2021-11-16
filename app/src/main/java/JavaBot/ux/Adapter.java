package JavaBot.ux;

import java.util.ArrayList;

import JavaBot.data_classes.Word;

public class Adapter {
    public ArrayList<QuestionForm> getUserQuestions(Integer maxQuestions, ArrayList<Word> showedWords) {
        var wordsToAsk = new ArrayList<Word>();
        for(var i = 0; i < showedWords.size() && wordsToAsk.size() < maxQuestions; i++) {
            wordsToAsk.add(showedWords.get(i));
        }
        return makeQuestions(wordsToAsk);
    }

    private ArrayList<QuestionForm> makeQuestions(ArrayList<Word> words)
    {
        var result = new ArrayList<QuestionForm>();
        for (Word word : words) {
            result.add(new QuestionForm(word.getTranslation(), word.getHeading()));
        }
        return result;
    }
}
