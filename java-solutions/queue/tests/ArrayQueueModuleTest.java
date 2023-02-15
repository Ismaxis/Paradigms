package queue.tests;

import queue.ArrayQueueADT;
import queue.ArrayQueueModule;
import queue.ArrayQueue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        testModule();
        Printer.println();
        testADT();
        Printer.println();
        testClass();
        Printer.println();
    }

    private static void testModule() {
        Printer.println("-- Module Test --");
        Printer.incTab();
        Printer.incTab();
        fillModule(3);
        dumpModule();
        fillModule(10);
        dumpModule(5);
        fillModule(5);
        dumpModule();
        Printer.decTab();
        Printer.decTab();
    }
    private static void testADT() {
        Printer.println("-- ADT Test --");
        Printer.incTab();
        int curIndent = Printer.getIndent();
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        Printer.incTab();
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
        Printer.decTab();
        Printer.decTab();
    }
    private static void testClass() {
        Printer.println("-- Class Test --");
        Printer.incTab();
        int curIndent = Printer.getIndent();
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        Printer.incTab();
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
        Printer.decTab();
        Printer.decTab();
    }

    // Common
    private static void fill(QueueFunctions queueFunctions, int count) {
        for (int i = 0; i < count; i++) {
            queueFunctions.enqueue(i);
        }
        Printer.println(count + " elements added");
    }
    private static void dump(QueueFunctions queueFunctions) {
        Printer.println("Dump all:");
        while(!queueFunctions.isEmpty()) {
            printQueueState(queueFunctions.size(), queueFunctions.element(), queueFunctions.dequeue());
        }
    }
    private static void dump(QueueFunctions queueFunctions, int count) {
        Printer.println("Dump all:");
        for (int i = 0; i < count; i++) {
            printQueueState(queueFunctions.size(), queueFunctions.element(), queueFunctions.dequeue());
        }
    }
    private static void printQueueState(int size, Object element, Object dequeued) {
        Printer.println(
                "size: " + size + "  \tel: " + element + "  \tdequeued: " + dequeued);
    }

    // Module
    private static QueueFunctions functionsForModuleQueue() {
        return new QueueFunctions() {
            @Override
            public void enqueue(Object element) {
                ArrayQueueModule.enqueue(element);
            }

            @Override
            public Object element() {
                return ArrayQueueModule.element();
            }

            @Override
            public Object dequeue() {
                return ArrayQueueModule.dequeue();
            }

            @Override
            public int size() {
                return ArrayQueueModule.size();
            }

            @Override
            public boolean isEmpty() {
                return ArrayQueueModule.isEmpty();
            }
        };
    }
    private static void fillModule(int count) {
        fill(functionsForModuleQueue(), count);
    }
    private static void dumpModule() {
        dump(functionsForModuleQueue());
    }
    private static void dumpModule(int count) {
        dump(functionsForModuleQueue(), count);
    }

    // ADT
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

    // Class
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
