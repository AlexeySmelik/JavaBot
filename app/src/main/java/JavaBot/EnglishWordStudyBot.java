package JavaBot;

import JavaBot.db.Operator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnglishWordStudyBot extends TelegramLongPollingBot {
    public final Operator operatorDB;
    private final String botToken;
    private final String botName;
    public final WordStore wordStore;
    private OldBot old;

    public EnglishWordStudyBot(String botToken, String botName, Operator operator, WordStore store) {
        super();
        this.botToken = botToken;
        this.botName = botName;
        this.wordStore = store;
        this.operatorDB = operator;
        try {
            old = new OldBot(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void print(String text, String chatId) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        var markupInline = new ReplyKeyboardMarkup();
        var rows = new ArrayList<KeyboardRow>();
        var row = new KeyboardRow();
        var button = new KeyboardButton();
        button.setText("help");
        row.add(button);
        rows.add(row);
        markupInline.setKeyboard(rows);
        message.setReplyMarkup(markupInline);

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
            var chatId = update.getMessage().getChatId().toString();
            operatorDB.add(chatId);
            var message = update.getMessage().getText();
            try {
                old.tryAddNewUser(chatId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            old.execute(chatId, message);
        }
    }
}
