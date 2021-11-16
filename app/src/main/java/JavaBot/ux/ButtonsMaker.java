package JavaBot.ux;

import JavaBot.Strings;
import JavaBot.data_classes.DialogState;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.util.ArrayList;

public class ButtonsMaker {

    public static ArrayList<KeyboardButton> GetButtons(DialogState currentState)
    {
        var result = new ArrayList<KeyboardButton>();
        var buttonsTexts = new ArrayList<String>();
        switch (currentState) {
            case MainState -> {
                buttonsTexts.add(Strings.dictionary.string);
                buttonsTexts.add(Strings.learn.string);
                buttonsTexts.add(Strings.revise.string);
                buttonsTexts.add(Strings.statistic.string);
            }
            case PrintingStatistic, PrintingLearnedWords, AskingUserWord, PrintingWordsToLearn, FinishingTest ->
                buttonsTexts.add(Strings.back.string);
            case FinishingPrintingNewWords -> {
                buttonsTexts.add(Strings.test.string);
                buttonsTexts.add(Strings.back.string);
                buttonsTexts.add(Strings.again.string);
            }
        }

        for(var text : buttonsTexts)
        {
            var button = new KeyboardButton();
            button.setText(text);
            result.add(button);
        }
        return result;
    }
}
