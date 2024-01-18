import java.util.Iterator;

public class DLListIterator<E> implements Iterator<E> {

    Node<E> current;
    Node<E> tail;

    public DLListIterator(DLList<E> dLList) {
        current = dLList.getHead();
        tail = dLList.getTail();
    }

    public boolean hasNext() {
        return current.next() != null && current.next() != tail;
    }

    public E next() {
        current = current.next();
        return current.get();
    }
}
