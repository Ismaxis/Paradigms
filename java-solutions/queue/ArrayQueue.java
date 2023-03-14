package queue;

import java.util.Objects;

/*
Model:
    a[1]...a[n]
Inv:
    n >= 0 &&
    for i in [1; n]:
        a[i] != null
Let immutable(n):
    for i in [1; n]:
       a'[i] = a[i]
 */

public class ArrayQueue extends AbstractQueue {
    private static final int INITIAL_SIZE = 10;
    private int head;
    private Object[] elements;

    public ArrayQueue() {
        super();
        elements = new Object[INITIAL_SIZE];
        head = 0;
    }

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a[n'] = element &&
    //       immutable(n)
    @Override
    protected void enqueueImpl(Object element) {
        ensureCapacity(size + 1);
        elements[(head + size) % elements.length] = element;
    }

    // Pre: n >= 1
    // Post: R = a[1] &&
    //           n' = n &&
    //           immutable(n)
    @Override
    protected Object elementImpl() {
        return elements[head];
    }

    // Pre: n >= 1
    // Post: R = a[1] &&
    //       n' = n - 1 &&
    //       for i in [1; n']:
    //           a'[i] = a[i + 1]
    @Override
    public Object dequeueImpl() {
        Object result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    // Pre: true
    // Post: n' = 0
    @Override
    protected void clearImpl() {
        head = 0;
        elements = new Object[INITIAL_SIZE];
    }

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a[1] = element &&
    //       for i in [1; n]:
    //           a'[i + 1] = a[i]
    public void push(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);

        head = (elements.length + head - 1) % elements.length;
        elements[head] = element;
        size++;
    }

    // Pre: n >= 1;
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = a[n]
    public Object peek() {
        assert size >= 1;

        return elements[(head + size - 1) % elements.length];
    }

    // Pre: n >= 1;
    // Post: n' = n - 1 &&
    //       immutable(n - 1) &&
    //       R = a[n]
    public Object remove() {
        assert size >= 1;

        int tail = (head + size - 1) % elements.length;
        Object result = elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    // Pre: true
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = b:
    //       for i in [1; n]:
    //           b[i] = a[i]
    public Object[] toArray() {
        // :NOTE: handmade :FIXED:
        return copyOfElements(size);
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            // :NOTE: handmade :FIXED:
            elements = copyOfElements(Math.max(elements.length * 2, capacity));
            head = 0;
        }
    }

    private Object[] copyOfElements(int capacity) {
        final Object[] newArr = new Object[capacity];
        if (head + size <= elements.length) {
            System.arraycopy(elements, head, newArr, 0, size);
        } else {
            final int lengthOfLeftPart = elements.length - head;
            System.arraycopy(elements, head, newArr, 0, lengthOfLeftPart);
            System.arraycopy(elements, 0, newArr, lengthOfLeftPart, size - lengthOfLeftPart);
        }
        return newArr;
    }

    @Override
    protected QueueIterator getIterator() {
        return new ArrayQueueIterator();
    }

    protected class ArrayQueueIterator implements QueueIterator {
        protected int i;
        @Override
        public Object getElement() {
            return elements[(head + i) % elements.length];
        }

        @Override
        public void inc() {
            i++;
        }

        @Override
        public boolean isEnd() {
            return i == size;
        }

        @Override
        public void removeCur() {
            final int length = elements.length;

            if (length <= head + size && head + i < length) {
                System.arraycopy(elements, head, elements, head + 1, i);
                head = (head + 1) % length;
            } else {
                System.arraycopy(elements, (head + i + 1) % length, elements, (head + i) % length, size - i);
            }
        }
    }
}
