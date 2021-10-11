package JavaBot.db;

import JavaBot.resources.WordAndTranslate;

public class EnglishConverter {
    public static String ToString(WordAndTranslate couple) {
        var res = new StringBuilder();
        res.append(couple.getWord());
        res.append(' ');
        res.append(couple.getTranslate());
        return res.toString();
    }

    public static WordAndTranslate ToCouple(String str) {
        var res = str.split(" ");
        return new WordAndTranslate(res[0], res[1]);
    }
}
