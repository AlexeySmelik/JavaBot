package JavaBot.dialog;

import java.util.List;
import java.util.function.Function;

public class State {
    private final List<MessageHandler> handlers;
    private final Function<Context, Integer> fallback;

    public State(List<MessageHandler> handlers, Function<Context, Integer> fallback) {
        this.handlers = handlers;
        this.fallback = fallback;
    }

    public List<MessageHandler> getHandlers() {
        return handlers;
    }

    public Function<Context, Integer> getFallback() {
        return fallback;
    }
}

