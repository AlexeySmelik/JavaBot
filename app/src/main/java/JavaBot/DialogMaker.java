package JavaBot;

import JavaBot.data_classes.DialogState;
import JavaBot.dialog.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import JavaBot.data_classes.Word;
import JavaBot.dialog.Context;
import java.util.function.Function;
import JavaBot.dialog.MessageHandler;

public class DialogMaker {
    private static final Integer maxQuestions = 2;
    private static EnglishWordStudyBot bot;

    public static HashMap<Integer, State> makeDialog(EnglishWordStudyBot bot) {
        DialogMaker.bot = bot;
        var states = new HashMap<Integer, State>();
        states.put(DialogState.MainState.ordinal(),
                makeState(
                        new HashMap<>(Map.of(
                                "statistic", DialogMaker::printStatistic,
                                "dictionary", DialogMaker::printLearnedWords,
                                "revise", DialogMaker::startTest,
                                "learn", DialogMaker::learnWords)),
                        null));
        states.put(DialogState.PrintingStatistic.ordinal(),
                makeState(new HashMap<>(Map.of("back", DialogMaker::back)),
                        null));
        states.put(DialogState.PrintingLearnedWords.ordinal(),
                makeState(new HashMap<>(Map.of("back", DialogMaker::back)),
                        null));
        states.put(DialogState.AskingUserWord.ordinal(),
                makeState(new HashMap<>(), DialogMaker::checkWord));
        states.put(DialogState.FinishingTest.ordinal(),
                makeState(
                        new HashMap<>(Map.of(
                                "again", DialogMaker::startTest,
                                "back", DialogMaker::back)),
                        null));
        states.put(DialogState.PrintingWordsToLearn.ordinal(),
                makeState(new HashMap<>(), DialogMaker::printWordsToLearn));
        states.put(DialogState.FinishingPrintingNewWords.ordinal(),
                makeState(
                        new HashMap<>(Map.of(
                                "again", DialogMaker::learnWords,
                                "test", DialogMaker::startTest,
                                "back", DialogMaker::back)),
                        null));
        return states;
    }

    private static State makeState(HashMap<String, Function<Context, Integer>> actions, Function<Context, Integer> fallback) {
        var handlers = new ArrayList<MessageHandler>();
        actions.forEach((key, value) -> handlers.add(new MessageHandler(key, value)));
        return new State(handlers, fallback);
    }

    private static Integer printStatistic(Context context) {
        var chatId = (String) context.get("chatId");
        var result = bot.userStore.get(chatId).learnedWords.size();
        bot.print("Learned words: " + result, chatId, DialogState.PrintingStatistic);
        bot.print("You can write: back - go to main state", chatId, DialogState.PrintingStatistic);
        return DialogState.PrintingStatistic.ordinal();
    }

    private static Integer startTest(Context context) {
        context.update("correctAnswers", 0);
        context.update("attempts", 0);
        var adapter = (Adapter) context.get("adapter");
        var chatId = (String) context.get("chatId");
        var showedWords = (ArrayList<Word>)context.get("showedWords");
        if(showedWords == null || showedWords.size() == 0)
        {
            bot.print("You have no words to revise", chatId, DialogState.FinishingTest);
        }

        context.update("questions", adapter.getUserQuestions(
                maxQuestions,
                (ArrayList<Word>)context.get("showedWords")));
        return askWord(context);
    }

    private static Integer printLearnedWords(Context context) {
        var chatId = (String) context.get("chatId");
        bot.print("Your learned words:", chatId, DialogState.PrintingLearnedWords);
        for(var word : bot.userStore.get(chatId).learnedWords)
            bot.print(word.getHeading(), chatId, DialogState.PrintingLearnedWords);
        bot.print("You can write: back - go to main state", chatId, DialogState.PrintingLearnedWords);
        return DialogState.PrintingLearnedWords.ordinal();
    }

    private static Integer askWord(Context context) {
        var chatId = (String) context.get("chatId");
        var questions = (ArrayList<QuestionForm>)context.get("questions");
        bot.print(questions.get((Integer)context.get("correctAnswers")).question, chatId, DialogState.AskingUserWord);
        return DialogState.AskingUserWord.ordinal();
    }

