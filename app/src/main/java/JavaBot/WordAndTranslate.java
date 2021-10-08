package JavaBot;

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

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        var word = (WordAndTranslate) obj;
        return this.word.equals(word.word) && this.translate.equals(word.translate);
    }
}
