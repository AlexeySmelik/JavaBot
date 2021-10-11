package JavaBot;

import JavaBot.resources.Context;
import JavaBot.handlers.MessageHandler;
import JavaBot.handlers.State;
import JavaBot.resources.WordAndTranslate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class DialogMaker {
    private static DictionaryRepositoryByTopics dictionary;
    private static Integer maxQuestions = 2;

    static {
        try {
            dictionary = new DictionaryRepositoryByTopics();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, State> MakeDialog(Context context) throws IOException {
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
                MakeState(new HashMap<>(), DialogMaker::askTopic));
        states.put(5,
                MakeState(new HashMap<>(), DialogMaker::checkWord));
        states.put(6,
                MakeState(
                        new HashMap<>(Map.of(
                                "заново", DialogMaker::startTest,
                                "назад", DialogMaker::back)),
                        null));
        states.put(7,
                MakeState(new HashMap<>(), DialogMaker::printWordsToLearn));
        states.put(8,
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
        var result = 0;
        for(Map.Entry<String, LearnedWords> e : ((HashMap<String, LearnedWords>) context.get("learnedWords")).entrySet())
        {
            result += e.getValue().WellLearnedWords.size();
        }
        System.out.println("Выучено слов: " + result);
        return 2;
    }

    private static Integer startTest(Context context) {
        context.update("correctAnswers", 0);
        context.update("attempts", 0);
        System.out.println("Выбери тему");
        return 4;
    }

    private static Integer printLearnedWords(Context context) {
        System.out.println("Твои выученные слова:");
        for(Map.Entry<String, LearnedWords> e : ((HashMap<String, LearnedWords>) context.get("learnedWords")).entrySet())
        {
            for(var i = 0; i < e.getValue().WellLearnedWords.size(); i++)
            {
                System.out.println(e.getValue().WellLearnedWords.get(i).getWord());
            }
        }
        return 3;
    }


    private static Integer askWord(Context context) {
        System.out.println(((ArrayList<QuestionForm>)context.get("questions")).get((Integer)context.get("correctAnswers")).question);
        return 5;
    }

    private static Integer askTopic(Context context) {
        var message = context.get("message").toString().substring(0, 1).toUpperCase(Locale.ROOT) +
                context.get("message").toString().substring(1);
        var learnedWords = ((HashMap<String, LearnedWords>) context.get("learnedWords"));
        if(!learnedWords.containsKey(message)) {
            System.out.println("Нет такой темы");
            return 6;
        }
        if(((Adapter) context.get("adapter")).GetUserQuestions(context.get("message").toString(), learnedWords, maxQuestions) == null)
        {
            System.out.println("Ты еще не учил слова");
            return 1;
        }
        context.update("topic", message);
        context.update("questions", ((Adapter) context.get("adapter")).GetUserQuestions(context.get("message").toString(), learnedWords, maxQuestions));
        return askWord(context);
    }

    private static WordAndTranslate GetWordByQuestion(QuestionForm question, Integer index, Integer maxQuestions){
        if(index < maxQuestions){
            return new WordAndTranslate(question.question, question.answer);
        }
        return new WordAndTranslate(question.answer, question.question);
    }


    private static Integer checkWord(Context context) {
        var learned = ((HashMap<String, LearnedWords>)context.get("learnedWords"));
        var question = ((ArrayList<QuestionForm>)context.get("questions")).get((int)context.get("correctAnswers"));
        var attempts = (Integer)context.get("attempts");
        var learnedByTopic = learned.get((String) context.get("topic"));
        var index = (int)context.get("correctAnswers");
        var word = GetWordByQuestion(question, index, maxQuestions);
        if((int)context.get("correctAnswers") == 2 * maxQuestions - 1)
        {
            var lastWord = GetWordByQuestion(question, 2 * maxQuestions - 1, maxQuestions);
            if(context.get("message").toString().equals(question.answer))
            {
                if(attempts == 0 && learnedByTopic.BadlyLearnedWords.contains(lastWord))
                {
                    learnedByTopic.BadlyLearnedWords.remove(lastWord);
                    learnedByTopic.NormallyLearnedWords.add(lastWord);
                }
                else if((Integer)context.get("attempts") == 0 && learnedByTopic.NormallyLearnedWords.contains(lastWord))
                {
                    learnedByTopic.NormallyLearnedWords.remove(lastWord);
                    learnedByTopic.WellLearnedWords.add(lastWord);
                }
                context.update("attempts", 0);
                System.out.println("Правильно");
                System.out.println("Закончено");
                return 6;
            }
            context.update("attempts", 0);
            question.UpdateHint();
            System.out.println(question.hint);
            return 5;
        }
        else if(context.get("message").toString().equals(question.answer))
        {
            if(attempts == 0 && learnedByTopic.BadlyLearnedWords.contains(word))
            {
                learnedByTopic.BadlyLearnedWords.remove(word);
                learnedByTopic.NormallyLearnedWords.add(word);
            }
            else if((Integer)context.get("attempts") == 0 && learnedByTopic.NormallyLearnedWords.contains(word))
            {
                learnedByTopic.NormallyLearnedWords.remove(word);
                learnedByTopic.WellLearnedWords.add(word);
            }
            context.update("correctAnswers", (int)context.get("correctAnswers") + 1);
            System.out.println("Правильно");
            context.update("attempts", 0);
            return askWord(context);
        }
        else
        {
            if(learnedByTopic.WellLearnedWords.contains(GetWordByQuestion(question, index, maxQuestions)))
            {
                learnedByTopic.WellLearnedWords.remove(word);
                learnedByTopic.NormallyLearnedWords.add(word);
            }
            else if(learnedByTopic.NormallyLearnedWords.contains(word))
            {
                learnedByTopic.NormallyLearnedWords.remove(word);
                learnedByTopic.BadlyLearnedWords.add(word);
            }
            context.update("attempts", 1);
            System.out.println("Неправильно");
            question.UpdateHint();
            System.out.println(question.hint);
        }
        return 5;
    }



    private static Integer back(Context context) {
        return 1;
    }

    private static Integer learnWords(Context context) {
        System.out.println("Напиши интересующую тематику");
        return 7;
    }

    private static Integer printWordsToLearn(Context context) {
        var message = context.get("message").toString().substring(0, 1).toUpperCase(Locale.ROOT) + context.get("message").toString().substring(1);
        var learnedWords = ((HashMap<String, LearnedWords>)context.get("learnedWords"));
        if(!learnedWords.containsKey(message))
        {
            System.out.println("Нет такой темы");
            return 7;
        }
        System.out.println("Вывожу слова по теме: " + context.get("message").toString());
        var newWords = 0;
        for(var i = 0; i < dictionary.DictionaryByTopics.get(message).size() && newWords < 2 * maxQuestions; i++){
            var word = dictionary.DictionaryByTopics.get(message).get(i);
            if(!learnedWords.get(message).WellLearnedWords.contains(word) &&
                    !learnedWords.get(message).NormallyLearnedWords.contains(word) &&
                    !learnedWords.get(message).BadlyLearnedWords.contains(word))
            {
               learnedWords.get(message).BadlyLearnedWords.add(word);
                System.out.println(word.getWord() + " - " + word.getTranslate());
                newWords++;
            }
        }
        return 8;
    }
}