    public static Integer help(Context context){
        var chatId = (String) context.get("chatId");
        bot.print("""
                In main state you can write these messages:\s
                statistic - print statistic of your learned words\s
                dictionary - print all your learned words\s
                learn - show you new english words\s
                revise - start a test to revise words which have been shown earlier""", chatId, DialogState.MainState);
        return DialogState.MainState.ordinal();
    }

    private static Integer checkWord(Context context) {
        var chatId = (String) context.get("chatId");
        var questions = (ArrayList<QuestionForm>)context.get("questions");
        var question = questions.get((int)context.get("correctAnswers"));
        var attempts = (Integer)context.get("attempts");
        var topic = (String) context.get("topic");
        var learnedByTopic = new ArrayList<Word>();
        for(var e : bot.userStore.get(chatId).learnedWords){
            if(bot.wordStore.get(topic).contains(e)){
                learnedByTopic.add(e);
            }
        }
        var word = new Word(question.answer, question.question);
        if(context.get("message").equals(question.answer))
        {
            if(attempts == 0)
                processLearnedWord(word, context);
            context.update("correctAnswers", (int)context.get("correctAnswers") + 1);
            bot.print("Correct", chatId, DialogState.AskingUserWord);
            context.update("attempts", 0);
            if((int)context.get("correctAnswers") != questions.size())
                return askWord(context);
        }
        else
        {
            context.update("attempts", 1);
            question.UpdateHint();
            bot.print(question.hint, chatId, DialogState.AskingUserWord);
        }
        if((int)context.get("correctAnswers") == questions.size())
        {
            bot.print("""
                        You can write:\s
                        back - go to main state
                        again - learn new words""", chatId, DialogState.FinishingTest);
            return DialogState.FinishingTest.ordinal();
        }
        return DialogState.AskingUserWord.ordinal();
    }

    private static void processLearnedWord(Word word, Context context){
        var chatId = (String) context.get("chatId");
        var showed = (ArrayList<Word>)context.get("showedWords");
        var user = bot.userStore.get(chatId);
        user.updateLearnedWords(List.of(word));
        bot.userStore.save(user, true);
        showed.remove(word);
    }

    public static Integer back(Context context) {
        var chatId = (String) context.get("chatId");
        bot.print("You are in main state, bro...", chatId, DialogState.MainState);

        return DialogState.MainState.ordinal();
    }

    private static Integer learnWords(Context context) {
        var chatId = (String) context.get("chatId");
        bot.print("Write a topic", chatId, DialogState.PrintingWordsToLearn);
        var idx = 0;
        for (var topic : bot.wordStore.getTopics()) {
            bot.print(idx + " - " + topic, chatId, DialogState.PrintingWordsToLearn);
            idx += 1;
        }
        return DialogState.PrintingWordsToLearn.ordinal();
    }

    private static Integer printWordsToLearn(Context context) {
        var chatId = (String) context.get("chatId");
        var message = Integer.parseInt((String) context.get("message"));
        var topic = bot.wordStore.getTopics().get(message);
        if(bot.wordStore.getTopics().size() < message)
        {
            bot.print("I don't have this topic", chatId, DialogState.PrintingWordsToLearn);
            return DialogState.PrintingWordsToLearn.ordinal();
        }
        bot.print("Words from topic: " + topic, chatId, DialogState.PrintingWordsToLearn);
        var newWords = new ArrayList<Word>();
        for(var i = 0; i < bot.wordStore.get(topic).size() && newWords.size() < maxQuestions; i++){
            var word = bot.wordStore.get(topic).get(i);
            if(!bot.userStore.get(chatId).learnedWords.contains(word)){
                newWords.add(word);
            }
        }
        for(var e : newWords){
            bot.print(e.getHeading() + " - " + e.getTranslation(), chatId, DialogState.PrintingWordsToLearn);
        }
        context.update("showedWords", newWords);
        context.update("topic", topic);
        bot.print("""
                You can write:\s
                test - start test to revise english words
                back - go to main state
                again - learn some new words""", chatId, DialogState.FinishingPrintingNewWords);
        return DialogState.FinishingPrintingNewWords.ordinal();
    }
}
