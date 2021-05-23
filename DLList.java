import java.io.Serializable;
import java.io.Serializable;
import java.util.Iterator;

public class DLList<E> implements Iterable<E>, Serializable {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public DLList() {
        head = new Node<E>(null);
        tail = new Node<E>(null);
        head.setNext(tail);
        head.setPrevious(null);
        tail.setNext(null);
        tail.setPrevious(head);
        size = 0;
    }

    public Node<E> getHead() {
        return head;
    }

    public Node<E> getTail() {
        return tail;
    }

    public E getFirst() {
        if (size == 0) return null;
        return head.next().get();
    }

    public E getLast() {
        if (size == 0) return null;
        return tail.previous().get();
    }

    public void removeLast() {
        if (size == 0) return;
        Node<E> newLast = tail.previous().previous();
        newLast.setNext(tail);
        tail.setPrevious(newLast);
        size--;
    }

    public void clear() {
        head.setNext(tail);
        tail.setPrevious(head);
        size = 0;
    }

    public void reverse() {
        DLList<E> replacement = new DLList<E>();
        Node<E> current = tail.previous();
        while (current != null && current != head) {
            replacement.add(current.get());
            current = current.previous();
        }
        head = replacement.getHead();
        tail = replacement.getTail();
    }

    private Node<E> getNode(int index) {
        Node<E> current;
        if (index < (size / 2)) {
            current = head.next();
            for (int i = 0; i < index; i++) {
                current = current.next();
            }
        } else {
            current = tail.previous();
            for (int i = size - 1; i > index; i--) {
                current = current.previous();
            }
        }
        return current;
    }

    public void add(E element) {
        Node<E> newNode = new Node<E>(element);
        Node<E> before = tail.previous();
        Node<E> after = tail;
        newNode.setNext(after);
        newNode.setPrevious(before);
        before.setNext(newNode);
        after.setPrevious(newNode);
        size++;
    }

    public void add(DLList<E> elements) {
        for (E e : elements) {
            add(e);
        }
    }

    public void addAtBeginning(E element) {
        Node<E> newNode = new Node<E>(element);
        Node<E> before = head;
        Node<E> after = head.next();
        newNode.setNext(after);
        newNode.setPrevious(before);
        before.setNext(newNode);
        after.setPrevious(newNode);
        size++;
    }

    public void add(int index, E element) {
        Node<E> newNode = new Node<E>(element);
        Node<E> current = head.next();
        int i = 0;
        while (current.next() != null) {
            if (i == index) {
                Node<E> before = current.previous();
                Node<E> after = current;
                newNode.setNext(after);
                newNode.setPrevious(before);
                before.setNext(newNode);
                after.setPrevious(newNode);
                size++;
                break;
            }
            current = current.next();
            i++;
        }
        size++;
    }

    public E get(int index) {
        return getNode(index).get();
    }

    public boolean remove(int index) {
        Node<E> current = getNode(index);
        Node<E> before = current.previous();
        Node<E> after = current.next();
        before.setNext(after);
        after.setPrevious(before);
        size--;
        return true;
    }

    public boolean remove(E element) {
        Node<E> current = head.next();
        while (current != null && current != tail) {
            if (current.get().equals(element)) {
                Node<E> before = current.previous();
                Node<E> after = current.next();
                before.setNext(after);
                after.setPrevious(before);
                size--;
                return true;
            }
            current = current.next();
        }
        // if (current.get().equals(element)) {
        // 	head.setNext(head.next().next());
        // 	size--;
        // }
        // else {
        // }
        return false;
    }

    public void set(int index, E element) {
        getNode(index).set(element);
    }

    public String toString() {
        Node<E> current = head.next();
        String finalString = "";
        while (current != null && current != tail) {
            if (current.next() != null) {
                finalString += current.get().toString();
            }
            current = current.next();
        }
        return finalString;
    }

    public void print() {
        print(head.next());
    }

    public void print(Node<E> n) {
        System.out.println(n);
        if (n.next() != tail && n.next() != null) print(n.next());
    }

    public int size() {
        return size;
    }

    public Iterator<E> iterator() {
        return new DLListIterator<E>(this);
    }
}
