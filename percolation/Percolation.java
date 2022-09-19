/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

// TODO fix the coor array to boolean and calculate the uf element index with row and col

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // declare top and bottom virtual sites
    private final int top = 0;
    private final int bot;

    // Number of open sites
    private int openSite = 0;

    // create an array for the system
    private int[][] coor;

    // UF data structure
    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be larger than 0: " + n);

        // allocating memory?
        coor = new int[n + 1][n + 1];

        // set bot value
        bot = n * n + 1;

        // initialize union-find with 2 more elements for the top and bottom virtual node
        uf = new WeightedQuickUnionUF(n * n + 2);

        // for assigning root values
        int count = -1;

        // assigning value to the array
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                coor[i][j] = count;
                count--;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row >= coor.length || col >= coor.length) {
            throw new IllegalArgumentException(
                    "Arguments must be integers greater than 0!: " + row + " " + col);
        }

        // if site is already open, do nothing
        if (isOpen(row, col)) return;


        // set site's value to be positive
        coor[row][col] = -coor[row][col];

        // a variable to make things easier?
        int x = coor[row][col];
        int length = coor.length - 1;

        // connect site to virtual top and bottom if they are first or last row
        if (row == 1) uf.union(top, x);
        if (row == coor.length - 1) uf.union(bot, x);

        // unify the neighboring site if they are open and are not out of bound
        if ((row != 1) && isOpen(row - 1, col))
            uf.union(x, x - length);
        if ((col != 1) && isOpen(row, col - 1))
            uf.union(x, x - 1);
        if ((col != length) && isOpen(row, col + 1))
            uf.union(x, x + 1);
        if ((row != length) && isOpen(row + 1, col))
            uf.union(x, x + length);

        // add to open site count
        openSite++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row >= coor.length || col >= coor.length) {
            throw new IllegalArgumentException(
                    "Arguments must be integers greater than 0!: " + row + " " + col);
        }
        return coor[row][col] > 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row >= coor.length || col >= coor.length) {
            throw new IllegalArgumentException(
                    "Arguments must be integers greater than0!: " + row + " " + col);
        }
        if (!isOpen(row, col)) return false;
        return uf.find(coor[row][col]) == uf.find(top);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(top) == uf.find(bot);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation per = new Percolation(100);
        System.out.println(per.coor[10][10]);
    }
}
