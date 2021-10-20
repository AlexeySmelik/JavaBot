package JavaBot;

import JavaBot.resources.Context;
import JavaBot.handlers.ConversationHandler;
import JavaBot.handlers.ConversationListener;
import JavaBot.handlers.MessageHandler;
import JavaBot.handlers.State;

import java.io.IOException;
import java.util.*;

public class OldBot {
    public final Context context;
    private static EnglishWordStudyBot bot;

    public OldBot(EnglishWordStudyBot b) throws IOException {
        bot = b;
        var data = new HashMap<String, Object>();
        var adapter = new Adapter();
        var learnedWords = new HashMap<String, LearnedWords>();
        for(var i = 0; i < adapter.getTopics().size(); i++)
        {
            learnedWords.put(adapter.getTopics().get(i), new LearnedWords());
        }
        learnedWords.put("Car", new LearnedWords());
        learnedWords.put("Food", new LearnedWords());
        data.put("message", "");
        data.put("correctAnswers", 0);
        data.put("adapter", adapter);
        data.put("learnedWords", learnedWords);
        data.put("questions", new ArrayList<QuestionForm>());
        data.put("topic", "");
        data.put("attempts", 0);
        context = new Context(1, data);
        var commands = new ArrayList<MessageHandler>();
        commands.add(new MessageHandler("/help", OldBot::Help));
        var states = DialogMaker.MakeDialog(bot, context);
        try {
            var convHandler = new ConversationHandler(commands, states, 1);
            var listener = new ConversationListener(convHandler);
            context.addEventListener("message", listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Integer Help(Context context){
        bot.print("In main state you can write these messages: \nstatistic - print statistic of your learned words \ndictionary - print all your learned words \nlearn - show you new english words \nrevise - start a test to revise words which have been shown earlier");
        return 1;
    }
}

