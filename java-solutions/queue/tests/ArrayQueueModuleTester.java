package queue.tests;

import queue.ArrayQueueModule;

public class ArrayQueueModuleTester extends ArrayQueueTester {
    @Override
    public void test() {
        Printer.println("-- Module Test --");
        Printer.incTab(2);

        QueueFunctions qf = functionsForModuleQueue();
        testClear(qf);
        Printer.println("Tested: clear()");

        ArrayQueueModule.clear();
        testEmpty(qf);
        Printer.println("Tested: isEmpty()");

        ArrayQueueModule.clear();
        testSize(qf);
        Printer.println("Tested: size()");

        ArrayQueueModule.clear();
        testSingleton(qf);
        
        testOrder(qf);
        Printer.println("Tested: First In First Out");

        ArrayQueueModule.clear();
        testOrderRandom(qf);
        Printer.println("Tested: Random Inputs");

        Printer.println("Some enqueues and dequeues:");
        Printer.incTab();

        fillModule(3);
        dumpModule();
        fillModule(10);
        dumpModule(5);
        fillModule(5);
        dumpModule();

        Printer.decTab(3);
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
}
