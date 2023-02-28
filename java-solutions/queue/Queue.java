package queue;

/*
Queue Model:
    a[1]...a[n],
    1 - head,
    n - tail
Inv:
    n >= 0 &&
    for i in [1; n]:
        a[i] != null

Let immutable(n):
    for i in [1; n]:
       a'[i] = a[i]
 */

public interface Queue {

    // Pre: element != null
    // Post: n' = n + 1 &&
    //       a[n'] = element &&
    //       immutable(n)
    void enqueue(Object element);

    // Pre: n >= 1
    // Post: R = a[1] &&
    //       n' = n &&
    //       immutable(n)
    Object element();

    // Pre: n >= 1
    // Post: R = a[1] &&
    //       n' = n - 1 &&
    //       for i in [1; n']:
    //           a'[i] = a[i + 1]
    Object dequeue();

    // Pre: true
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = n
    int size();

    // Pre: true
    // Post: n' = n &&
    //       immutable(n) &&
    //       R = (n == 0)
    boolean isEmpty();

    // Pre: true
    // Post: n' = 0
    void clear();
}
