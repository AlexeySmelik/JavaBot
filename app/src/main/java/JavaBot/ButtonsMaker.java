package JavaBot;

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
                buttonsTexts.add("dictionary");
                buttonsTexts.add("learn");
                buttonsTexts.add("revise");
                buttonsTexts.add("statistic");
            }
            case PrintingStatistic, PrintingLearnedWords, AskingUserWord, PrintingWordsToLearn -> buttonsTexts.add("back");
            case FinishingTest -> {
                buttonsTexts.add("again");
                buttonsTexts.add("back");
            }
            case FinishingPrintingNewWords -> {
                buttonsTexts.add("test");
                buttonsTexts.add("back");
                buttonsTexts.add("again");
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
