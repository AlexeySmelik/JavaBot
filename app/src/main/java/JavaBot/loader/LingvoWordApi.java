package JavaBot.loader;

import JavaBot.data_classes.Word;
import JavaBot.deserialization.WordList;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LingvoWordApi implements WordApi {
    private final String apiKey;
    private final OkHttpClient client;
    private String bearerToken;

    public LingvoWordApi(String key) throws IOException {
        apiKey = key;
        client = new OkHttpClient();
        setBearerToken();
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
        bearerToken = Objects.requireNonNull(response.body()).string();
    }

    @Override
    public List<Word> getWordsByPrefix(
            String prefix,
            String srcLang,
            String dstLang
    ) throws IOException {
        var httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("developers.lingvolive.com")
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("WordList")
                .addQueryParameter("prefix", prefix)
                .addQueryParameter("srcLang", srcLang)
                .addQueryParameter("dstLang", dstLang)
                .addQueryParameter("pageSize", "20")
                .build();

        var headers = Headers.of("Authorization", "Bearer " + bearerToken);

        var request = new Request.Builder()
                .url(httpUrl)
                .get()
                .headers(headers)
                .build();

        var response = client.newCall(request).execute();

        var gson = new Gson();
        var entity = gson.fromJson(response.body().string(), WordList.class);
        var headings = entity.Headings;
        var out = new ArrayList<Word>();
        for (var heading : headings) {
            var wordAndTranslate = new Word(heading.Heading, heading.Translation);
            out.add(wordAndTranslate);
        }
        return out;
    }
}
