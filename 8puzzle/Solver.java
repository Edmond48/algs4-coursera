/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // declare the search node that stores the solution
    private SearchNode solution;

    // declare a boolean to tell if board is solvable
    private boolean solvable;

    // declare a search node class
    private class SearchNode implements Comparable<SearchNode> {
        private final Board node;
        private final SearchNode prev;
        private final int moves;
        private final int priorFunc;

        public SearchNode(Board b, SearchNode sn) {
            this.node = b;
            prev = sn;
            if (prev == null) moves = 0;
            else moves = prev.moves + 1;

            // use manhattan
            priorFunc = node.manhattan() + moves;

            // use hamming
            // priorFunc = node.hamming() + moves;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priorFunc, that.priorFunc);
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        // throw if argument is null
        if (initial == null) throw new IllegalArgumentException("Argument can't be null");

        // check if it's solvable and also solve it
        SearchNode current;
        SearchNode currentTwin;

        // create two PQ
        MinPQ<SearchNode> mpq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> mpqtwin = new MinPQ<SearchNode>();

        // insert the two initial search node
        mpq.insert(new SearchNode(initial, null));
        mpqtwin.insert(new SearchNode(initial.twin(), null));

        // remove from PQ and set the current and currentTwin search node
        current = mpq.min();
        currentTwin = mpqtwin.min();

        // loop to find the solution
        while ((!current.node.isGoal()) && (!currentTwin.node.isGoal())) {

            // remove from PQ
            current = mpq.delMin();

            // remove from twin PQ
            currentTwin = mpqtwin.delMin();

            // insert neighbor node for original board
            for (Board a : current.node.neighbors()) {
                if ((current.prev == null) || (!a.equals(current.prev.node))) {
                    mpq.insert(new SearchNode(a, current));
                }
            }

            // insert neighbor node for twin board
            for (Board a : currentTwin.node.neighbors()) {
                if ((currentTwin.prev == null) || (!a.equals(currentTwin.prev.node))) {
                    mpqtwin.insert(new SearchNode(a, currentTwin));
                }
            }
        }
        solution = current;
        solvable = current.node.isGoal();

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solution.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> iterator = new Stack<Board>();
        SearchNode pointer = solution;
        while (pointer != null) {
            iterator.push(pointer.node);
            pointer = pointer.prev;
        }
        return iterator;
    }

    // test client
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
