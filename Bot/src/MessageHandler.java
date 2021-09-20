import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class MessageHandler {
    public Function<Context, Integer> action;
    public Consumer<Context> preAction;
    private final String inText;

    public MessageHandler(String inText, Function<Context, Integer> action) {
        this.inText = inText;
        this.action = action;
        this.preAction = null;
    }

    public MessageHandler(
            String inText,
            Function<Context, Integer> action,
            Consumer<Context> preAction) {
        this.inText = inText;
        this.action = action;
        this.preAction = preAction;
    }

    public Boolean hasPreAction() {
        return preAction != null;
    }

    public static MessageHandler getMessageHandler(String inText, ArrayList<MessageHandler> handlers) {
        for (var handler : handlers)
            if (handler.inText.equals(inText))
                return handler;
        return null;
    }
}
