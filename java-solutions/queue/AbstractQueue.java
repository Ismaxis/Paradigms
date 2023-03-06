package queue;

import java.util.Objects;

abstract public class AbstractQueue implements Queue {
    protected int size;

    public AbstractQueue() {
        this.size = 0;
    }

    @Override
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
        size++;
    }
    protected abstract void enqueueImpl(Object element);

    @Override
    public Object element() {
        assert size >= 1;
        return elementImpl();
    }
    protected abstract Object elementImpl();

    @Override
    public Object dequeue() {
        assert size >= 1;
        size--;

        return dequeueImpl();
    }
    protected abstract Object dequeueImpl();

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }
    protected abstract void clearImpl();

    @Override
    public boolean contains(Object element) {
        return !guard(element) && find(getIterator(), element);
    }
    @Override
    public boolean removeFirstOccurrence(Object element) {
        if (guard(element)) {
            return false;
        }

        QueueIterator iter = getIterator();
        if (find(iter, element)) {
            iter.removeCur();
            size--;
            return true;
        } else {
            return false;
        }
    }
    protected boolean guard(Object element) {
        return isEmpty() || element == null;
    }
    protected static boolean find(QueueIterator iter, Object element) {
        while (!iter.isEnd()) {
            if (Objects.equals(iter.getElement(), element)) {
                return true;
            }
            iter.inc();
        }
        return false;
    }

    abstract protected QueueIterator getIterator();
    protected interface QueueIterator {
        Object getElement();

        Object getNext();

        void inc();

        boolean isEnd();

        void removeCur();
    }
}
