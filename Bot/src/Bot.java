/*
import handlers.*;

import java.util.*;

public class Bot {
    private final Context context;
    private ConversationHandler convHandler;
    private static Integer maxQuestions = 5;
    private static ArrayList<QuestionForm> questions = new QuestionHelper().get();
    private static ArrayList<String> themes = new ArrayList<String>();

    public Bot() {
        System.out.println();
        var data = new HashMap<String, Object>();
        data.put("correctAnswers", 0);
        context = new Context(1, data);

        var states = DialogMaker.MakeDialog(context);
        try(var convHandler = new ConversationHandler(null, states, 1)) {
            var listener = new ConversationListener(convHandler);
            context.manager.add("message", listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPolling() {
        var sc = new Scanner(System.in);
        while (true)
            context.changeMessage(sc.nextLine().toLowerCase(Locale.ROOT));
    }
}
*/
