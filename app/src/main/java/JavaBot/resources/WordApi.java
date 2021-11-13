package JavaBot.resources;

import java.io.IOException;
import java.util.List;

public interface WordApi {
    List<Word> getWordsByPrefix(String prefix,
                                String srcLang,
                                String dstLang) throws IOException;
}
