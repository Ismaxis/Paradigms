package queue.tests;

import java.util.Objects;

abstract public class ArrayQueueTester {
    abstract public void test();
    protected static ContractFailure error(String methodName) {
        return new ContractFailure(methodName);
    }
    protected static ContractFailure error(String methodName, String description) {
        return new ContractFailure(methodName, description);
    }

    public void testEnqueue(QueueFunctions qf) {
        Object[] elements = {1, "abacaba", 5.5};

        int expectedSize = qf.size() + 1;
        qf.enqueue(elements[0]);

        compare(qf.size(), expectedSize,"enqueue(Object)", "n' = n + 1");
        qf.clear();

        fillQueue(qf, elements);
        qf.enqueue("Test last element, that shouldn't be dequeued");

        checkImmutable(elements, qf, "enqueue(Object)", "immutable(n)");
    }
    public void testClear(QueueFunctions qf) {
        for (int i = 0; i < 5; i++) {
            qf.enqueue(2 * i);
        }
        qf.clear();
        compare(qf.size(), 0, "clear()", "n' = 0");

        Integer testElem = 88;
        qf.enqueue(testElem);
        Object head = qf.dequeue();
        compare(head, testElem, "clear()","Queue contains elements after clear");
    }
    public void testDequeue(QueueFunctions qf) {
        Object[] elements = {"acabaca", 2.7, 100, "___"};

        qf.enqueue(elements[0]);
        int expectedSize = qf.size() - 1;
        qf.dequeue();

        compare(qf.size(), expectedSize,"dequeue()", "n' = n - 1");

        String head = "First element to be dequeued";
        String head2 = "Second element to be dequeued";
        qf.enqueue(head);
        qf.enqueue(head2);
        fillQueue(qf, elements);

        compare(qf.dequeue(), head, "dequeue()", "R = a[1]");
        compare(qf.dequeue(), head2, "dequeue()", "a'[1] = a[2]");

        checkImmutable(elements, qf, "dequeue()", "for i in [1; n']: a'[i] = a[i + 1]");
    }
    public void testElement(QueueFunctions qf) {
        Object[] elements = {3.14, "^_^", 200, "(#_#)"};

        qf.enqueue(elements[0]);
        int expectedSize = qf.size();
        qf.element();

        compare(qf.size(), expectedSize,"element()", "n' = n");
        qf.clear();

        String head = "Element to be dequeued";
        qf.enqueue(head);
        fillQueue(qf, elements);

        compare(qf.element(), head, "element()", "R = a[1]");
        qf.dequeue();

        checkImmutable(elements, qf, "element()", "immutable(n)");
    }
    public void testSize(QueueFunctions qf) {
        compare(qf.size(), 0, "size()", "Initial size should be 0");
        Object[] elements = {"/_/", "--_--", 1020, 0};

        qf.enqueue(elements[0]);
        int expectedSize = qf.size();
        qf.size();

        compare(qf.size(), expectedSize,"size()", "n' = n");
        qf.clear();

        fillQueue(qf, elements);

        expectedSize = qf.size();
        // Immutable check
        for (Object element : elements) {
            compare(qf.dequeue(), element, "size()","immutable(n)");
            compare(qf.size(), --expectedSize, "size()","R = n");
        }
    }
    public void testEmpty(QueueFunctions qf) {
        Object[] elements = {"-_-", 202, "(@_@)", 1000};

        qf.enqueue(elements[0]);
        int expectedSize = qf.size();
        qf.isEmpty();

        // Size check
        compare(qf.size(), expectedSize,"isEmpty()", "n' = n");
        qf.clear();

        fillQueue(qf, elements);
        qf.isEmpty();

        // Immutable check
        for (Object element : elements) {
            compare(qf.dequeue(), element, "isEmpty()","immutable(n)");
            compare(qf.isEmpty(), qf.size() == 0, "isEmpty()","R = (n == 0)");
        }
    }
    public void testPush(QueueFunctions qf) {
        Object[] elements = {"abba", 36.6, 12301823};
        Object[] elementsWithFirst = {"Test first element, that should be added",
                                    "abba", 36.6, 12301823};

        int expectedSize = qf.size() + 1;
        qf.push(elements[0]);

        compare(qf.size(), expectedSize,"push(Object)", "n' = n + 1");
        qf.clear();

        fillQueue(qf, elements);
        qf.push(elementsWithFirst[0]);

        checkImmutable(elementsWithFirst, qf, "push(Object)", "a[1] = element && for i in [1; n]: a'[i + 1] = a[i]");
    }
    public void testRemove(QueueFunctions qf) {
        Object[] elements = {"abba", 36.6, 12301823};
        Object[] elementsWithLast = {"abba", 36.6, 12301823,
                "Test last element, that should be removed"};

        qf.enqueue(elements[0]);
        int expectedSize = qf.size() - 1;
        qf.remove();

        compare(qf.size(), expectedSize,"remove()", "n' = n - 1");

        fillQueue(qf, elementsWithLast);

        compare(qf.remove(), elementsWithLast[elementsWithLast.length - 1], "remove()", "R = a[n]");

        checkImmutable(elements, qf, "remove()", "immutable(n - 1)]");
    }
    public void testPeek(QueueFunctions qf) {
        Object[] elements = {3.14, "^_^", 200, "(#_#)"};

        qf.enqueue(elements[0]);
        int expectedSize = qf.size();
        qf.element();

        compare(qf.size(), expectedSize,"peek()", "n' = n");
        qf.clear();

        fillQueue(qf, elements);
        String tail = "Element to be dequeued";
        qf.enqueue(tail);

        compare(qf.peek(), tail, "peek()", "R = a[n]");
        qf.remove();

        checkImmutable(elements, qf, "peek()", "immutable(n)");
    }
    public void testToArray(QueueFunctions qf) {
        Object[] elements = {"abba", 36.6, 12301823, "-_-", 202, "(@_@)", 1000};
        fillQueue(qf, elements);

        int expectedSize = qf.size();
        Object[] elementsFromToArray = qf.toArray();

        compare(qf.size(), expectedSize, "toArray()", "n' = n");

        if (elements.length != elementsFromToArray.length) {
            throw error("toArray()",
                    "Wrong length. Expected: " + elements.length + ", found: " + elementsFromToArray.length);
        }
        for (int i = 0; i < elements.length; i++) {
            compare(elementsFromToArray[i], elements[i], "toArray()", "b[i] = a[i]");
        }

        checkImmutable(elements, qf, "toArray()", "immutable(n)");
    }
    private static void fillQueue(QueueFunctions qf, Object[] elements) {
        for (Object element : elements) {
            qf.enqueue(element);
        }
    }
    private static void checkImmutable(Object[] elements, QueueFunctions qf, String methodName, String partOfContract) {
        for (Object element : elements) {
            compare(qf.dequeue(), element, methodName,
                    partOfContract);
        }
    }
    private static void compare(Object found, Object expected, String methodName, String partOfContract) {
        if (!Objects.equals(expected, found)) {
            throw error(methodName, partOfContract + " \nExpected: " + expected + ", found: " + found);
        }
    }
}
