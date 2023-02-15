package queue;


import java.util.Arrays;
import java.util.Objects;

// Model:
/*
    enqueue - add to back
    element - look at front
    dequeue - remove from front
    size
    isEmpty - size == 0
    clears
 */
public class ArrayQueue {
    private int size;
    private int head;
    private Object[] elements = new Object[2];

    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);

        elements[(head + size) % elements.length] = element;
        size++;
    }
    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] newArr = Arrays.copyOf(elements, Math.max(elements.length * 2, capacity));
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[(head + i) % elements.length];
            }
            elements = newArr;
            head = 0;
        }
    }
    public Object element() {
        assert size > 0;

        return elements[head];
    }

    public Object dequeue() {
        assert size > 0;

        Object result = elements[head];
        size--;
        head = (head + 1) % elements.length;
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        elements = new Object[2];
        size = 0;
        head = 0;
    }
}
