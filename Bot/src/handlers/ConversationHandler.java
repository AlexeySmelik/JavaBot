package handlers;

import java.io.Closeable;
import java.util.*;

public class ConversationHandler implements Handler<Context, Integer>, Closeable {
    public enum State {
        SAVE(-2), END(-1);

        State(final Integer stateId) { }
    }

    private final List<MessageHandler> commands;
    private final Map<Integer, List<MessageHandler>> states;
    private Integer state;

    public ConversationHandler(
            List<MessageHandler> commands,
            Map<Integer, List<MessageHandler>> states,
            Integer startState
    ) {
        this.commands = commands;
        try {
            checkCorrectStatements(states);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.states = states;
        this.state = startState;
    }

    public ConversationHandler(
            List<MessageHandler> commands,
            Map<Integer, List<MessageHandler>> states,
            MessageHandler entryHandler,
            Context context
    ) {
        this.commands = commands;
        try {
            checkCorrectStatements(states);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.states = states;
        this.state = entryHandler.apply(context);
    }

    public void execute(Context context) {
        if (tryExecuteCommand(context))
            return;
        tryHandleMessage(context);
    }

    private void tryChangeState(Integer newState) {
        try {
            checkCorrectState(newState);
            state = newState;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean tryExecuteCommand(Context context) {
        return commands != null && tryHandle(commands, context);
    }

    private Boolean tryHandleMessage(Context context) {
        return states != null && states.containsKey(state) && tryHandle(states.get(state), context);
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

    private void checkCorrectState(Integer state) throws Exception {
        if (!states.containsKey(state) && Arrays.stream(State.values()).noneMatch(state::equals))
            throw new Exception("No such state");
    }

    private void checkCorrectStatements(Map<Integer, List<MessageHandler>> stats) throws Exception {
        for (var key : stats.keySet())
            if (key <= 0)
                throw new Exception("States ID have to be positive");
    }

    @Override
    public void close() { }

    @Override
    public Integer apply(Context context) { return null; }
}
