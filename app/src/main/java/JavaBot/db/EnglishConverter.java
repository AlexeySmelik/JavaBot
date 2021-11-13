package JavaBot.db;

import JavaBot.data_classes.Word;

public class EnglishConverter {
    public static String ToString(Word couple) {
        return couple.getHeading() +
                ':' +
                couple.getTranslation();
    }

    public static Word ToCouple(String str) {
        var res = str.split(":");
        return new Word(res[0], res[1]);
    }
}
