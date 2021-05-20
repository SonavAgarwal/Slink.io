import java.io.Serializable;

public class Node<E> implements Serializable {

    private E data;
    private Node<E> next;
    private Node<E> previous;

    public Node(E data) {
        this.data = data;
        next = null;
        previous = null;
    }

    public void setNext(Node<E> newNode) {
        next = newNode;
    }

    public void setPrevious(Node<E> newNode) {
        previous = newNode;
    }

    public E get() {
        return data;
    }

    public Node<E> next() {
        return next;
    }

    public Node<E> previous() {
        return previous;
    }

    public void set(E data) {
        this.data = data;
    }

    public String toString() {
        return data.toString();
    }
}
