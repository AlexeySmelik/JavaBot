package JavaBot;

import JavaBot.handlers.Context;

import java.util.function.Function;
import java.util.regex.Pattern;

public class MessageHandler {
    private final Function<Context, Integer> action;
    private final String pattern;

    public MessageHandler(String pattern, Function<Context, Integer> action) {
        this.pattern = pattern;
        this.action = action;
    }

    public Boolean is(String str) {
        return Pattern.matches(pattern, str);
    }

    public Integer apply(Context context) {
        return action.apply(context);
    }
}

