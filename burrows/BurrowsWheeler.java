/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i, 32);
                break;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt(csa.index(i) == 0 ? csa.length() - 1 : csa.index(i) - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        int[] frequency = new int[R + 1];
        int[] next = new int[s.length()];

        for (int i = 0; i < s.length(); i++) {
            frequency[s.charAt(i) + 1]++;
        }

        for (int r = 1; r < R; r++) {
            frequency[r + 1] += frequency[r];
        }

        for (int i = 0; i < s.length(); i++) {
            next[frequency[s.charAt(i)]++] = i;
        }

        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt(next[first]));
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Invalid argument!");
    }
}
