package queue.tests;

import queue.ArrayQueueADT;

public class ArrayQueueADTTester extends ArrayQueueTester {
    @Override
    public void test() {
        Printer.println("-- ADT Test --");
        Printer.incTab();

        ArrayQueueADT queue = ArrayQueueADT.create();
        testEmpty(functionsForADTQueue(queue));
        Printer.println("Tested: isEmpty()");

        queue = ArrayQueueADT.create();
        testSize(functionsForADTQueue(queue));
        Printer.println("Tested: size()");

        queue = ArrayQueueADT.create();
        testSingleton(functionsForADTQueue(queue));

        queue = ArrayQueueADT.create();
        testClear(functionsForADTQueue(queue));
        Printer.println("Tested: clear()");

        queue = ArrayQueueADT.create();
        testOrder(functionsForADTQueue(queue));
        Printer.println("Tested: First In First Out");

        queue = ArrayQueueADT.create();
        testOrderRandom(functionsForADTQueue(queue));
        Printer.println("Tested: Random Inputs");

        queue = ArrayQueueADT.create();
        testOrderRandomDeque(functionsForADTQueue(queue));
        Printer.println("Tested: Random Inputs DequeToArray");

        Printer.println("Some enqueues and dequeues:");
        Printer.incTab();
        int curIndent = Printer.getIndent();
        Printer.incTab();

        ArrayQueueADT queue1 = ArrayQueueADT.create();
        ArrayQueueADT queue2 = ArrayQueueADT.create();
        Printer.println("q1:", curIndent);
        fillADT(queue1, 3);
        Printer.println("q2:", curIndent);
        fillADT(queue2, 3);
        Printer.println("q1:", curIndent);
        dumpADT(queue1);
        Printer.println("q1:", curIndent);
        fillADT(queue1, 10);
        Printer.println("q1:", curIndent);
        dumpADT(queue1, 5);
        Printer.println("q1:", curIndent);
        fillADT(queue1, 5);
        Printer.println("q1:", curIndent);
        dumpADT(queue1);
        Printer.println("q2:", curIndent);
        dumpADT(queue2);

        Printer.decTab(3);
    }

    private static QueueFunctions functionsForADTQueue(ArrayQueueADT queue) {
        return new QueueFunctions() {
            @Override
            public void enqueue(Object element) {
                ArrayQueueADT.enqueue(queue, element);
            }

            @Override
            public Object element() {
                return ArrayQueueADT.element(queue);
            }

            @Override
            public Object dequeue() {
                return ArrayQueueADT.dequeue(queue);
            }

            @Override
            public int size() {
                return ArrayQueueADT.size(queue);
            }

            @Override
            public boolean isEmpty() {
                return ArrayQueueADT.isEmpty(queue);
            }

            @Override
            public void clear() {
                ArrayQueueADT.clear(queue);
            }

            @Override
            public void push(Object element) {
                ArrayQueueADT.push(queue, element);
            }

            @Override
            public Object peek() {
                return ArrayQueueADT.peek(queue);
            }

            @Override
            public Object remove() {
                return ArrayQueueADT.remove(queue);
            }

            @Override
            public Object[] toArray() {
                return ArrayQueueADT.toArray(queue);
            }
        };
    }
    private static void fillADT(ArrayQueueADT queue, int count) {
        fill(functionsForADTQueue(queue), count);
    }
    private static void dumpADT(ArrayQueueADT queue) {
        dump(functionsForADTQueue(queue));
    }
    private static void dumpADT(ArrayQueueADT queue, int count) {
        dump(functionsForADTQueue(queue), count);
    }
}
