package JavaBot;

import JavaBot.data_classes.Context;
import JavaBot.handlers.ConversationHandler;
import JavaBot.handlers.MessageHandler;
import JavaBot.handlers.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestsConversationHandler {
    private ConversationHandler convHandler;
    private List<MessageHandler> commands;
    private Map<Integer, State> states;

    // todo Mock

    // IDo { int Do(); }

    // Mock<IDo>.Setup(x => x.Do()).Returns(42);

    @BeforeEach
    public void init() {
        commands = new ArrayList<>();
        states = new HashMap<>();
    }

    @Test
    public void checkPriorityCommand() throws Exception {
        var command = new MessageHandler("help", c -> {
            c.update("nextState", 3);
            return 3;
        });

        commands.add(command);
        var handler = new MessageHandler("help", c -> {
            c.update("nextState", 2);
            return 2;
        });
        var handlers = new ArrayList<MessageHandler>();
        handlers.add(handler);

        states.put(1, new State(handlers, null));
        var startState = 1;
        var data = new HashMap<String, Object>();
        data.put("message", "");
        data.put("nextState", startState);

        var context = new Context(data);
        context.update("message", "help");

        initConvHandler(startState);
        convHandler.execute(context);
        Assertions.assertEquals(3, context.get("nextState"));
    }

    // naming AAA A_B_C
    @Test
    public void checkMessageHandlerStateChange() throws Exception {
        // Arange


        // Act


        // Assert



        var handler1 = new MessageHandler("abc", c -> {
            c.update("nextState", 1);
            return 1;
        });
        var handler2 = new MessageHandler("abcd", c -> {
            c.update("nextState", 2);
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
        data.put("message", "");
        data.put("nextState", startState);
        var context = new Context(data);
        context.update("message", "abc");

        initConvHandler(startState);
        convHandler.execute(context);
        Assertions.assertEquals(1, context.get("nextState"));

        context.update("message", "abcd");
        convHandler.execute(context);
        Assertions.assertEquals(2, context.get("nextState"));
    }

    @Test
    public void checkFallbackStateChange() throws Exception {
        var state1 = new State(null, c -> {
            c.update("nextState", 2);
            return 2;
        });
        var state2 = new State(null, c -> {
            c.update("nextState", 3);
            return 3;
        });
        var state3 = new State(null, c -> {
            c.update("nextState", 1);
            return 1;
        });
        states.put(1, state1);
        states.put(2, state2);
        states.put(3, state3);
        var data = new HashMap<String, Object>();
        var startState = 1;
        data.put("message", "");
        data.put("nextState", startState);
        var context = new Context(data);
        initConvHandler(startState);
        for (var i = 1; i < 4; i++) {
            convHandler.execute(context);
            Assertions.assertEquals(i % 3 + 1, context.get("nextState"));
        }
    }

    @Test
    public void ifNotNecessaryMessageHandlerUseFallback() throws Exception {
        var data = new HashMap<String, Object>();
        data.put("message", "");
        data.put("cnt", 0);
        var context = new Context(data);
        context.update("cnt", 0);
        var state = new State(null, c -> {
            c.update("cnt", (Integer)c.get("cnt") + 54);
            return 1;
        });
        states.put(1, state);
        initConvHandler(1);

        convHandler.execute(context);
        Assertions.assertEquals(54, context.get("cnt"));

        context.update("message", "Lol");
        convHandler.execute(context);
        Assertions.assertEquals(108, context.get("cnt"));
    }


    private void initConvHandler(Integer startState) throws Exception {
        convHandler = new ConversationHandler(commands, states, startState);
    }
}
