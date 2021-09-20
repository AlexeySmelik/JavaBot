import java.util.ArrayList;
import java.util.Map;

public class ConversationHandler {
    public static Integer SaveState = -1;
    public static Integer EndState = -2;

    private final ArrayList<MessageHandler> commands;
    private final Map<Integer, MessageHandler> statements;
    private Integer state;

    public ConversationHandler(
            ArrayList<MessageHandler> commands,
            Map<Integer, MessageHandler> statements,
            Integer startState) {
        this.commands = commands;
        this.statements = statements;
        this.state = startState;
    }

    public void doAction(Context context) {
        var command = MessageHandler.getMessageHandler(context.lastUserMessage, commands);
        if (command != null)
        {

            var nextState = command.action.apply(context);
            if (!SaveState.equals(nextState)) // != SaveState
            {
                state = nextState;
                if (command.hasPreAction())
                    command.preAction.accept(context);
            }
            return;
        }

        var message = statements.get(state);
        var nextState = message.action.apply(context);
        if (!SaveState.equals(nextState)) // != SaveState
        {
            state = nextState;
            if (message.hasPreAction())
                message.preAction.accept(context);
        }
    }
}
