package JavaBot.resources;

import java.io.IOException;
import java.util.Set;

public class LingvoLoader implements Loader{
    private final LingvoWordApi wordApi;
    private final Set<String> themes;


    public LingvoLoader(String key) throws IOException{
        wordApi = new LingvoWordApi(key);
        themes = Set.of("car", "planet", "my", "weather", "home", "math");
    }

    @Override
    public void load(Store store){
        for (var theme : themes)
            try {
                var words = wordApi.getWordsByPrefix(theme,
                        LingvoWordLoader.Lang.EN.idx, LingvoWordLoader.Lang.RU.idx);
                store.add(theme, words);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
