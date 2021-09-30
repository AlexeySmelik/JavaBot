/*
import handlers.Context;
import handlers.MessageHandler;
import handlers.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DialogMaker {
    private static final ArrayList<String> themes = new ArrayList<>();

    public static HashMap<Integer, State> MakeDialog(Context context) {
        themes.add("еда");
        themes.add("природа");
        var states = new HashMap<Integer, State>();
        states.put(1,
                MakeState(
                        new HashMap<>(Map.of(
                                "статистика", DialogMaker::printStatistic,
                                "словарь", DialogMaker::printLearnedWords,
                                "повторить", DialogMaker::startTest,
                                "выучить", DialogMaker::learnWords)),
                        null));
        states.put(2,
                MakeState(new HashMap<>(Map.of("назад", DialogMaker::back)),
                        null));
        states.put(3,
                MakeState(new HashMap<>(Map.of("назад", DialogMaker::back)),
                        null));
        states.put(4,
                MakeState(new HashMap<>(), DialogMaker::checkWord));
        states.put(5,
                MakeState(
                        new HashMap<>(Map.of(
                                "заново", DialogMaker::startTest,
                                "назад", DialogMaker::back)),
                        null));
        states.put(6,
                MakeState(new HashMap<>(), DialogMaker::printWordsToLearn));
        states.put(7,
                MakeState(
                        new HashMap<>(Map.of(
                                "заново", DialogMaker::learnWords,
                                "тест", DialogMaker::startTest,
                                "назад", DialogMaker::back)),
                        null));
        return states;
    }

    private static State MakeState(HashMap<String, Function<Context, Integer>> actions, Function<Context, Integer> fallback) {
        var handlers = new ArrayList<MessageHandler>();
        actions.forEach((key, value) -> handlers.add(new MessageHandler(key, value)));
        return new State(handlers, fallback);
    }

    private static Integer printStatistic(Context context) {
        System.out.println("Тут печатается статистика");
        return 2;
    }

    private static Integer startTest(Context context) {
        context.set("correctAnswers", 0);
        return askWord();
    }

    private static Integer printLearnedWords(Context context) {
        System.out.println("Выводятся выученные слова");
        return 3;
    }


    private static Integer askWord() {
        System.out.println("Напиши перевод слова apple");
        return 4;
    }

    private static Integer checkWord(Context context) {
        int maxQuestions = 5;
        if((int)context.get("correctAnswers") == maxQuestions - 1)
        {
            if(context.getMessage().equals("яблоко"))
            {
                System.out.println("Правильно");
                System.out.println("Закончено");
                return 5;
            }

            System.out.println("*подсказка*");
            return 4;
        }
        if(context.getMessage().equals("яблоко"))
        {
            context.set("correctAnswers", (int)context.get("correctAnswers") + 1);
            System.out.println("Правильно");
            return askWord();
        }
        else
        {
            System.out.println("Неправильно");
            System.out.println("*подсказка*");
        }
        return 4;
    }



    private static Integer back(Context context) {
        return 1;
    }

    private static Integer learnWords(Context context) {
        System.out.println("Напиши интересующую тематику");
        return 6;
    }

    private static Integer printWordsToLearn(Context context) {
        if(!themes.contains(context.getMessage()))
        {
            System.out.println("Нет такой темы");
            return 6;
        }
        System.out.println("Вывожу слова по теме " + context.getMessage());
        return 7;
    }
}
*/
