import handlers.Context;
import handlers.ConversationHandler;
import handlers.ConversationListener;
import handlers.MessageHandler;

import java.util.*;

public class Bot {
    private final Context context;
    private ConversationHandler convHandler;
    private static Integer maxQuestions = 5;
    private static ArrayList<QuestionForm> questions = new QuestionHelper().getQuestions();

    public Bot() {
        var data = new HashMap<String, String>();
        data.put("message", "hello");
        data.put("id", "1234");
        context = new Context(data);

        var states = new HashMap<Integer, List<MessageHandler>>();
        var list = new ArrayList<MessageHandler>();
        var list2 = new ArrayList<MessageHandler>();
        var list3 = new ArrayList<MessageHandler>();
        list3.add(new MessageHandler("restart", Bot::start));
        states.put(3, list3);
        list2.add(new MessageHandler("Да", Bot::check));
        list2.add(new MessageHandler("Нет", Bot::check));
        states.put(2, list2);
        list.clear();
        list.add(new MessageHandler(" ", Bot::start));
        list.add(new MessageHandler("", Bot::Do));

        states.put(1, list);


        try(var convHandler = new ConversationHandler(null, states, list.get(0), context)) {
            var listener = new ConversationListener(convHandler);
            context.manager.add("message", listener);
        }
    }

    private static Integer Do(Context context) {
        if(context.AnsweredQuestions == 0)
        {
            Collections.shuffle(questions);
        }
        System.out.println(questions.get(context.AnsweredQuestions).question);
        return 2;
    }
    private static Integer check(Context context) {
        if(questions.get(context.AnsweredQuestions).answer.equals(context.get("message").toLowerCase(Locale.ROOT)))
        {
            System.out.println("Правильно");
            context.CorrectAnswers++;
        }
        else
        {
            System.out.println("Ты ошибся(");
        }
        context.AnsweredQuestions++;
        if(context.AnsweredQuestions >= maxQuestions)
        {
            System.out.println("Конец..");
            System.out.println("Твой результат:"+context.CorrectAnswers.toString() + "/" + context.AnsweredQuestions.toString());
            System.out.println("Нажми SPACE чтобы начать заново");
            return 3;
        }
        System.out.println("\nНажми Enter, чтобы перейти к следующему вопросу");
        return 1;
    }


    private static Integer start(Context context) {
        System.out.println("Игра начинается!");
        System.out.println("Нажми Enter, чтобы увидеть вопрос");
        context.AnsweredQuestions = 0;
        context.CorrectAnswers = 0;
        return 1;
    }

    private static Integer restart(Context context) {
        System.out.println("Точно?");
        return 1;
    }

    public void startPolling() {
        var sc = new Scanner(System.in);
        while (true)
            context.set("message", sc.nextLine());
    }
}
