/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // pointers
    private Node<Item> first;
    private Node<Item> last;
    private int sz;

    // linked list
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        sz = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null || last == null;
    }

    // return the number of items on the deque
    public int size() {
        return sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item!");
        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        first.prev = null;
        if (isEmpty()) last = first;
        else {
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        sz++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item");
        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else {
            last.prev = oldLast;
            oldLast.next = last;
        }
        sz++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;
        if (sz != 1) first.next.prev = null;
        first = first.next;
        sz--;
        if (isEmpty()) last = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;
        if (sz != 1) last.prev.next = null;
        last = last.prev;
        sz--;
        if (isEmpty()) first = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    // iterator with no remove
    private class LinkedIterator implements Iterator<Item> {

        private Node<Item> current;

        LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            switch (item) {
                case "-":
                    if (!deque.isEmpty()) StdOut.print(deque.removeLast() + " ");
                    break;
                case "~":
                    if (!deque.isEmpty()) StdOut.print(deque.removeFirst() + " ");
                    break;
                case "<":
                    item = StdIn.readString();
                    deque.addLast(item);
                    break;
                case ">":
                    item = StdIn.readString();
                    deque.addFirst(item);
                    break;
            }
        }
        StdOut.println("(" + deque.size() + " left on deque)");
        for (String s : deque) {
            StdOut.print(s + " ");
        }
    }
}
