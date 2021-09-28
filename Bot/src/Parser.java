import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private final ArrayList<String> linksArray;
    private final ArrayList<String> info;
    private final Map<String, ArrayList<ArrayList<String>>> themeAndWords; // отрефакторить, сделать класс

    public Parser() throws IOException {
        linksArray = new ArrayList<>();
        info = new ArrayList<>();
        themeAndWords = new HashMap<>();
        getInfoAndLinks();
        getDictionaryWithThemes();
        System.out.println(themeAndWords);
    }

    private void getInfoAndLinks() throws IOException {
        getThemeAndLinks();
    }

    private void getThemeAndLinks() throws IOException {
        String url = "https://study-english.info/vocabularies.php";
        Document page = Jsoup.parse(new URL(url), 3000);
        Elements titles = page.select("a");
        for (Element title : titles){
            String text = title.text();
            if (text.contains("Лексика по теме ") & !text.contains("Археология") & !text.contains("Архитектура")){
                System.out.println(text);
                var theme = text.replace("Лексика по теме ", "");
                theme.substring(1, theme.length()-1);
                info.add(theme);
                var href = "https:" + title.attr("href");
                linksArray.add(href);
            }
        }
    }

    private void getDictionaryWithThemes() throws IOException {
        for (int i = 0; i < linksArray.size(); i++ ){
            var dictionaryForThisTheme = getWordsForTheme(linksArray.get(i));
            System.out.println(dictionaryForThisTheme);
            themeAndWords.put(info.get(i), dictionaryForThisTheme);
        }
    }

    private ArrayList<ArrayList<String>> getWordsForTheme(String link) throws IOException {
        String url = link;
        Document page = Jsoup.parse(new URL(url), 3000);
        Elements titles = page.select("tr");
        var arrayOfWordAndTranslate = new ArrayList<ArrayList<String>>();
        for (Element title : titles){
            String text = title.text();
            if (text.contains("[") & !text.contains(",") & ( text.length() == text.indexOf("]") - 1 )){
                var firstSpecialSymbol = text.indexOf("[");
                var secondSpecialSymbol = text.indexOf("]");
                var englishWord = text.substring(0, firstSpecialSymbol -2);
                var russianWord = text.substring(secondSpecialSymbol + 2);
                var wordAndTranslate = new ArrayList<String>();
                wordAndTranslate.add(englishWord);
                wordAndTranslate.add(russianWord);
                arrayOfWordAndTranslate.add(wordAndTranslate);
            }
        }
        return arrayOfWordAndTranslate;
    }

}
