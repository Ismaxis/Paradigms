package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    public LinkedQueue() {
        super();
        this.head = null;
        this.tail = null;
    }
    @Override
    public void enqueueImpl(Object element) {
        Node newTail = new Node(element);
        if (isEmpty()) {
            head = newTail;
        } else {
            tail.setNext(newTail);
        }
        tail = newTail;
    }
    @Override
    public Object elementImpl() {
        return head.getElement();
    }
    @Override
    public Object dequeueImpl() {
        Node curHead = head;
        head = head.getNext();
        if (isEmpty()) {
            tail = null;
        }
        return curHead.getElement();
    }
    @Override
    public void clearImpl() {
        head = null;
        tail = null;
    }

    private static class Node {
        private final Object element;
        private Node next;

        public Node(Object element) {
            this.element = element;
            this.next = null;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Object getElement() {
            return element;
        }

        public boolean isEnd() {
            return next == null;
        }
    }

    @Override
    protected QueueIterator getIterator() {
        return new LinkedQueueIterator(this);
    }
    protected static class LinkedQueueIterator implements QueueIterator {
        // :NOTE: access
        LinkedQueue queue;
        Node prevNode;
        Node curNode;

        public LinkedQueueIterator(LinkedQueue queue) {
            this.queue = queue;
            prevNode = null;
            curNode = queue.head;
        }
        @Override
        public Object getElement() {
            return curNode.getElement();
        }

        @Override
        public void inc() {
            prevNode = curNode;
            curNode = curNode.getNext();
        }

        // :NOTE: && prevNode.isEnd()
        @Override
        public boolean isEnd() {
            return curNode == null && prevNode.isEnd();
        }
        @Override
        public void removeCur() {
            if (curNode.isEnd()) {
                queue.tail = prevNode;
            }
            if (curNode == queue.head) {
                queue.head = curNode.getNext();
            } else {
                prevNode.setNext(curNode.getNext());
            }
        }
    }
}
