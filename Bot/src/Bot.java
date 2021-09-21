import data.Context;
import handlers.ConversationHandler;

import java.util.Scanner;

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
