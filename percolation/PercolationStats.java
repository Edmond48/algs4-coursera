/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    // constant confidence interval
    static final double CONFIDENCE_95 = 1.96;

    // array to store thresholds
    private final double[] thrs;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        // throw if arguments is less than or equal to 0
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "Arguments must be integers greater than 0! " + n + " " + trials);
        }

        // declare size of grid
        // double sz = n * n;

        // declare threshold array
        thrs = new double[trials];

        // perform T experiment and assign value to threshold array
        for (int i = 0; i < trials; i++) {

            // create a new Percolation object
            Percolation per = new Percolation(n);

            // open random sites until system percolates
            while (!per.percolates()) {
                per.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }

            // assign threshold value to array
            thrs[i] = per.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thrs);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thrs);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(thrs.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(thrs.length);
    }

    // test client
    public static void main(String[] args) {

        // create new object
        PercolationStats ps = new PercolationStats(1, 1);
        if (args.length == 2) {
            ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }

        // assign value to print
        double mean = ps.mean();
        double stddev = ps.stddev();
        double confilo = ps.confidenceLo();
        double confihi = ps.confidenceHi();

        StdOut.printf("%-23s = " + mean + "%n", "mean");
        StdOut.printf("%-23s = " + stddev + "%n", "stddev");
        StdOut.printf("%-23s = [" + confilo + ", " + confihi + "]%n", "95% confidence interval");

    }
}
