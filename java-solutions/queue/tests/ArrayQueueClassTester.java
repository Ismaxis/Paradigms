package queue.tests;

import queue.ArrayQueue;

public class ArrayQueueClassTester extends ArrayQueueTester {
    @Override
    public void test() {
        Printer.println("-- Class Test --");
        Printer.incTab();

        ArrayQueue queue = new ArrayQueue();
        testEmpty(functionsForClassQueue(queue));
        Printer.println("Tested: isEmpty()");

        queue = new ArrayQueue();
        testSize(functionsForClassQueue(queue));
        Printer.println("Tested: size()");

        queue = new ArrayQueue();
        testSingleton(functionsForClassQueue(queue));

        queue = new ArrayQueue();
        testClear(functionsForClassQueue(queue));
        Printer.println("Tested: clear()");

        queue = new ArrayQueue();
        testOrder(functionsForClassQueue(queue));
        Printer.println("Tested: First In First Out");

        queue = new ArrayQueue();
        testOrderRandom(functionsForClassQueue(queue));
        Printer.println("Tested: Random Inputs");

        queue = new ArrayQueue();
        testOrderRandomDeque(functionsForClassQueue(queue));
        Printer.println("Tested: Random Inputs DequeToArray");

        Printer.println("Some enqueues and dequeues:");
        Printer.incTab();
        int curIndent = Printer.getIndent();
        Printer.incTab();

        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        Printer.println("q1:", curIndent);
        fillClass(queue1, 3);
        Printer.println("q2:", curIndent);
        fillClass(queue2, 3);
        Printer.println("q1:", curIndent);
        dumpClass(queue1);
        Printer.println("q1:", curIndent);
        fillClass(queue1, 10);
        Printer.println("q1:", curIndent);
        dumpClass(queue1, 5);
        Printer.println("q1:", curIndent);
        fillClass(queue1, 5);
        Printer.println("q1:", curIndent);
        dumpClass(queue1);
        Printer.println("q2:", curIndent);
        dumpClass(queue2);

        Printer.decTab(3);
    }

    private static QueueFunctions functionsForClassQueue(ArrayQueue queue) {
        return new QueueFunctions() {
            @Override
            public void enqueue(Object element) {
                queue.enqueue(element);
            }

            @Override
            public Object element() {
                return queue.element();
            }

            @Override
            public Object dequeue() {
                return queue.dequeue();
            }

            @Override
            public int size() {
                return queue.size();
            }

            @Override
            public boolean isEmpty() {
                return queue.isEmpty();
            }

            @Override
            public void clear() {
                queue.clear();
            }

            @Override
            public void push(Object element) {
                queue.push(element);
            }

            @Override
            public Object peek() {
                return queue.peek();
            }

            @Override
            public Object remove() {
                return queue.remove();
            }

            @Override
            public Object[] toArray() {
                return queue.toArray();
            }
        };
    }
    private static void fillClass(ArrayQueue queue, int count) {
        fill(functionsForClassQueue(queue), count);
    }
    private static void dumpClass(ArrayQueue queue) {
        dump(functionsForClassQueue(queue));
    }
    private static void dumpClass(ArrayQueue queue, int count) {
        dump(functionsForClassQueue(queue), count);
    }
}
