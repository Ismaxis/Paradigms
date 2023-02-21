package queue.tests;

import java.util.ArrayDeque;
import java.util.Random;

abstract public class ArrayQueueTester {
    abstract public void test();

    protected static TestFailedException error(String message) {
        return new TestFailedException(message);
    }

    public void testEmpty(QueueFunctions queueFunctions) {
        if (!queueFunctions.isEmpty()) {
            throw error("Queue is not empty initially");
        }

        int elementsAdded = 3;
        for (int i = 0; i < elementsAdded; i++) {
            queueFunctions.enqueue(100 * i);
        }
        if (queueFunctions.isEmpty()) {
            throw error("Queue is empty after filled");
        }

        for (int i = 0; i < elementsAdded - 1; i++) {
            queueFunctions.dequeue();
        }
        if (queueFunctions.isEmpty()) {
            throw error("Queue is empty after partial dump");
        }

        queueFunctions.dequeue();
        if (!queueFunctions.isEmpty()) {
            throw error("Queue is not empty after full dump");
        }
    }
    public void testSize(QueueFunctions queueFunctions) {
        if (queueFunctions.size() != 0) {
            throw error("Queue size is not zero initially");
        }

        int maxSize = 100;
        for (int i = 0; i < maxSize; i++) {
            queueFunctions.enqueue(100 * i);
            if (queueFunctions.size() != i + 1) {
                throw error("Queue size is incorrect after enqueue");
            }
        }

        for (int i = 0; i < maxSize - 1; i++) {
            queueFunctions.dequeue();
            if (queueFunctions.size() != maxSize - i - 1) {
                throw error("Queue size is incorrect after dequeue");
            }
        }
        if (queueFunctions.size() != 1) {
            throw error("Queue size is incorrect after partial dump");
        }

        queueFunctions.dequeue();
        if (queueFunctions.size() != 0) {
            throw error("Queue size is not zero after full dump");
        }
    }
    public void testSingleton(QueueFunctions queueFunctions) {
        queueFunctions.enqueue(0);
        Object head1 = queueFunctions.element();
        Object head2 = queueFunctions.element();
        if (head1 != head2) {
            throw error("Two head elements are not the same");
        }

        queueFunctions.enqueue(1);

        head2 = queueFunctions.dequeue();
        if (head1 != head2) {
            throw error("Two head elements are not the same after enqueue");
        }

        head2 = queueFunctions.dequeue();
        if (head1 == head2) {
            throw error("Two head elements are same after dequeue");
        }
    }
    public void testClear(QueueFunctions queueFunctions) {
        for (int i = 0; i < 5; i++) {
            queueFunctions.enqueue(2 * i);
        }
        queueFunctions.clear();
        if (!queueFunctions.isEmpty()) {
            throw error("Queue is not empty after clear");
        }
        if (queueFunctions.size() != 0) {
            throw error("Queue size is not zero after clear");
        }

        Integer testElem = 88;
        queueFunctions.enqueue(testElem);
        Object head = queueFunctions.dequeue();
        if (head != testElem) {
            throw error("Queue contains elements after clear. Expected: '" + testElem + "', found: '" + head + "'");
        }
    }
    public void testOrder(QueueFunctions queueFunctions) {
        Integer[] array = {25, 67, 20, 10};
        for (int i = 0; i < array.length; i++) {
            queueFunctions.enqueue(array[i]);
        }
        for (int i = 0; i < array.length; i++) {
            Object head =  queueFunctions.dequeue();
            if (array[i] != head) {
                throw error("Wrong order: Expected: '" + array[i] + "', found: '" + head + "'" );
            }
        }
    }
    public void testOrderRandom(QueueFunctions queueFunctions) {
        ArrayDeque<Integer> reference = new ArrayDeque<>();
        int iterations = 10000000;
        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            int op = random.nextInt() % 5;
            if (op == 0) {
                int newElem = random.nextInt();
                queueFunctions.enqueue(newElem);
                reference.add(newElem);
            } else if (op == 1) {
                if (reference.isEmpty()) {
                    continue;
                }
                Object testDeq = queueFunctions.dequeue();
                Integer referenceDeq = reference.poll();
                if (testDeq instanceof Integer integer && referenceDeq != null) {
                    if (integer != referenceDeq.intValue()) {
                        throw error("Wrong order: Expected: '" + referenceDeq + "', found: '" + testDeq + "'" );
                    }
                } else {
                    throw error("Wrong type: Expected: " + (referenceDeq == null ? "null" : referenceDeq.getClass()) +
                            ", found: " + (testDeq == null ? "null" : testDeq.getClass()));
                }
            } else if (op == 2) {
                if (queueFunctions.isEmpty() != reference.isEmpty()) {
                    if (reference.isEmpty()) {
                        throw error("Queue is not empty, but should");
                    } else {
                        throw error("Queue is empty, but should not");
                    }
                }
            } else if (op == 3) {
                int testSize = queueFunctions.size();
                int referenceSize = reference.size();
                if (testSize != referenceSize) {
                    throw error("Queue has incorrect size: Expected: " + referenceSize + ", found: " + testSize);
                }
            } else if (op == 4) {
                queueFunctions.clear();
                reference.clear();
            }
        }
    }
    public void testOrderRandomDeque(QueueFunctions queueFunctions) {
        ArrayDeque<Integer> reference = new ArrayDeque<>();
        int iterations = 10000000;
        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            int op = random.nextInt() % 10;
            if (op == 0) {
                int newElem = random.nextInt();
                queueFunctions.enqueue(newElem);
                reference.add(newElem);
            } else if (op == 1) {
                if (reference.isEmpty()) {
                    continue;
                }
                Object testDeq = queueFunctions.dequeue();
                Integer referenceDeq = reference.poll();
                compare(testDeq, referenceDeq);
            } else if (op == 2) {
                if (queueFunctions.isEmpty() != reference.isEmpty()) {
                    if (reference.isEmpty()) {
                        throw error("Queue is not empty, but should");
                    } else {
                        throw error("Queue is empty, but should not");
                    }
                }
            } else if (op == 3) {
                int testSize = queueFunctions.size();
                int referenceSize = reference.size();
                if (testSize != referenceSize) {
                    throw error("Queue has incorrect size: Expected: " + referenceSize + ", found: " + testSize);
                }
            } else if (op == 4) {
                queueFunctions.clear();
                reference.clear();
            } else if (op == 5) {
                int newElem = random.nextInt();
                queueFunctions.push(newElem);
                reference.push(newElem);
            } else if (op == 6) {
                if (reference.isEmpty()) {
                    continue;
                }
                Object testDeq = queueFunctions.remove();
                Integer referenceDeq = reference.removeLast();
                compare(testDeq, referenceDeq);
            } else if (op == 7) {
                if (reference.isEmpty()) {
                    continue;
                }
                Object testDeq = queueFunctions.peek();
                Integer referenceDeq = reference.getLast();
                compare(testDeq, referenceDeq);
            } else if (op == 8) {
                if (reference.isEmpty()) {
                    continue;
                }
                Object testDeq = queueFunctions.element();
                Integer referenceDeq = reference.element();
                compare(testDeq, referenceDeq);
            } else if (op == 9) {
                Object[] testArr = queueFunctions.toArray();
                Object[] referenceArr = reference.toArray();
                if (testArr.length != referenceArr.length) {
                    throw  error("Array has wrong length: Expected: " + referenceArr.length + ", found: " + testArr.length);
                }

                for (int j = 0; j < referenceArr.length; j++) {
                    compare(testArr[j], (Integer) referenceArr[j]);
                }

            }
        }
    }

    private static void compare(Object testDeq, Integer referenceDeq) {
        if (testDeq instanceof Integer integer && referenceDeq != null) {
            if (integer != referenceDeq.intValue()) {
                throw error("Wrong order: Expected: '" + referenceDeq + "', found: '" + testDeq + "'" );
            }
        } else {
            throw error("Wrong type: Expected: " + (referenceDeq == null ? "null" : referenceDeq.getClass()) +
                    ", found: " + (testDeq == null ? "null" : testDeq.getClass()));
        }
    }

    protected static void fill(QueueFunctions queueFunctions, int count) {
        for (int i = 0; i < count; i++) {
            queueFunctions.enqueue(i);
        }
        Printer.println(count + " elements added");
    }
    protected static void dump(QueueFunctions queueFunctions) {
        Printer.println("Dump all:");
        Printer.incTab();
        while(!queueFunctions.isEmpty()) {
            printQueueState(queueFunctions.size(), queueFunctions.element(), queueFunctions.dequeue());
        }
        Printer.decTab();
    }
    protected static void dump(QueueFunctions queueFunctions, int count) {
        Printer.printf("Dump %d:\n", count);
        Printer.incTab();
        for (int i = 0; i < count; i++) {
            printQueueState(queueFunctions.size(), queueFunctions.element(), queueFunctions.dequeue());
        }
        Printer.decTab();
    }
    protected static void printQueueState(int size, Object element, Object dequeued) {
        Printer.println(
                "size: " + size + "  \telement: " + element + "  \tdequeued: " + dequeued);
    }
}
