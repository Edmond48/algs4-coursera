/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public final class Board {

    // array to store numbers at coordinates
    private final int[][] board;

    // dimension
    private final int n;

    // indices of the empty square
    private int row0;
    private int col0;

    // indices of the two squares that are switched in twin
    private int x1 = 0, y1 = 0, x2 = 0, y2 = 0;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {

        // check for a valid argument
        if (tiles == null) throw new IllegalArgumentException("Argument can't be null");

        // copy our own array called board[][] for use
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                board[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != i * n + j + 1) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) manhattan += (Math.abs((board[i][j] - 1) / n - i) +
                        Math.abs((board[i][j] - 1) % n - j));
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i == n - 1) && (j == n - 1) && (board[i][j] == 0)) {
                    return true;
                }
                else if (board[i][j] != i * n + j + 1) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        return (this.dimension() == that.dimension())
                && (Arrays.deepEquals(this.board, that.board));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> iterable = new Stack<Board>();
        if (row0 != 0) iterable.push(swap(row0 - 1, col0, row0, col0));
        if (row0 != n - 1) iterable.push(swap(row0 + 1, col0, row0, col0));
        if (col0 != 0) iterable.push(swap(row0, col0 - 1, row0, col0));
        if (col0 != n - 1) iterable.push(swap(row0, col0 + 1, row0, col0));
        return iterable;
    }

    // helper function
    private Board swap(int a, int b, int c, int d) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = board[i][j];
            }
        }
        int holder = copy[a][b];
        copy[a][b] = copy[c][d];
        copy[c][d] = holder;
        return new Board(copy);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        while (((x1 == x2) && (y1 == y2)) || (board[x1][y1] == 0) || (board[x2][y2] == 0)) {
            x1 = StdRandom.uniform(0, n);
            y1 = StdRandom.uniform(0, n);
            x2 = StdRandom.uniform(0, n);
            y2 = StdRandom.uniform(0, n);
        }
        return swap(x1, y1, x2, y2);

    }

    // unit testing (not graded)
    public static void main(String[] args) {

        // read from command line and create the input array
        In in = new In(args[0]);
        int newN = in.readInt();
        int[][] newBoard = new int[newN][newN];
        for (int i = 0; i < newN; i++) {
            for (int j = 0; j < newN; j++) {
                newBoard[i][j] = in.readInt();
            }
        }

        // test every methods
        Board b = new Board(newBoard);
        Board c = new Board(newBoard);
        StdOut.println(b.toString());
        StdOut.println("Dimension: " + b.dimension());
        StdOut.println("Hamming distance: " + b.hamming());
        StdOut.println("Manhattan distance: " + b.manhattan());
        StdOut.println("This board is the goal? " + b.isGoal());
        StdOut.println("Board equal a different reference with same value? " + b.equals(c));
        StdOut.println("Neighbors: ");
        for (Board board : b.neighbors()) {
            StdOut.println(board.toString());
        }
        StdOut.println("Twin: \r\n" + b.twin().toString());
    }
}
