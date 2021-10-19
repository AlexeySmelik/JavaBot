package JavaBot;

import JavaBot.db.Operator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.google.gson.Gson;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EnglishWordStudyBot extends TelegramLongPollingBot {
    private final Operator operator;
    private final String botToken;
    private final String botName;
    private final IStore wordStore;
    private OldBot old;
    private String ChatId;
    public EnglishWordStudyBot(String botToken, String botName, Operator operator, IStore store) {
        super();
        this.botToken = botToken;
        this.botName = botName;
        this.wordStore = store;
        this.operator = operator;
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
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
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
