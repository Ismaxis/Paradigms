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

public class ArrayQueueADT {
    private static final int INITIAL_SIZE = 5;
    private int size;
    private int head;
    private Object[] elements = new Object[INITIAL_SIZE];

    // Pre: queue != null && element != null
    // Post: queue.n' = queue.n + 1 &&
    //       queue.a[queue.n'] = element &&
    //       immutable(queue, queue.n)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);

        queue.elements[(queue.head + queue.size) % queue.elements.length] = element;
        queue.size++;
    }

    // Pre: queue != null && queue.n >= 1
    // Post: R = queue.a[1] &&
    //           queue.n' = queue.n &&
    //           immutable(queue, queue.n)
    public static Object element(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size >= 1;

        return queue.elements[queue.head];
    }

    // Pre: queue != null && queue.n >= 1
    // Post: R = queue.a[1] &&
    //           queue.n' = queue.n - 1 &&
    //           for i in [1; queue.n']:
    //              queue.a'[i] = queue.a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size >= 1;

        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.size--;
        queue.head = (queue.head + 1) % queue.elements.length;
        return result;
    }

    // Pre: queue != null
    // Post: queue.n' = queue.n &&
    //       immutable(queue, queue.n) &&
    //       R = queue.n
    public static int size(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size;
    }

    // Pre: queue != null
    // Post: queue.n' = queue.n &&
    //       immutable(queue, queue.n) &&
    //       R = (queue.n == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return size(queue) == 0;
    }

    // Pre: queue != null
    // Post: queue.n' = 0
    public static void clear(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);

        queue.elements = new Object[INITIAL_SIZE];
        queue.size = 0;
        queue.head = 0;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            Object[] newArr = Arrays.copyOf(queue.elements, Math.max(queue.elements.length * 2, capacity));
            for (int i = 0; i < queue.size; i++) {
                newArr[i] = queue.elements[(queue.head + i) % queue.elements.length];
            }
            queue.elements = newArr;
            queue.head = 0;
        }
    }
}
