package JavaBot;

public interface EventManager<TSource> {
    void add(String eventType, EventListener listener);
    void remove(String eventType, EventListener listener);
    void notify(String eventType, TSource source);
}
