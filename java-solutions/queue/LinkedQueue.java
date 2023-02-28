package queue;

import java.util.Objects;

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
    }
}
