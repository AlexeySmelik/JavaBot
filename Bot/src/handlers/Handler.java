package handlers;

public interface Handler<TSource, TResult> {
    void execute(TSource source);
    TResult apply(TSource source);
}
