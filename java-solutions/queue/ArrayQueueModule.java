package queue;

import java.util.Objects;

/*
Model:
    a[1]...a[n]
Inv:
    n >= 0 &&
    for i in [1; n]:
        a[i] != null
Let immutable(n):
    for i in [1; n]:
       a'[i] = a[i]
 */

public class ArrayQueueModule {
    private static final int INITIAL_SIZE = 10;
    private static int size;
    private static int head;
    private static Object[] elements = new Object[INITIAL_SIZE];

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a'[n'] = element &&
    //       immutable(n)
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);

        elements[(head + size) % elements.length] = element;
        size++;
    }

    // Pre: n >= 1
    // Post: R = a[1] &&
    //           n' = n &&
    //           immutable(n)
    public static Object element() {
        assert size >= 1;

        return elements[head];
    }

    // Pre: n >= 1
    // Post: R = a[1] &&
    //           n' = n - 1 &&
    //           for i in [1; n']:
    //              a[i] = a[i + 1]
    public static Object dequeue() {
        assert size >= 1;

        Object result = elements[head];
        elements[head] = null;
        size--;
        head = (head + 1) % elements.length;
        return result;
    }

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a[1] = element &&
    //       for i in [1; n]:
    //          a'[i + 1] = a[i]
    public static void push(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);

        head = (elements.length + head - 1) % elements.length;
        elements[head] = element;
        size++;
    }

    // Pre: n >= 1;
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = a[n]
    public static Object peek() {
        assert size >= 1;

        return elements[(head + size - 1) % elements.length];
    }

    // Pre: n >= 1;
    // Post: n' = n - 1 &&
    //       immutable(n - 1) &&
    //       R = a[n]
    public static Object remove() {
        assert size >= 1;

        int tail = (head + size - 1) % elements.length;
        Object result = elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    // Pre: true
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = b:
    //       for i in [1; n]:
    //           b[i] = a[i]
    public static Object[] toArray() {
        return copyOfElements(size);
    }

    // Pre: true
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = n
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = (n == 0)
    public static boolean isEmpty() {
        return size() == 0;
    }

    // Pre: true
    // Post: n' = 0
    public static void clear() {
        elements = new Object[INITIAL_SIZE];
        size = 0;
        head = 0;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = copyOfElements(Math.max(elements.length * 2, capacity));
            head = 0;
        }
    }

    private static Object[] copyOfElements(int capacity) {
        final Object[] newArr = new Object[capacity];
        if (head + size <= elements.length) {
            System.arraycopy(elements, head, newArr, 0, size);
        } else {
            final int lengthOfLeftPart = elements.length - head;
            System.arraycopy(elements, head, newArr, 0, lengthOfLeftPart);
            System.arraycopy(elements, 0, newArr, lengthOfLeftPart, size - lengthOfLeftPart);
        }
        return newArr;
    }

}
