import java.io.IOException;
import java.util.ArrayList;

public class Adapter {
    private final DictionaryRepositoryByTopics dictByTopics;

    public Adapter() throws IOException {
        dictByTopics = new DictionaryRepositoryByTopics();
    }

    public ArrayList<String> getTopics(){
        return dictByTopics.getTopics();
    }

    /*public ArrayList<WordAndTranslate> getWordAndTranslateForTopic(String topic){
        return null;
    }*/

    /*public WordAndTranslate getRandomWordAndTranslateInThisTopic(){
        return new WordAndTranslate();
    }*/


}
