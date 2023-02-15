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
public class ArrayQueueADT {
    private /* static */ int size;
    private /* static */ int head;
    private /* static */ Object[] elements = new Object[2];

    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);

        queue.elements[(queue.head + queue.size) % queue.elements.length] = element;
        queue.size++;
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
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[queue.head];
    }

    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object result = queue.elements[queue.head];
        queue.size--;
        queue.head = (queue.head + 1) % queue.elements.length;
        return result;
    }

    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }

    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.size = 0;
        queue.head = 0;
    }

    public static void degPrint(ArrayQueueADT queue) {
        System.out.println("--- dbg ---");
        System.out.println("queue.size: " +  queue.size);
        for (Object element : queue.elements) {
            System.out.print((element == null ? "n" : element) + " ");
        }
        System.out.println();

        for (int i = 0; i < queue.elements.length; i++) {
            System.out.print(i == queue.head ? "| " : "  ");
        }
        System.out.println("\n--- end ---\n");
    }
}
