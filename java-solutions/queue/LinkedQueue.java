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
        return new LinkedQueueIterator();
    }
    protected class LinkedQueueIterator implements QueueIterator {
        // :NOTE: access :FIXED:
        protected Node prevNode;
        protected Node curNode;

        public LinkedQueueIterator() {
            prevNode = null;
            curNode = head;
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

        // :NOTE: && prevNode.isEnd() :FIXED:
        @Override
        public boolean isEnd() {
            return curNode == null;
        }

        @Override
        public void removeCur() {
            if (curNode.isEnd()) {
                tail = prevNode;
            }
            if (curNode == head) {
                head = curNode.getNext();
            } else {
                prevNode.setNext(curNode.getNext());
            }
        }
    }
}
