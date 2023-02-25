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
}
