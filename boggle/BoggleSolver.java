/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

public class BoggleSolver {

    // Dictionary trie
    private final DeluxTST<Integer> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new DeluxTST<Integer>();
        for (int i = 0; i < dictionary.length; i++) {
            int j = i < dictionary.length / 2 ?
                    i + dictionary.length / 2 :
                    i - dictionary.length / 2;
            switch (dictionary[j].length()) {
                case 1:
                case 2:
                    break;
                case 3:
                case 4:
                    this.dictionary.put(dictionary[j], 1);
                    break;
                case 5:
                    this.dictionary.put(dictionary[j], 2);
                    break;
                case 6:
                    this.dictionary.put(dictionary[j], 3);
                    break;
                case 7:
                    this.dictionary.put(dictionary[j], 5);
                    break;
                default:
                    this.dictionary.put(dictionary[j], 11);
                    break;
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TrieSET set = new TrieSET();
        int r = board.rows();
        int c = board.cols();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                getWord(i, j, board, new boolean[r][c], new StringBuilder(), set,
                        dictionary.root());
            }
        }
        return set;
    }

    private void getWord(int row, int col, BoggleBoard board, boolean[][] marked,
                         StringBuilder str, TrieSET set, DeluxTST.Node<Integer> x) {
        char c = board.getLetter(row, col);

        str.append(c);

        marked[row][col] = true;

        DeluxTST.Node<Integer> node = dictionary.find(x, c);

        if (c == 'Q' && node != null) {
            str.append('U');
            node = dictionary.find(node.mid, 'U');
        }

        // StdOut.println(str);

        if (node != null) {
            if (node.val != null) {
                set.add(str.toString());
            }
            if (row != 0) {
                if (!marked[row - 1][col]) {
                    getWord(row - 1, col, board, copyArray(marked), str, set, node.mid);
                }
                if (col != 0 && !marked[row - 1][col - 1]) {
                    getWord(row - 1, col - 1, board, copyArray(marked), str, set, node.mid);
                }
                if (col != board.cols() - 1 && !marked[row - 1][col + 1]) {
                    getWord(row - 1, col + 1, board, copyArray(marked), str, set, node.mid);
                }
            }

            if (col != 0 && !marked[row][col - 1]) {
                getWord(row, col - 1, board, copyArray(marked), str, set, node.mid);
            }

            if (col != board.cols() - 1 && !marked[row][col + 1]) {
                getWord(row, col + 1, board, copyArray(marked), str, set, node.mid);
            }

            if (row != board.rows() - 1) {
                if (!marked[row + 1][col]) {
                    getWord(row + 1, col, board, copyArray(marked), str, set, node.mid);
                }
                if (col != 0 && !marked[row + 1][col - 1]) {
                    getWord(row + 1, col - 1, board, copyArray(marked), str, set, node.mid);
                }
                if (col != board.cols() - 1 && !marked[row + 1][col + 1]) {
                    getWord(row + 1, col + 1, board, copyArray(marked), str, set, node.mid);
                }
            }
        }

        if (str.length() > 1 && str.charAt(str.length() - 2) == 'Q') {
            str.delete(str.length() - 2, str.length());
        }
        else str.deleteCharAt(str.length() - 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word)) return 0;
        return dictionary.get(word);
    }

    private boolean[][] copyArray(boolean[][] arg) {
        boolean[][] newArray = new boolean[arg.length][arg[0].length];
        for (int i = 0; i < arg.length; i++) {
            for (int j = 0; j < arg[i].length; j++) {
                newArray[i][j] = arg[i][j];
            }
        }
        return newArray;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}


