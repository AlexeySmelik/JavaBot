package JavaBot.handlers;

public interface EventManager<TSource> {
    void add(String eventType, EventListener<TSource> listener);
    void remove(String eventType, EventListener<TSource> listener);
    void notify(String eventType, TSource source);
}
