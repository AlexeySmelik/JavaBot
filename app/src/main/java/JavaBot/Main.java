package JavaBot;

import JavaBot.db.MongoDBOperator;
import JavaBot.db.MongoDBRepository;
import JavaBot.deserialization.Config;
import JavaBot.data_classes.WordStore;
import JavaBot.loader.LingvoLoader;
import com.google.gson.Gson;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Main {
    public static void main(String [] args) {
        try {
            var config = getConfig();

            var database = MongoDBOperator.InitializeDatabase(config.uriMongoDB, config.dbName);
            var repository = new MongoDBRepository(database);

            var store = new WordStore();
            var loader = new LingvoLoader(config.extKey);
            loader.load(store);

            var botsApi = new TelegramBotsApi(DefaultBotSession.class);
            var bot = new EnglishWordStudyBot(
                    config.token,
                    config.botName,
                    store,
                    repository
            );
            botsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Config getConfig() throws IOException, URISyntaxException {
        var configURL = ClassLoader.getSystemClassLoader().getResource("config.json");
        assert configURL != null;
        var file = new File(configURL.toURI());
        var lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        var gson = new Gson();
        return gson.fromJson(String.join("", lines), Config.class);
    }
}
