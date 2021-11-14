package JavaBot.loader;

import JavaBot.data_classes.Store;

import java.io.IOException;
import java.util.Set;

public class LingvoLoader implements Loader {
    public enum Lang {
        RU("1049"),
        EN("1033");

        public final String idx;

        Lang(String idx) {
            this.idx = idx;
        }
    }

    private final LingvoWordApi wordApi;
    private final Set<String> themes;


    public LingvoLoader(String key) throws IOException {
        wordApi = new LingvoWordApi(key);
        themes = Set.of("car", "planet", "my", "weather", "home", "math");
    }

    @Override
    public void load(Store store){
        for (var theme : themes)
            try {
                var words = wordApi.getWordsByPrefix(theme,
                        Lang.EN.idx, Lang.RU.idx);
                store.add(theme, words);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
