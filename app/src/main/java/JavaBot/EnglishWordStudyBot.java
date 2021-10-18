package JavaBot;

import JavaBot.resources.Config;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EnglishWordStudyBot extends TelegramLongPollingBot {
    private Config config;
    private OldBot old;
    private String ChatId;
    public EnglishWordStudyBot() {
        super();
        var configURL = ClassLoader.getSystemClassLoader().getResource("config.json");
        var gson = new Gson();
        try {
            assert configURL != null;
            var file = new File(configURL.toURI());
            var lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            config = gson.fromJson(String.join("", lines), Config.class);
            old = new OldBot(this);

        } catch (Exception e) {
            config = null;
            e.printStackTrace();
        }
    }
    public void print(String text) {
        var message = new SendMessage();
        message.setChatId(ChatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
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
            ChatId = update.getMessage().getChatId().toString();
            old.context.updateMessage(update.getMessage().getText());
        }
    }
}
