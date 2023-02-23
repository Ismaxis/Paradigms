package queue.tests;

import queue.ArrayQueue;

public class ArrayQueueClassTester extends ArrayQueueTester {
    @Override
    public void test() {
        Printer.println("-- Class Test --");
        Printer.incTab();
        Printer.println("Tested:");
        Printer.incTab();

        testClear(functionsForClassQueue(new ArrayQueue()));
        Printer.println("clear()");

        testEnqueue(functionsForClassQueue(new ArrayQueue()));
        Printer.println("enqueue(Object)");

        testDequeue(functionsForClassQueue(new ArrayQueue()));
        Printer.println("dequeue()");

        testElement(functionsForClassQueue(new ArrayQueue()));
        Printer.println("element()");

        testSize(functionsForClassQueue(new ArrayQueue()));
        Printer.println("size()");

        testEmpty(functionsForClassQueue(new ArrayQueue()));
        Printer.println("isEmpty()");

        testPush(functionsForClassQueue(new ArrayQueue()));
        Printer.println("push(Object)");

        testRemove(functionsForClassQueue(new ArrayQueue()));
        Printer.println("remove()");

        testPeek(functionsForClassQueue(new ArrayQueue()));
        Printer.println("peek()");

        testToArray(functionsForClassQueue(new ArrayQueue()));
        Printer.println("toArray()");

        Printer.decTab();
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
}
