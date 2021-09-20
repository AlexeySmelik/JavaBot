import java.util.ArrayList;
import java.util.Map;

public class ConversationHandler {
    public static Integer SaveState = -1;
    public static Integer EndState = -2;

    private final ArrayList<MessageHandler> commands;
    private Map<Integer, MessageHandler> statements;
    private Integer state;

    public ConversationHandler(
            ArrayList<MessageHandler> commands,
            Map<Integer, MessageHandler> statements,
            Integer startState) {
        this.commands = commands;
        this.statements = statements;
        this.state = startState;
    }

    public void changeStatements(Map<Integer, MessageHandler> newStatements) {
        statements = newStatements;
    }

    public void doAction(Context context) {
        var command = MessageHandler.getMessageHandler(context.lastUserMessage, commands);
        if (command != null)
        {
            var nextState = command.action.apply(context);
            if (!SaveState.equals(nextState) && statements.containsKey(nextState)) // != SaveState
            {
                state = nextState;
                if (statements.get(state).hasPreAction())
                    statements.get(state).preAction.accept(context);
            }
            return;
        }

        if (!statements.containsKey(state))
            return;

        var message = statements.get(state);
        var nextState = message.action.apply(context);
        if (!SaveState.equals(nextState) && statements.containsKey(nextState)) // != SaveState
        {
            state = nextState;
            if (statements.get(state).hasPreAction())
                statements.get(state).preAction.accept(context);
        }
    }
}
