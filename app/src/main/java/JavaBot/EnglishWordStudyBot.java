package JavaBot;

import JavaBot.db.Operator;
import JavaBot.resources.WordStore;
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

    public void print(String text, String chatId, Integer state) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        var markupInline = new ReplyKeyboardMarkup();
        var rows = new ArrayList<KeyboardRow>();
        var row = new KeyboardRow();
        for (var e : GetButtons(state))
        {
            row.add(e);
        }
        rows.add(row);
        markupInline.setKeyboard(rows);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<KeyboardButton> GetButtons(Integer state)
    {
        var result = new ArrayList<KeyboardButton>();
        switch (state) {
            case 1 -> {
                var button = new KeyboardButton();
                button.setText("dictionary");
                var button2 = new KeyboardButton();
                button2.setText("learn");
                var button3 = new KeyboardButton();
                button3.setText("revise");
                var button4 = new KeyboardButton();
                button4.setText("statistic");
                result.add(button);
                result.add(button2);
                result.add(button3);
                result.add(button4);
                break;
            }
            case 2, 3, 5 -> {
                var button = new KeyboardButton();
                button.setText("back");
                result.add(button);
                break;
            }
            case 6 -> {
                var button = new KeyboardButton();
                button.setText("again");
                var button2 = new KeyboardButton();
                button2.setText("back");
                result.add(button);
                result.add(button2);
                break;
            }
            case 8 -> {
                var button = new KeyboardButton();
                button.setText("test");
                var button2 = new KeyboardButton();
                button2.setText("back");
                var button3 = new KeyboardButton();
                button3.setText("again");
                result.add(button);
                result.add(button2);
                result.add(button3);

                break;
            }
        }
        return result;
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
