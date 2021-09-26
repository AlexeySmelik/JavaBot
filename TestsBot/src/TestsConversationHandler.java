import handlers.Context;
import handlers.ConversationHandler;
import handlers.MessageHandler;
import handlers.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestsConversationHandler {
    private ConversationHandler convHandler;
    private List<MessageHandler> commands;
    private Map<Integer, State> states;

    @BeforeEach
    public void init() {
        commands = new ArrayList<>();
        states = new HashMap<>();
    }

    @Test
    public void checkPriorityCommand() throws Exception {
        var command = new MessageHandler("help", c -> {
            c.set("nextState", 3);
            return 3;
        });

        commands.add(command);
        var handler = new MessageHandler("help", c -> {
            c.set("nextState", 2);
            return 2;
        });
        var handlers = new ArrayList<MessageHandler>();
        handlers.add(handler);

        states.put(1, new State(handlers, null));
        var startState = 1;
        var data = new HashMap<String, Object>();
        data.put("nextState", startState);

        var context = new Context(1, data);
        context.updateMessage("help");

        initConvHandler(startState);
        convHandler.execute(context);
        Assertions.assertEquals(3, context.get("nextState"));
    }

    @Test
    public void checkMessageHandlerStateChange() throws Exception {
        var handler1 = new MessageHandler("abc", c -> {
            c.set("nextState", 1);
            return 1;
        });
        var handler2 = new MessageHandler("abcd", c -> {
            c.set("nextState", 2);
            return 2;
        });
        var handlers = new ArrayList<MessageHandler>();
        handlers.add(handler1);
        handlers.add(handler2);

        var state1 = new State(handlers, null);
        var state2 = new State(null, null);
        states.put(1, state1);
        states.put(2, state2);

        var startState = 1;
        var data = new HashMap<String, Object>();
        data.put("nextState", startState);
        var context = new Context(1, data);
        context.updateMessage("abc");

        initConvHandler(startState);
        convHandler.execute(context);
        Assertions.assertEquals(1, context.get("nextState"));

        context.updateMessage("abcd");
        convHandler.execute(context);
        Assertions.assertEquals(2, context.get("nextState"));
    }

    @Test
    public void checkFallbackStateChange() throws Exception {
        var state1 = new State(null, c -> {
            c.set("nextState", 2);
            return 2;
        });
        var state2 = new State(null, c -> {
            c.set("nextState", 3);
            return 3;
        });
        var state3 = new State(null, c -> {
            c.set("nextState", 1);
            return 1;
        });
        states.put(1, state1);
        states.put(2, state2);
        states.put(3, state3);
        var data = new HashMap<String, Object>();
        var startState = 1;
        data.put("nextState", startState);
        var context = new Context(1, data);
        initConvHandler(startState);
        for (var i = 1; i < 4; i++) {
            convHandler.execute(context);
            Assertions.assertEquals(i % 3 + 1, context.get("nextState"));
        }
    }

    @Test
    public void ifNotNecessaryMessageHandlerUseFallback() throws Exception {
        var data = new HashMap<String, Object>();
        data.put("cnt", 0);
        var context = new Context(1, data);
        context.set("cnt", 0);
        var state = new State(null, c -> {
            c.set("cnt", (Integer)c.get("cnt") + 54);
            return 1;
        });
        states.put(1, state);
        initConvHandler(1);

        convHandler.execute(context);
        Assertions.assertEquals(54, context.get("cnt"));

        context.updateMessage("Lol");
        convHandler.execute(context);
        Assertions.assertEquals(108, context.get("cnt"));
    }


    private void initConvHandler(Integer startState) throws Exception {
        convHandler = new ConversationHandler(commands, states, startState);
    }
}
