package queue.tests;

public interface QueueFunctions {
    void enqueue(Object element);
    Object element();
    Object dequeue();
    void push(Object element);
    Object peek();
    Object remove();
    Object[] toArray();
    int size();
    boolean isEmpty();
    void clear();
}