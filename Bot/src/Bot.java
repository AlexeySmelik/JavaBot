import handlers.Context;
import handlers.ConversationHandler;
import handlers.ConversationListener;
import handlers.MessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Bot {
    private final Context context;
    private ConversationHandler convHandler;

    public Bot() {
        var data = new HashMap<String, String>();
        data.put("message", "hello");
        data.put("id", "1234");
        context = new Context(data);

        var states = new HashMap<Integer, List<MessageHandler>>();
        var list = new ArrayList<MessageHandler>();
        list.add(new MessageHandler("да", Bot::start));
        list.add(new MessageHandler("нет", Bot::restart));
        states.put(1, list);
        try(var convHandler = new ConversationHandler(null, states, list.get(0), context)) {
            var listener = new ConversationListener(convHandler);
            context.manager.add("message", listener);
        }
    }

    private static Integer start(Context context) {
        System.out.println("Абоба?");
        return 1;
    }

    private static Integer restart(Context context) {
        System.out.println("Точно?");
        return 1;
    }

    public void startPolling() {
        var sc = new Scanner(System.in);
        while (true)
            context.set("message", sc.nextLine());
    }
}
