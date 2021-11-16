package JavaBot;

import JavaBot.data_classes.DialogState;
import JavaBot.db.User;
import JavaBot.db.IUserRepository;
import JavaBot.data_classes.Store;
import JavaBot.ux.ButtonsMaker;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.util.ArrayList;

public class EnglishWordStudyBot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botName;
    public final Store wordStore;
    public final IUserRepository userStore;
    private OldBot old;

    public EnglishWordStudyBot(
            String botToken,
            String botName,
            Store store,
            IUserRepository repository
    ) {
        super();
        this.botToken = botToken;
        this.botName = botName;
        this.wordStore = store;
        this.userStore = repository;
        try {
            old = new OldBot(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print(String text, String chatId, DialogState state) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        var markupInline = new ReplyKeyboardMarkup();
        var rows = new ArrayList<KeyboardRow>();
        var row = new KeyboardRow();
        for (var e : ButtonsMaker.GetButtons(state))
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
            userStore.save(new User(chatId, null), false);
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
