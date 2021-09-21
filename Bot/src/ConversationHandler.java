import java.util.ArrayList;
import java.util.Map;

public class ConversationHandler implements AutoCloseable {
    public static final Integer SaveState = -1;
    public static final Integer EndState = -2;

    private final ArrayList<MessageHandler> commands;
    private final Map<Integer, MessageHandler> statements;
    private Integer state;

    public ConversationHandler(
            ArrayList<MessageHandler> commands,
            Map<Integer, MessageHandler> statements,
            Integer startState) throws Exception {
        this.commands = commands;
        checkStatementsOnCorrect(statements);
        this.statements = statements;
        this.state = startState;
    }

    public void doAction(Context context) {
        var command = MessageHandler.getMessageHandler(context.lastUserMessage, commands);
        if (command != null)
        {
            var newState = command.action.apply(context);
            tryChangeState(newState, context);
            return;
        }

        if (!statements.containsKey(state))
            return;

        var message = statements.get(state);
        var newState = message.action.apply(context);
        tryChangeState(newState, context);
    }

    private Boolean tryChangeState(Integer newStage, Context context){
        if (EndState.equals(newStage))
        {
            state = newStage;
            return true;
        }
        if (!SaveState.equals(newStage) && statements.containsKey(newStage))
        {
            state = newStage;
            if (statements.get(state).hasPreAction())
                statements.get(state).preAction.accept(context);
            return true;
        }
        return false;
    }

    @Override
    public void close() throws Exception { }

    private void checkStatementsOnCorrect(Map<Integer, MessageHandler> stats) throws Exception {
        for (var key : stats.keySet())
            if (key <= 0)
                throw new Exception("StatementsID have to be positive");
    }
}
