package JavaBot.db;

import JavaBot.resources.WordAndTranslate;

public class EnglishConverter {
    public static String ToString(WordAndTranslate couple) {
        return couple.getWord() +
                ':' +
                couple.getTranslate();
    }

    public static WordAndTranslate ToCouple(String str) {
        var res = str.split(":");
        return new WordAndTranslate(res[0], res[1]);
    }
}
