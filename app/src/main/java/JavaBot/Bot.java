package JavaBot;

import java.io.IOException;
import java.util.*;

public class Bot {
    private final Context context;
    private ConversationHandler convHandler;
    private static Integer maxQuestions = 5;
    private static ArrayList<String> themes = new ArrayList<String>();

    public Bot() throws IOException {
        System.out.println();
        var data = new HashMap<String, Object>();
        var adapter = new Adapter();
        var learnedWords = new HashMap<String, LearnedWords>();
        for(var i = 0; i < adapter.getTopics().size(); i++)
        {
            learnedWords.put(adapter.getTopics().get(i), new LearnedWords());
        }
        data.put("correctAnswers", 0);
        data.put("adapter", adapter);
        data.put("learnedWords", learnedWords);
        data.put("questions", new ArrayList<QuestionForm>());
        data.put("topic", "");
        data.put("attempts", 0);
        context = new Context(1, data);


        var states = DialogMaker.MakeDialog(context);
        try(var convHandler = new ConversationHandler(null, states, 1)) {
            var listener = new ConversationListener(convHandler);
            context.manager.add("updateMessage", listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPolling() {
        var sc = new Scanner(System.in);
        while (true)
            context.updateMessage(sc.nextLine().toLowerCase(Locale.ROOT));
    }
}

