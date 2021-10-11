package JavaBot;

public class Tuple implements ITuple {
    private final String word;
    private final String translate;
    public Tuple(String word, String translate) {
        this.word = word;
        this.translate = translate;
    }

    public String getWord(){
        return word;
    }

    public String getTranslate(){
        return translate;
    }
}
