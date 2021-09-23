import handlers.Context;
import handlers.ConversationHandler;
import handlers.ConversationListener;
import handlers.MessageHandler;

import java.util.*;

public class Bot {
    private final Context context;
    private ConversationHandler convHandler;
    private static Integer maxQuestions = 5;
    private static ArrayList<QuestionForm> questions = new QuestionHelper().get();

    public Bot() {
        var data = new HashMap<String, String>();
        data.put("message", "hello");
        data.put("id", "1234");
        data.put("correctAnswers", "0");
        data.put("totalAnswers", "0");
        System.out.println();
        context = new Context(data);

        var states = new HashMap<Integer, List<MessageHandler>>();
        var firstStateList = new ArrayList<MessageHandler>();
        var secondStateList = new ArrayList<MessageHandler>();
        var thirdStateList = new ArrayList<MessageHandler>();
        thirdStateList.add(new MessageHandler("restart", Bot::start));
        states.put(3, thirdStateList);
        secondStateList.add(new MessageHandler("Да", Bot::check));
        secondStateList.add(new MessageHandler("Нет", Bot::check));
        states.put(2, secondStateList);
        firstStateList.clear();
        firstStateList.add(new MessageHandler(" ", Bot::start));
        firstStateList.add(new MessageHandler("", Bot::Do));
        states.put(1, firstStateList);

        try(var convHandler = new ConversationHandler(null, states, firstStateList.get(0), context)) {
            var listener = new ConversationListener(convHandler);
            context.manager.add("message", listener);
        }
    }

    private static Integer Do(Context context) {
        if(context.get("totalAnswers").equals("0"))
        {
            Collections.shuffle(questions);
        }
        System.out.println(questions.get(Integer.parseInt(context.get("totalAnswers"))).question);
        return 2;
    }
    private static Integer check(Context context) {
        if(questions.get(Integer.parseInt(context.get("totalAnswers"))).answer.equals(context.get("message")))
        {
            var updatedCorrectAnswers = Integer.parseInt(context.get("correctAnswers")) + 1;
            System.out.println("Правильно");
            context.set("correctAnswers", Integer.toString(updatedCorrectAnswers));
        }
        else
        {
            System.out.println("Ты ошибся(");
        }
        var updatedAnswers = Integer.parseInt(context.get("totalAnswers")) + 1;
        context.set("totalAnswers", Integer.toString(updatedAnswers));
        if(Integer.parseInt(context.get("totalAnswers")) >= maxQuestions)
        {
            System.out.println("Конец.. \nТвой результат:" +
                    context.get("correctAnswers") +
                    "/" +
                    context.get("totalAnswers") +
                    "\nНапиши restart чтобы начать заново");
            return 3;
        }
        System.out.println("\nНажми Enter, чтобы перейти к следующему вопросу");
        return 1;
    }

    private static Integer start(Context context) {
        System.out.println("Игра начинается! \nНажми Enter, чтобы увидеть вопрос");
        context.set("totalAnswers", "0");
        context.set("correctAnswers", "0");
        return 1;
    }

    public void startPolling() {
        var sc = new Scanner(System.in);
        while (true)
            context.set("message", sc.nextLine());
    }
}
