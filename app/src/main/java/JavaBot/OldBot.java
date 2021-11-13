package JavaBot;

import JavaBot.data_classes.Context;
import JavaBot.handlers.ConversationHandler;
import JavaBot.handlers.ConversationListener;
import JavaBot.handlers.MessageHandler;
import JavaBot.data_classes.Word;

import java.io.IOException;
import java.util.*;

public class OldBot {
    private final EnglishWordStudyBot bot;
    private final Map<String, Context> users;

    public OldBot(EnglishWordStudyBot bot) {
        this.bot = bot;
        users = new HashMap<>();
    }

    public void execute(String chatId, String message) {
        var user = users.get(chatId);
        user.update("message", message);
    }

    public Boolean tryAddNewUser(String chatId) throws IOException {
        if (users.containsKey(chatId))
            return false;
        var context = getDefaultContext(chatId);
        users.put(chatId, context);

        var commands = new ArrayList<MessageHandler>();
        commands.add(new MessageHandler("/help", DialogMaker::help));
        commands.add(new MessageHandler("help", DialogMaker::help));
        commands.add(new MessageHandler("/start", DialogMaker::help));
        commands.add(new MessageHandler("back", DialogMaker::back));
        try {
            var states = DialogMaker.makeDialog(bot);
            var convHandler = new ConversationHandler(commands, states, 1);
            var listener = new ConversationListener(convHandler);
            context.addEventListener("message", listener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private Context getDefaultContext(String chatId) {
        var data = new HashMap<String, Object>();
        var adapter = new Adapter();
        data.put("chatId", chatId);
        data.put("message", "");
        data.put("correctAnswers", 0);
        data.put("adapter", adapter);
        data.put("questions", new ArrayList<QuestionForm>());
        data.put("topic", "");
        data.put("attempts", 0);
        data.put("showedWords", new ArrayList<Word>());
        return new Context(data);
    }
}

