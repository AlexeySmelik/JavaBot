import handlers.Context;
import handlers.ConversationHandler;
import handlers.MessageHandler;
import handlers.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashMap;

public class TestsConversationHandler {
    @Test
    public void SimpleTest () {
        var handlers = new ArrayList<MessageHandler>();
        handlers.add(new MessageHandler("start", c -> {
            c.set("counter", (Integer)c.get("counter") + 1);
            return 1;
        }));
        var state = new State(handlers, c -> {
            c.set("counter", (Integer)c.get("counter") + 2);
            return 1;
        });
        var states = new HashMap<Integer, State>();
        states.put(1, state);
        var data = new HashMap<String, Object>();
        data.put("counter", 0);
        var context = new Context(1, data);
        try(var convHandler = new ConversationHandler(null, states, 1)) {
            context.changeMessage("Lol");
            convHandler.execute(context);
            Assertions.assertEquals(2, context.get("counter"));
            context.changeMessage("start");
            convHandler.execute(context);
            Assertions.assertEquals(3, context.get("counter"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
