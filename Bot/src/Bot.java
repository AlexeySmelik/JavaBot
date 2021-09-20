import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class Bot {
    private final Context context;
    private final QuestionHelper questionHelper;
    private ConversationHandler conversationHandler;

    public Bot() {
        context = new Context();
        questionHelper = new QuestionHelper();
        InitConvHandler();
    }

    public void startPolling() {
        sendMessage("Hello... my description...");
        var sc = new Scanner(System.in);
        while (true) {
            context.lastUserMessage = sc.nextLine();
            conversationHandler.doAction(context);
        }
    }

    public void sendMessage(String text) {
        System.out.println(text);
    }

    private void InitConvHandler() {
        conversationHandler = new ConversationHandler(getCommands(), getStatements(), 0);
    }

    private ArrayList<MessageHandler> getCommands() {
        var commands = new ArrayList<MessageHandler>();
        commands.add(new MessageHandler("\\help", Bot::helpMethod));
        commands.add(new MessageHandler("\\start", Bot::startMethod));
        commands.add(new MessageHandler("\\restart", Bot::restartMethod));
        return commands;
    }

    private HashMap<Integer, MessageHandler> getStatements() {
        var statements = new HashMap<Integer, MessageHandler>();
        var questions = questionHelper.getQuestions();
        for (var i = 1; i <= questionHelper.getNumberOfQuestions(); i++)
            statements.put(i, generateMessageHandler(questions.get(i - 1), i, questionHelper.getNumberOfQuestions()));
        return statements;
    }

    private static MessageHandler generateMessageHandler(QuestionForm form, Integer index, Integer max) {
        Function<Context, Integer> func = (Context context) -> {
            if (context.lastUserMessage.equals(form.answer)) {
                if (index + 1 > max)
                {
                    System.out.println("Конец)0))\nПропиши \\restart, чтобы начать заново!");
                    return ConversationHandler.EndState;
                }
                System.out.println("Next)0))");
                return index + 1;
            } else {
                System.out.println("Неправильный ответ)0))");
                return ConversationHandler.SaveState;
            }
        };

        Consumer<Context> cons = (Context context) ->
            System.out.println(form.question);

        return new MessageHandler(form.answer, func, cons);
    }

    private static Integer startMethod(Context context) {
        System.out.println("Go!");
        return 1;
    }

    private static Integer helpMethod(Context context) {
        System.out.println("Норм бот!");
        return ConversationHandler.SaveState;
    }

    private static Integer restartMethod(Context context) {
        System.out.println("Let's go!");
        return 1;
    }
}
