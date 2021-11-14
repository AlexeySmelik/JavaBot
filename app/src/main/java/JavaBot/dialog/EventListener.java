package JavaBot.dialog;

public interface EventListener<TSource> {
    void update(String eventType, TSource source);
}
