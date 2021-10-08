package JavaBot;

public class ConversationListener implements EventListener<Context> {
    private final ConversationHandler handler;

    public ConversationListener(ConversationHandler handler) {
        this.handler = handler;
    }

    public void update(String eventType, Context context) {
        if (eventType.equals("updateMessage"))
            handler.execute(context);
    }
}

