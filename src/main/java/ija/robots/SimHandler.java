package ija.robots;

/**
 * Simple event handler.
 */
public interface SimHandler<T> {
    /**
     * Invokes the event handler.
     * @param data Data from the event.
     */
    public void invoke(T data);
}
