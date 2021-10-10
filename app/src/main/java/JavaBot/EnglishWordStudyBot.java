package JavaBot;

import JavaBot.resources.Config;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.google.gson.Gson;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EnglishWordStudyBot extends TelegramLongPollingBot {
    private Config config;

    public EnglishWordStudyBot() {
        super();
        var configURL = ClassLoader.getSystemClassLoader().getResource("config.json");
        var gson = new Gson();
        try {
            assert configURL != null;
            var file = new File(configURL.toURI());
            var lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            config = gson.fromJson(String.join("", lines), Config.class);
        } catch (Exception e) {
            config = null;
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.botName;
    }

    @Override
    public String getBotToken() {
        return config.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
