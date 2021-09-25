import handlers.*;

import java.util.*;

public class Bot {
    private final Context context;
    private ConversationHandler convHandler;
    private static Integer maxQuestions = 5;
    private static ArrayList<QuestionForm> questions = new QuestionHelper().get();
    private static ArrayList<String> themes = new ArrayList<String>();

    public Bot() {
        themes.add("еда");
        themes.add("природа");
        var maxQuestions = 5;
        var data = new HashMap<String, Object>();
        data.put("message", "hello");
        data.put("id", "1234");
        data.put("correctAnswers", 0);
        System.out.println();
        context = new Context(1, data);

        var states = new HashMap<Integer, State>();
        var firstStateList = new ArrayList<MessageHandler>();
        firstStateList.add(new MessageHandler("статистика", Bot::printStatistic));
        firstStateList.add(new MessageHandler("словарь", Bot::printLearnedWords));
        firstStateList.add(new MessageHandler("повторить", Bot::startTest));
        firstStateList.add(new MessageHandler("выучить", Bot::learnWords));
        var secondStateList = new ArrayList<MessageHandler>();
        secondStateList.add(new MessageHandler("назад", Bot::back));
        var thirdStateList = new ArrayList<MessageHandler>();
        thirdStateList.add(new MessageHandler("назад", Bot::back));
        var fourthStateList = new ArrayList<MessageHandler>();
        fourthStateList.add(new MessageHandler("", Bot::askWord));
        var seventhStateList = new ArrayList<MessageHandler>();
        seventhStateList.add(new MessageHandler("заново", Bot::startTest));
        seventhStateList.add(new MessageHandler("назад", Bot::back));
        var ninthStateList = new ArrayList<MessageHandler>();
        ninthStateList.add(new MessageHandler("заново", Bot::learnWords));
        ninthStateList.add(new MessageHandler("тест", Bot::startTest));
        ninthStateList.add(new MessageHandler("назад", Bot::back));
        states.put(1, new State(firstStateList, null));
        states.put(2, new State(secondStateList, null));
        states.put(3, new State(thirdStateList, null));
        states.put(4, new State(fourthStateList, null));
        states.put(5, new State(new ArrayList<MessageHandler>(), Bot::checkWord));
        states.put(6, new State(new ArrayList<MessageHandler>(), Bot::checkWord));
        states.put(7, new State(seventhStateList, null));
        states.put(8, new State(new ArrayList<MessageHandler>(), Bot::printWordsToLearn));
        states.put(9, new State(ninthStateList, null));
        try(var convHandler = new ConversationHandler(null, states, 1)) {
            var listener = new ConversationListener(convHandler);
            context.manager.add("message", listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Integer printStatistic(Context context) {
        System.out.println("Тут печатается статистика");
        return 2;
    }

    private static Integer startTest(Context context) {
        context.set("correctAnswers", 0);
        System.out.println("Начинаем тест по словам \nНажми Enter чтобы начать");
        return 4;
    }

    private static Integer printLearnedWords(Context context) {
        System.out.println("Выводятся выученные слова");
        return 3;
    }


    private static Integer askWord(Context context) {
        System.out.println("Напиши перевод слова apple");
        return 5;
    }

    private static Integer checkWord(Context context) {
        if((int)context.get("correctAnswers") == maxQuestions - 1)
        {
            if(context.getMessage().equals("яблоко"))
            {
                System.out.println("Правильно");
                System.out.println("Закончено");
                return 7;
            }

            System.out.println("*подсказка*");
            return 5;
        }
        if(context.getMessage().equals("яблоко"))
        {
            context.set("correctAnswers", (int)context.get("correctAnswers") + 1);
            System.out.println("Правильно");
            System.out.println("Нажми Enter, чтобы увидеть следующее слово");
            return 4;
        }
        else
        {
            System.out.println("Неправильно");
            System.out.println("*подсказка*");
        }
        return 5;
    }



    private static Integer back(Context context) {
        return 1;
    }

    private static Integer learnWords(Context context) {
        System.out.println("Напиши интересующую тематику");
        return 8;
    }

    private static Integer printWordsToLearn(Context context) {
        if(!themes.contains(context.getMessage()))
        {
            System.out.println("Нет такой темы");
            return 8;
        }
        System.out.println("Вывожу слова по теме " + context.getMessage());
        return 9;
    }




    public void startPolling() {
        var sc = new Scanner(System.in);
        while (true)
            context.changeMessage(sc.nextLine().toLowerCase(Locale.ROOT));
    }
}
