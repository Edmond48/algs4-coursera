/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

// TODO remove the calls to random before the loop in dequeue() and iterator
// TODO maybe instead use shuffle from the library: https://coursera.cs.princeton.edu/algs4/assignments/queues/faq.php

import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // Item array
    private Item[] rq;

    // index of the head indicates space for storage if stack nextSpace is empty
    private int head;

    // size
    private int sz;

    // Stack to store next available position
    private ResizingArrayStack<Integer> nextSpace = new ResizingArrayStack<Integer>();

    // construct an empty randomized queue
    public RandomizedQueue() {
        rq = (Item[]) new Object[2];
        sz = 0;
        head = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return sz == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return sz;
    }

    // function to resize array
    private void resize(int capacity) {
        assert capacity >= sz;
        Item[] copy = (Item[]) new Object[capacity];
        int j = 0;
        for (int i = 0; i < rq.length; i++) {
            if (rq[i] != null) {
                copy[j] = rq[i];
                j++;
            }
        }
        head = j;
        rq = copy;
        nextSpace = new ResizingArrayStack<Integer>();
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null!");
        if (sz == rq.length) resize(rq.length * 2);
        if (!nextSpace.isEmpty()) rq[nextSpace.pop()] = item;
        else {
            rq[head++] = item;
        }
        sz++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow!");
        int index = StdRandom.uniform(0, rq.length);
        while (rq[index] == null) {
            index = StdRandom.uniform(0, rq.length);
        }
        Item item = rq[index];
        rq[index] = null;
        sz--;
        nextSpace.push(index);

        // resize if necessary
        if (sz > 0 && sz == rq.length / 4) resize(rq.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack is empty!");
        Item item = rq[StdRandom.uniform(0, rq.length)];
        while (item == null) {
            item = rq[StdRandom.uniform(0, rq.length)];
        }
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {

        // make a copy of the array
        private Item[] rqiterator = rq.clone();

        // get the current size
        private int n = sz;

        // see if there are any other items
        public boolean hasNext() {
            return n > 0;
        }

        // remove which does not work
        public void remove() {
            throw new UnsupportedOperationException();
        }

        // get the next element
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            // get a random index
            int x = StdRandom.uniform(0, rqiterator.length);

            // get an index that is suitable
            while (rqiterator[x] == null) {
                x = StdRandom.uniform(0, rqiterator.length);
            }
            Item item = rqiterator[x];
            rqiterator[x] = null;
            n--;
            if (n > 0 && n == rqiterator.length / 4) {
                Item[] copy = (Item[]) new Object[rqiterator.length / 2];
                int j = 0;
                for (int i = 0; i < rqiterator.length; i++) {
                    if (rqiterator[i] != null) {
                        copy[j] = rqiterator[i];
                        j++;
                    }
                }
                rqiterator = copy;
            }
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) rq.enqueue(item);
            else if (!rq.isEmpty()) StdOut.print(rq.dequeue() + " ");
        }
        StdOut.println(
                "(" + rq.size() + " left on random queue. Here is a sample: " + rq.sample() + ")");
        for (int i = 0; i < 3; i++) {
            for (String s : rq) {
                StdOut.print(s + " ");
            }
            StdOut.println();
        }
    }
}
