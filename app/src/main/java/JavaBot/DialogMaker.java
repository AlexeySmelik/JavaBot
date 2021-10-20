package JavaBot;

import JavaBot.handlers.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import JavaBot.resources.WordAndTranslate;
import JavaBot.resources.Context;
import java.util.function.Function;
import JavaBot.handlers.MessageHandler;

public class DialogMaker {
    private static Integer maxQuestions = 2;
    private static EnglishWordStudyBot bot;
    private static HashMap<String, ArrayList<WordAndTranslate>> dictionary = new HashMap<String, ArrayList<WordAndTranslate>>();

    public static HashMap<Integer, State> MakeDialog(EnglishWordStudyBot b,Context context) throws IOException {
        bot = b;
        var first = new ArrayList<WordAndTranslate>();
        first.add(new WordAndTranslate("car", "машина"));
        first.add(new WordAndTranslate("wheel", "колесо"));
        var second = new ArrayList<WordAndTranslate>();
        second.add(new WordAndTranslate("milk", "молоко"));
        second.add(new WordAndTranslate("banana", "банан"));
        dictionary.put("Car", first);
        dictionary.put("Food", second);
        //bot.print(Integer.toString(dictionary.DictionaryByTopics.size()));
        var states = new HashMap<Integer, State>();
        states.put(1,
                MakeState(
                        new HashMap<>(Map.of(
                                "statistic", DialogMaker::printStatistic,
                                "dictionary", DialogMaker::printLearnedWords,
                                "revise", DialogMaker::startTest,
                                "learn", DialogMaker::learnWords)),
                        null));
        states.put(2,
                MakeState(new HashMap<>(Map.of("back", DialogMaker::back)),
                        null));
        states.put(3,
                MakeState(new HashMap<>(Map.of("back", DialogMaker::back)),
                        null));
        states.put(4,
                MakeState(new HashMap<>(), DialogMaker::askTopic));
        states.put(5,
                MakeState(new HashMap<>(), DialogMaker::checkWord));
        states.put(6,
                MakeState(
                        new HashMap<>(Map.of(
                                "again", DialogMaker::startTest,
                                "back", DialogMaker::back)),
                        null));
        states.put(7,
                MakeState(new HashMap<>(), DialogMaker::printWordsToLearn));
        states.put(8,
                MakeState(
                        new HashMap<>(Map.of(
                                "again", DialogMaker::learnWords,
                                "test", DialogMaker::startTest,
                                "back", DialogMaker::back)),
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
        bot.print("Learned words: " + result);
        bot.print("You can write: back - go to main state");
        return 2;
    }

    private static Integer startTest(Context context) {
        context.update("correctAnswers", 0);
        context.update("attempts", 0);
        bot.print("Write a topic");
        for(var i = 0; i < dictionary.keySet().toArray().length; i++){
            bot.print(Integer.toString(i) + " - " + dictionary.keySet().toArray()[i]);
        }
        return 4;
    }

    private static Integer printLearnedWords(Context context) {
        bot.print("Your learned words:");
        for(Map.Entry<String, LearnedWords> e : ((HashMap<String, LearnedWords>) context.get("learnedWords")).entrySet())
        {
            for(var i = 0; i < e.getValue().WellLearnedWords.size(); i++)
            {
                bot.print(e.getValue().WellLearnedWords.get(i).getWord());
            }
        }
        bot.print("You can write: back - go to main state");
        return 3;
    }


    private static Integer askWord(Context context) {
        bot.print(((ArrayList<QuestionForm>)context.get("questions")).get((Integer)context.get("correctAnswers")).question);
        return 5;
    }

    private static Integer askTopic(Context context) {
        var message = Integer.parseInt((String) context.get("message"));
        var learnedWords = ((HashMap<String, LearnedWords>) context.get("learnedWords"));
        if(learnedWords.size() < message) {
            bot.print("I don't have this topic");
            bot.print((String) context.get("message"));
            return 6;
        }
        bot.print(Integer.toString(message));
        var topic = learnedWords.keySet().toArray()[Integer.parseInt((String) context.get("message"))].toString();
        context.update("topic", topic);
        var adapter = ((Adapter) context.get("adapter"));
        context.update("questions", adapter.GetUserQuestions(topic, learnedWords, maxQuestions, dictionary));
        return askWord(context);
    }




    private static Integer checkWord(Context context) {
        var learned = ((HashMap<String, LearnedWords>)context.get("learnedWords"));
        var question = ((ArrayList<QuestionForm>)context.get("questions")).get((int)context.get("correctAnswers"));
        var attempts = (Integer)context.get("attempts");
        var topic = (String) context.get("topic");
        var learnedByTopic = learned.get(topic);
        var index = (int)context.get("correctAnswers");
        var word = new WordAndTranslate(question.answer, question.question);
        if((int)context.get("correctAnswers") == maxQuestions - 1)
        {
            if(((String) context.get("message")).equals(question.answer))
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
                context.update("attempts", 0);
                bot.print("Correct");
                bot.print("Finished");
                bot.print("You can write: \nback - go to main state\nagain - learn new words");
                return 6;
            }
            context.update("attempts", 0);
            question.UpdateHint();
            bot.print(question.hint);
            return 5;
        }
        else if(((String) context.get("message")).equals(question.answer))
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
            bot.print("Correct");
            context.update("attempts", 0);
            return askWord(context);
        }
        else
        {
            if(learnedByTopic.WellLearnedWords.contains(word))
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
            bot.print("Wrong");
            question.UpdateHint();
            bot.print(question.hint);
        }
        return 5;
    }



    private static Integer back(Context context) {
        return 1;
    }

    private static Integer learnWords(Context context) {
        bot.print("Write a topic");
        for(var i = 0; i < dictionary.keySet().toArray().length; i++){
            bot.print(Integer.toString(i) + " - " + dictionary.keySet().toArray()[i]);
        }
        return 7;
    }

    private static Integer printWordsToLearn(Context context) {
        var message = Integer.parseInt((String) context.get("message"));
        var learnedWords = ((HashMap<String, LearnedWords>)context.get("learnedWords"));
        var topic = dictionary.keySet().toArray()[message].toString();
        if(dictionary.size() < message)
        {
            bot.print("I don't have this topic");
            return 7;
        }


        bot.print("Words from topic: " + topic);
        var newWords = 0;
        for(var i = 0; i < dictionary.get(topic).size() && newWords < maxQuestions; i++){
            var word = dictionary.get(topic).get(i);
            if(!learnedWords.get(topic).WellLearnedWords.contains(word) &&
                    !learnedWords.get(topic).NormallyLearnedWords.contains(word) &&
                    !learnedWords.get(topic).BadlyLearnedWords.contains(word))
            {
                learnedWords.get(topic).BadlyLearnedWords.add(word);
                bot.print(word.getWord() + " - " + word.getTranslate());
                newWords++;
            }
        }
        bot.print("You can write: \ntest - start test to revise english words\nback - go to main state\nagain - learn some new words");


        return 8;
    }
}

