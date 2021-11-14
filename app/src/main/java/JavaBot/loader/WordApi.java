package JavaBot.loader;

import JavaBot.data_classes.Word;

import java.io.IOException;
import java.util.List;

public interface WordApi {
    List<Word> getWordsByPrefix(String prefix, String srcLang, String dstLang) throws IOException;
}
