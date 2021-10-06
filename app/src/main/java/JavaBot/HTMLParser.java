import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public  class HTMLParser {
    private final ArrayList<String> linksArray;
    private final ArrayList<String> themesArray;
    private final Map<String, ArrayList<WordAndTranslate>> themeAndWords;
    private final HashSet<String> themesHashSet;

    public HTMLParser() throws IOException {
        linksArray = new ArrayList<>();
        themesArray = new ArrayList<>();
        themeAndWords = new HashMap<>();
        themesHashSet = new HashSet<>();
    }

    public Map<String, ArrayList<WordAndTranslate>> parse() throws IOException {
        fillThemesHashSet();
        getThemeAndLinks();
        getDictionaryWithThemes();
        return themeAndWords;
    }

    private void fillThemesHashSet(){
        themesHashSet.add("Автомобиль");
        themesHashSet.add("Музыка");
    }

    private void getThemeAndLinks() throws IOException {
        String url = "https://study-english.info/vocabularies.php";
        Document page = Jsoup.parse(new URL(url), 3000);
        Elements titles = page.select("a");
        for (Element title : titles){
            String text = title.text();
            if (text.contains("Лексика по теме ")){
                String theme = text.replace("Лексика по теме ", "");
                theme = theme.substring(1, theme.length()-1);
                if (themesHashSet.contains(theme)) {
                    themesArray.add(theme);
                    String href = "https:" + title.attr("href");
                    linksArray.add(href);
                }
            }
        }
    }

    private void getDictionaryWithThemes() throws IOException {
        for (int i = 0; i < linksArray.size(); i++ ){
            String link = linksArray.get(i);
            String theme = themesArray.get(i);
            ArrayList<WordAndTranslate> dictionaryForThisTheme = getDictionaryForTheme(link);
            themeAndWords.put(theme, dictionaryForThisTheme);
        }
    }

    private ArrayList<WordAndTranslate> getDictionaryForTheme(String link) throws IOException {
        Document page = Jsoup.parse(new URL(link), 3000);
        Elements titles = page.select("tr");
        var arrayOfWordAndTranslate = new ArrayList<WordAndTranslate>();
        for (Element title : titles){
            String text = title.text();
            if (text.contains("[") & !text.contains(",")){
                int firstSpecialSymbol = text.indexOf("[");
                int secondSpecialSymbol = text.indexOf("]");
                String englishWord = text.substring(0, firstSpecialSymbol -2);
                String russianWord = text.substring(secondSpecialSymbol + 2);
                WordAndTranslate wordAndTranslate = new WordAndTranslate(englishWord, russianWord);
                arrayOfWordAndTranslate.add(wordAndTranslate);
            }
        }
        return arrayOfWordAndTranslate;
    }

}