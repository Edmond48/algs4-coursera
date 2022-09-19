/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        // number of elements to be printed
        final int k = Integer.parseInt(args[0]);

        // create new randomized queue
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        double n = 1.0;

        // enqueue k items from input
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (k == 0) return;
            else if (rq.size() < k) {
                rq.enqueue(s);
            }
            else if (StdRandom.uniform() < (k / n)) {
                rq.dequeue();
                rq.enqueue(s);
            }
            n++;
        }
        while (!rq.isEmpty()) {
            StdOut.println(rq.dequeue());
        }
    }
}
