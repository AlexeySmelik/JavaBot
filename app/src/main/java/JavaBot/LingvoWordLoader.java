package JavaBot;

import JavaBot.resources.Minicard;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.Set;

public class LingvoWordLoader implements Loader {
    public enum Lang {
        RU("1049"),
        EN("1033");

        public final String idx;

        Lang(String idx) {
            this.idx = idx;
        }
    }

    private final String apiKey;
    private final Set<String> themes;
    private final OkHttpClient client;
    private String bearerToken;

    public LingvoWordLoader(String key) {
        apiKey = key;
        themes = Set.of("car", "sport"); //TODO
        client = new OkHttpClient();
        try {
            setBearerToken();
            getTranslation("home", Lang.EN.idx, Lang.RU.idx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBearerToken() throws IOException {
        var httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("developers.lingvolive.com")
                .addPathSegment("api")
                .addPathSegment("v1.1")
                .addPathSegment("authenticate")
                .build();
        var body = new FormBody.Builder().build();
        var headers = Headers.of("Authorization", "Basic " + apiKey);
        var request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .headers(headers)
                .build();

        var response = client.newCall(request).execute();
        bearerToken = response.body().string();
    }

    private String getTranslation(String text, String srcLang, String dstLang) throws IOException {
        var httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("developers.lingvolive.com")
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("Minicard")
                .addQueryParameter("text", text)
                .addQueryParameter("srcLang", srcLang)
                .addQueryParameter("dstLang", dstLang)
                .build();

        var headers = Headers.of("Authorization", "Bearer " + bearerToken);

        var request = new Request.Builder()
                .url(httpUrl)
                .get()
                .headers(headers)
                .build();

        var response = client.newCall(request).execute();

        var gson = new Gson();
        var entity = gson.fromJson(response.body().string(), Minicard.class);
        return entity.Translation.Translation;
    }

    @Override
    public void load(Store store) {

    }
}
