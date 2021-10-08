package JavaBot;
import java.util.ArrayList;

public class LearnedWords {
    public ArrayList<WordAndTranslate> WellLearnedWords;
    public ArrayList<WordAndTranslate> NormallyLearnedWords;
    public ArrayList<WordAndTranslate> BadlyLearnedWords;

    public LearnedWords(){
        WellLearnedWords = new ArrayList<WordAndTranslate>();
        NormallyLearnedWords = new ArrayList<WordAndTranslate>();
        BadlyLearnedWords = new ArrayList<WordAndTranslate>();
    }
}
