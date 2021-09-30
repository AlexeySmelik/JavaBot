public class WordAndTranslate {
    private final String word;
    private final String translate;
    public WordAndTranslate(String word, String translate) {
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
