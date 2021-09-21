import data.Context;
import handlers.ConversationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class Bot {
    private final Context context;
    private ConversationHandler conversationHandler;

    public Bot() {
        context = new Context();
    }

    public void startPolling() {
        var sc = new Scanner(System.in);
        while (true) {
            context.lastUserMessage = sc.nextLine();
        }
    }
}
