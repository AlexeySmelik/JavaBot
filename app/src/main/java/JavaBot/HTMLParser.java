package JavaBot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public  class HTMLParser implements IParser{
    private final HashSet<String> themesType;

    public HTMLParser() {
        themesType = new HashSet<>();
    }

    public void parse(IStore store) throws IOException {
        fillThemesHashSet();
        getThemeAndLinks(store);
    }

    private void fillThemesHashSet(){
        themesType.add("Автомобиль");
        themesType.add("Музыка");
        themesType.add("Внешность");
    }

    private void getThemeAndLinks(IStore store) throws IOException {
        String url = "https://study-english.info/vocabularies.php";
        Document page = Jsoup.parse(new URL(url), 3000);
        Elements titles = page.select("a");
        for (Element title : titles){
            String text = title.text();
            if (text.contains("Лексика по теме ")){
                String theme = text.replace("Лексика по теме ", "");
                theme = theme.substring(1, theme.length()-1);
                if (themesType.contains(theme)) {
                    String href = "https:" + title.attr("href");
                    store.addT(theme, getDictionaryForTheme(href));
                }
            }
        }
    }

    private ArrayList<ITuple> getDictionaryForTheme(String link) throws IOException {
        Document page = Jsoup.parse(new URL(link), 3000);
        Elements titles = page.select("tr");
        var arrayOfWordAndTranslate = new ArrayList<ITuple>();
        for (Element title : titles){
            String text = title.text();
            if (text.contains("[") & !text.contains(",")
                    & !text.contains("/") & !text.contains("(")){
                int firstSpecialSymbol = text.indexOf("[");
                int secondSpecialSymbol = text.indexOf("]");
                String englishWord = text.substring(0, firstSpecialSymbol - 1);
                String russianWord = text.substring(secondSpecialSymbol + 2);
                Tuple wordAndTranslate = new Tuple(englishWord, russianWord);
                arrayOfWordAndTranslate.add(wordAndTranslate);
            }
        }
        return arrayOfWordAndTranslate;
    }


}