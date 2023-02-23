package queue.tests;

import queue.ArrayQueueModule;

public class ArrayQueueModuleTester extends ArrayQueueTester {
    @Override
    public void test() {
        Printer.println("-- Module Test --");
        Printer.incTab();
        Printer.println("Tested:");
        Printer.incTab();

        QueueFunctions qf = functionsForModuleQueue();
        testClear(qf);
        Printer.println("clear()");

        ArrayQueueModule.clear();
        testEnqueue(qf);
        Printer.println("enqueue(Object)");

        ArrayQueueModule.clear();
        testDequeue(qf);
        Printer.println("dequeue()");

        ArrayQueueModule.clear();
        testElement(qf);
        Printer.println("element()");

        ArrayQueueModule.clear();
        testSize(qf);
        Printer.println("size()");

        ArrayQueueModule.clear();
        testEmpty(qf);
        Printer.println("isEmpty()");

        ArrayQueueModule.clear();
        testPush(qf);
        Printer.println("push(Object)");

        ArrayQueueModule.clear();
        testRemove(qf);
        Printer.println("remove()");

        ArrayQueueModule.clear();
        testPeek(qf);
        Printer.println("peek()");

        ArrayQueueModule.clear();
        testToArray(qf);
        Printer.println("toArray()");

        Printer.decTab(2);
    }
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

            @Override
            public void clear() {
                ArrayQueueModule.clear();
            }

            @Override
            public void push(Object element) {
                ArrayQueueModule.push(element);
            }

            @Override
            public Object peek() {
                return ArrayQueueModule.peek();
            }

            @Override
            public Object remove() {
                return ArrayQueueModule.remove();
            }

            @Override
            public Object[] toArray() {
                return ArrayQueueModule.toArray();
            }
        };
    }
}
