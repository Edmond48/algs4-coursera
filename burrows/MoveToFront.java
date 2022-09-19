/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;
    private static final int LG_R = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int[] sequence = new int[R];
        for (int i = 0; i < R; i++) sequence[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = sequence[c];
            BinaryStdOut.write(index, LG_R);
            if (index != 0) {
                for (int j = 0; j < R; j++) {
                    if (sequence[j] < index) sequence[j]++;
                }
                sequence[c] = 0;
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] sequence = new char[R];
        for (int i = 0; i < R; i++) sequence[i] = (char) i;
        while (!BinaryStdIn.isEmpty()) {
            int code = BinaryStdIn.readInt(LG_R);
            char index = sequence[code];
            BinaryStdOut.write(index);
            if (code != 0) {
                for (int j = code; j > 0; j--) sequence[j] = sequence[j - 1];
                sequence[0] = index;
            }
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Invalid argument");
    }
}
