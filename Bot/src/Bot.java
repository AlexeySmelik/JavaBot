import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class Bot {
    public Context context;
    public ConversationHandler conversationHandler;
    public QuestionHelper questionHelper;

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
        var commands = new ArrayList<MessageHandler>();
        commands.add(new MessageHandler("\\help", Bot::helpMethod));
        commands.add(new MessageHandler("\\start", Bot::startMethod));

        var statements = new HashMap<Integer, MessageHandler>();
        for (var i = 1; i <= questionHelper.getNumberOfQuestions(); i++) {
            var form = questionHelper.getRandomQuestion();
            statements.put(
                    i,
                    generateMessageHandler(
                            form,
                            i == questionHelper.getNumberOfQuestions() ?
                                    ConversationHandler.EndState :
                                    i
                    )
            );
        }

        conversationHandler = new ConversationHandler(commands, statements, 1);
    }

    private static MessageHandler generateMessageHandler(QuestionForm form, Integer index) {
        Function<Context, Integer> func = (Context context) -> {
            if (context.lastUserMessage.equals(form.answer)) {
                System.out.println("Некст");
                return index + 1;
            } else {
                System.out.println("Неправильный ответ)0))");
                return ConversationHandler.SaveState;
            }
        };

        Consumer<Context> cons = (Context context) -> {
            System.out.println(form.question);
        };

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
}
