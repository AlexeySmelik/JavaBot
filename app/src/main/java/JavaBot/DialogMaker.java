package JavaBot;

import JavaBot.handlers.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import JavaBot.resources.WordAndTranslate;
import JavaBot.resources.Context;
import java.util.function.Function;
import JavaBot.handlers.MessageHandler;

public class DialogMaker {
    private static Integer maxQuestions = 2;
    private static EnglishWordStudyBot bot;

    public static HashMap<Integer, State> MakeDialog(EnglishWordStudyBot bot) {
        DialogMaker.bot = bot;
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
        var chatId = (String) context.get("chatId");
        var result = bot.operatorDB.getWords(chatId).size();
        bot.print("Learned words: " + result, chatId);
        bot.print("You can write: back - go to main state", chatId);
        return 2;
    }

    private static Integer startTest(Context context) {
        context.update("correctAnswers", 0);
        context.update("attempts", 0);
        var adapter = (Adapter)context.get("adapter");
        var chatId = (String) context.get("chatId");
        context.update("questions", adapter.GetUserQuestions(maxQuestions, (ArrayList<WordAndTranslate>)context.get("showedWords"), chatId, bot.wordStore, bot.operatorDB, (String)context.get("topic") ));

        return askWord(context);
    }

    private static Integer printLearnedWords(Context context) {
        var chatId = (String) context.get("chatId");
        bot.print("Your learned words:", chatId);
        for(var word : bot.operatorDB.getWords(chatId))
            bot.print(word.getWord(), chatId);
        bot.print("You can write: back - go to main state", chatId);
        return 3;
    }


    private static Integer askWord(Context context) {
        var chatId = (String) context.get("chatId");
        bot.print("Here", chatId);
        bot.print(((ArrayList<QuestionForm>)context.get("questions")).get((Integer)context.get("correctAnswers")).question, chatId);
        return 5;
    }

    private static Integer askTopic(Context context) {
        var chatId = (String) context.get("chatId");
        var message = Integer.parseInt((String) context.get("message"));
        if(bot.wordStore.getTopicsName().size() < message) {
            bot.print("I don't have this topic", chatId);
            bot.print((String) context.get("message"), chatId);
            return 6;
        }
        bot.print(Integer.toString(message), chatId);
        var topic = bot.wordStore.getTopicsName().toArray()[Integer.parseInt((String) context.get("message"))].toString();
        context.update("topic", topic);
        var adapter = ((Adapter) context.get("adapter"));
        for(var e : (ArrayList<WordAndTranslate>)context.get("showedWords")){
            bot.print(e.getWord(), chatId);
        }
        //context.update("questions", adapter.GetUserQuestions(maxQuestions, (ArrayList<WordAndTranslate>)context.get("showedWords"), chatId));
        return askWord(context);
    }

    public static Integer Help(Context context){
        var chatId = (String) context.get("chatId");
        bot.print("In main state you can write these messages: \nstatistic - print statistic of your learned words \ndictionary - print all your learned words \nlearn - show you new english words \nrevise - start a test to revise words which have been shown earlier", chatId);
        return 1;
    }

    private static Integer checkWord(Context context) {
        var chatId = (String) context.get("chatId");
        var question = ((ArrayList<QuestionForm>)context.get("questions")).get((int)context.get("correctAnswers"));
        var attempts = (Integer)context.get("attempts");
        var topic = (String) context.get("topic");
        var learnedByTopic = new ArrayList<WordAndTranslate>();
        for(var e : bot.operatorDB.getWords(chatId)){
            if(bot.wordStore.get(topic).contains(e)){
                learnedByTopic.add(e);
            }
        }
        var index = (int)context.get("correctAnswers");
        var word = new WordAndTranslate(question.answer, question.question);
        var showed = (ArrayList<WordAndTranslate>)context.get("showedWords");
        if((int)context.get("correctAnswers") == maxQuestions - 1)
        {
            if(((String) context.get("message")).equals(question.answer))
            {
                if(attempts == 0)
                {
                    var list = new ArrayList<WordAndTranslate>();
                    list.add(word);
                    bot.operatorDB.updateWords(chatId, list);
                    if(showed.contains(word)){
                        showed.remove(word);
                    }
                }
                context.update("attempts", 0);
                bot.print("Correct", chatId);
                bot.print("Finished", chatId);
                bot.print("You can write: \nback - go to main state\nagain - learn new words", chatId);
                return 6;
            }
            context.update("attempts", 0);
            question.UpdateHint();
            bot.print(question.hint, chatId);
            return 5;
        }
        else if(((String) context.get("message")).equals(question.answer))
        {
            if(attempts == 0)
            {
                var list = new ArrayList<WordAndTranslate>();
                list.add(word);
                bot.operatorDB.updateWords(chatId, list);
                if(showed.contains(word)){
                    showed.remove(word);
                }
            }
            context.update("correctAnswers", (int)context.get("correctAnswers") + 1);
            bot.print("Correct", chatId);
            context.update("attempts", 0);
            return askWord(context);
        }
        else
        {
            if(attempts == 0)
            {
                var list = new ArrayList<WordAndTranslate>();
                list.add(word);
                bot.operatorDB.updateWords(chatId, list);
                if(showed.contains(word)){
                    showed.remove(word);
                }
            }
            context.update("attempts", 1);
            bot.print("Wrong", chatId);
            question.UpdateHint();
            bot.print(question.hint, chatId);
        }
        return 5;
    }



    private static Integer back(Context context) {
        return 1;
    }

    private static Integer learnWords(Context context) {
        var chatId = (String) context.get("chatId");
        bot.print("Write a topic", chatId);
        var idx = 0;
        for (var topic : bot.wordStore.getTopicsName()) {
            bot.print(idx + " - " + topic, chatId);
            idx += 1;
        }
        return 7;
    }

    private static Integer printWordsToLearn(Context context) {
        var chatId = (String) context.get("chatId");
        var message = Integer.parseInt((String) context.get("message"));
        var topic = bot.wordStore.getTopicsName().get(message); // TODO
        if(bot.wordStore.getTopicsName().size() < message)
        {
            bot.print("I don't have this topic", chatId);
            return 7;
        }


        bot.print("Words from topic: " + topic, chatId);
        var newWords = new ArrayList<WordAndTranslate>();
        for(var i = 0; i < bot.wordStore.get(topic).size() && newWords.size() < maxQuestions; i++){
            var tuple = bot.wordStore.get(topic).get(i);
            var word = new WordAndTranslate(tuple.getWord(), tuple.getTranslate());
            if(!bot.operatorDB.getWords(chatId).contains(word)){
                newWords.add(word);
            }
        }
        for(var e : newWords){
            bot.print(e.getWord() + " - " + e.getTranslate(), chatId);
        }
        context.update("showedWords", newWords);
        context.update("topic", topic);
        bot.print("You can write: \ntest - start test to revise english words\nback - go to main state\nagain - learn some new words", chatId);
        return 8;
    }
}

