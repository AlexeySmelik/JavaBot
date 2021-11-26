package JavaBot;

import JavaBot.db.MongoDBOperator;
import JavaBot.db.UserRepository;
import JavaBot.deserialization.Config;
import JavaBot.data_classes.WordStore;
import JavaBot.loader.LingvoLoader;
import JavaBot.api.LingvoWordApi;
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
            var database = MongoDBOperator.InitializeDatabase(Config.uriMongoDB, Config.dbName);
            var repository = new UserRepository(database);

            var store = new WordStore();
            var wordApi = new LingvoWordApi(Config.extKey);
            var loader = new LingvoLoader(wordApi);
            loader.load(store);

            var botsApi = new TelegramBotsApi(DefaultBotSession.class);
            var bot = new EnglishWordStudyBot(
                    Config.token,
                    Config.botName,
                    store,
                    repository
            );
            botsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
