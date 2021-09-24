package handlers;

import java.util.List;
import java.util.Map;

public class ConversationHandler implements Handler<Context, Integer>, AutoCloseable {
    private final List<MessageHandler> commands;
    private final Map<Integer, State> states;
    private Integer state;

    public ConversationHandler(List<MessageHandler> commands, Map<Integer, State> states, Integer startState)
            throws Exception {
        this.commands = commands;
        this.states = states;
        this.state = startState;
        checkCorrectStatesId();
    }

    public void execute(Context context) {
        if (tryExecuteCommand(context))
            return;
        handleMessage(context);
    }

    private void tryChangeState(Integer newState) {
        try {
            if (!states.containsKey(state))
                throw new Exception("No such state");
            state = newState;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean tryExecuteCommand(Context context) {
        return commands != null && tryHandle(commands, context);
    }

    private void handleMessage(Context context) {
        if (states == null ||
            !states.containsKey(state) ||
            tryHandle(states.get(state).getHandlers(), context))
            return;
        var fallbackState = states.get(state).getFallback().apply(context);
        tryChangeState(fallbackState);
    }

    private Boolean tryHandle(List<MessageHandler> handlers, Context context) {
        for (var handler : handlers)
            if (handler.is(context.getMessage()))
            {
                var newState = handler.apply(context);
                tryChangeState(newState);
                return true;
            }
        return false;
    }

    private void checkCorrectStatesId() throws Exception{
        for (var key : states.keySet())
            if (key <= 0)
                throw new Exception("States ID have to be positive");
    }

    @Override
    public void close() { }

    @Override
    public Integer apply(Context context) { return null; }
}
