package queue;


import java.util.Arrays;
import java.util.Objects;

/*
Model:
    a[1]...a[n]
Inv:
    n >= 0 &&
    for i in [1; n]:
        a[i] != null
Let immutable(queue, n):
    for i in [1; n]:
       queue.a'[i] = queue.a[i]
 */

public class ArrayQueue {
    private static final int INITIAL_SIZE = 5;
    private int size;
    private int head;
    private Object[] elements;

    public ArrayQueue() {
        elements = new Object[INITIAL_SIZE];
        size = 0;
        head = 0;
    }

    // Pre: element != null
    // Post: this.n' = this.n + 1 &&
    //       this.a[this.n'] = element &&
    //       immutable(this, this.n)
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);

        elements[(head + size) % elements.length] = element;
        size++;
    }

    // Pre: this.n >= 1
    // Post: R = this.a[1] &&
    //           this.n' = this.n &&
    //           immutable(queue, this.n)
    public Object element() {
        assert size >= 1;

        return elements[head];
    }

    // Pre: this.n >= 1
    // Post: R = this.a[1] &&
    //           this.n' = this.n - 1 &&
    //           for i in [1; this.n']:
    //              this.a'[i] = this.a[i + 1]
    public Object dequeue() {
        assert size >= 1;

        Object result = elements[head];
        elements[head] = null;
        size--;
        head = (head + 1) % elements.length;
        return result;
    }

    // Pre: true
    // Post: this.n' = this.n &&
    //       immutable(queue, this.n) &&
    //       R = this.n
    public int size() {
        return size;
    }

    // Pre: true
    // Post: this.n' = this.n &&
    //       immutable(queue, this.n) &&
    //       R = (this.n == 0)
    public boolean isEmpty() {
        return size() == 0;
    }

    // Pre: true
    // Post: this.n' = 0
    public void clear() {
        elements = new Object[INITIAL_SIZE];
        size = 0;
        head = 0;
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
}
