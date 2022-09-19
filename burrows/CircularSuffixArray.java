/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.Heap;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private static final int R = 256;

    private CircularSuffix[] index;

    private int n;

    private String s;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int index;

        public int compareTo(CircularSuffix cs) {
            for (int i = 0; i < n; i++) {
                if (s.charAt(index + i) < s.charAt(cs.index + i)) return -1;
                else if (s.charAt(index + i) > s.charAt(cs.index + i)) return 1;
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {

        if (s == null) throw new IllegalArgumentException("Argument cannot be null");
        this.n = s.length();
        this.s = s + s;

        index = new CircularSuffix[n];

        for (int i = 0; i < n; i++) {
            index[i] = new CircularSuffix();
            index[i].index = i;
        }

        Heap.sort(index);
    }

    // private char charAt(int i) {
    //     if (i >= n) return s.charAt(i - n);
    //     else return s.charAt(i);
    // }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("Index " + i + " out of bound");
        return index[i].index;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < csa.length(); i++) {
            StdOut.print(csa.index(i) + " ");
        }
    }
}
