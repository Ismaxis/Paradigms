package queue.tests;

import queue.ArrayQueueADT;

public class ArrayQueueADTTester extends ArrayQueueTester {

    @Override
    public void test() {
        Printer.println("-- ADT Test --");
        Printer.incTab();
        Printer.println("Tested:");
        Printer.incTab();

        testClear(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("clear()");

        testEnqueue(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("enqueue(Object)");

        testDequeue(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("dequeue()");

        testElement(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("element()");

        testSize(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("size()");

        testEmpty(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("isEmpty()");

        testPush(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("push(Object)");

        testRemove(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("remove()");

        testPeek(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("peek()");

        testToArray(functionsForADTQueue(ArrayQueueADT.create()));
        Printer.println("toArray()");

        Printer.decTab(2);
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
}
