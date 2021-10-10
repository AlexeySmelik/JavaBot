package JavaBot.handlers;

public interface EventListener<TSource> {
    void update(String eventType, TSource source);
}
