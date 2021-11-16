package JavaBot.loader;

import JavaBot.api.WordApi;
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

    private final WordApi wordApi;
    private final Set<String> themes;

    public LingvoLoader(WordApi wordApi) {
        this.wordApi = wordApi;
        themes = Set.of("planet", "my", "weather", "home", "math");
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
