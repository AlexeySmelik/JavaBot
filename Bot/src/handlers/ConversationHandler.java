package handlers;

import data.Context;

import java.util.*;

public class ConversationHandler implements AutoCloseable {
    public enum State {
        SAVE(-2), END(-1);

        State(final Integer stateId) { }
    }

    private final ArrayList<MessageHandler> commands;
    private final Map<Integer, MessageHandler> states;
    private Context context;
    private Integer state;

    public ConversationHandler(
            ArrayList<MessageHandler> commands,
            Map<Integer, MessageHandler> states,
            Context context,
            Integer startState
    ) throws Exception {
        this.commands = commands;
        checkCorrectStatements(states);
        this.states = states;
        this.state = startState;
        this.context = context;
        tryHandleMessage();
    }

    /**
     * Этот метод лучше не использовать, но если совсем никак, то ладно...
     */
    public void changeContext(Context context) {
        this.context = context;
    }

    /**
     * Можно перейти на другое состояние вручную, если ну прям вообще никак...
     */
    public void moveTo(Integer stateID) {
        tryChangeState(stateID);
    }

    /**
     * А вот этот метод я лично сам рекомендую всем и каждому)0))
     * За вас всё сделает машина, думать не надо, просто вызвать это и произойдёт магия
     * Pogchamp XD;
     */
    public void move() throws Exception {
        if (tryExecuteCommand())
            return;
        if (tryHandleMessage())
            return;
        throw new Exception("No such message handler");
    }

    private void tryChangeState(Integer newState) {
        try {
            checkCorrectState(newState);
            state = newState;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean tryExecuteCommand() {
        for (var command : commands)
            if (command.is(context.lastUserMessage))
            {
                tryChangeState(command.apply(context));
                return true;
            }

        return false;
    }

    private Boolean tryHandleMessage() {
        if (!states.containsKey(state))
            return false;
        var newState = states.get(state).apply(context);
        tryChangeState(newState);
        return true;
    }

    private void checkCorrectState(Integer state) throws Exception {
        if (!states.containsKey(state) && Arrays.stream(State.values()).noneMatch(state::equals))
            throw new Exception("No such state");
    }

    private void checkCorrectStatements(Map<Integer, MessageHandler> stats) throws Exception {
        for (var key : stats.keySet())
            if (key <= 0)
                throw new Exception("States ID have to be positive");
    }

    @Override
    public void close() {

    }
}
