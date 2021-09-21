package handlers;

import data.Context;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;

public class MessageHandler {
    private final Function<Context, Integer> action;
    private final ArrayList<String> patterns;

    public MessageHandler(String pattern, Function<Context, Integer> action) {
        patterns = new ArrayList<>();
        patterns.add(pattern);
        this.action = action;
    }

    public MessageHandler(ArrayList<String> patterns, Function<Context, Integer> action) {
        this.patterns = patterns;
        this.action = action;
    }

    public Boolean is(String str) {
        for (var pattern : patterns)
            if (Pattern.matches(pattern, str))
                return true;
        return false;
    }

    public Integer apply(Object c) {
        return action.apply((Context) c);
    }
}
