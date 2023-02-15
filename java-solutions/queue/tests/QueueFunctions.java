package queue.tests;

public interface QueueFunctions {
    void enqueue(Object element);
    Object element();
    Object dequeue();
    int size();
    boolean isEmpty();
}