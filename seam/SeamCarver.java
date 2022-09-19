/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    // constant border energy
    private static final double BORDER_ENERGY = 1000.0;

    // the photo after edit
    private Picture carvedPic;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Null argument for constructor");
        this.carvedPic = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(carvedPic);
        return picture;
    }

    // width of current picture
    public int width() {
        return carvedPic.width();
    }

    // height of current picture
    public int height() {
        return carvedPic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // check for valid argument
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new IllegalArgumentException("Pixel index out of bound");

        // if pixel at border return 1000
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return BORDER_ENERGY;

        // calculate and return
        return Math.sqrt(deltaSquaredX(x, y) + deltaSquaredY(x, y));
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        // energy 2D-array to save computing time
        double[][] energy = new double[width()][height()];
        for (int i = 0; i < energy.length; i++) {
            for (int j = 0; j < energy[i].length; j++)
                energy[i][j] = energy(i, j);
        }

        // create a DS to ask query about the shortest path to pixels
        // the top row is set to 1000
        // the rest is infinite
        double[][] distTo = new double[width()][height()];
        for (int i = 0; i < distTo.length; i++) {
            for (int j = 0; j < distTo[i].length; j++) {
                if (i == 0) distTo[i][j] = BORDER_ENERGY;
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // DS to keep track of the pixel from which we get to the current pixel in the seam
        int[][] pathTo = new int[width()][height()];

        // for every pixel in the picture going for one column at a time
        for (int x = 0; x < width() - 1; x++) {
            for (int y = 0; y < height(); y++) {

                // check the next pixel directly on the right
                if (distTo[x + 1][y] > distTo[x][y] + energy[x + 1][y]) {
                    distTo[x + 1][y] = distTo[x][y] + energy[x + 1][y];
                    pathTo[x + 1][y] = y;
                }

                // check the upper-right adjacent pixel
                if ((y != 0) && (distTo[x + 1][y - 1] > distTo[x][y] + energy[x + 1][y
                        - 1])) {
                    distTo[x + 1][y - 1] = distTo[x][y] + energy[x + 1][y - 1];
                    pathTo[x + 1][y - 1] = y;
                }

                // check the lower-right adjacent pixel
                if ((y != height() - 1) && (distTo[x + 1][y + 1] > distTo[x][y] + energy[x
                        + 1][y + 1])) {
                    distTo[x + 1][y + 1] = distTo[x][y] + energy[x + 1][y + 1];
                    pathTo[x + 1][y + 1] = y;
                }
            }
        }

        // initialize the row of the last pixel in the seam
        int endY = 0;

        // find the pixel at the end of the seam and store it in endY
        double min = distTo[width() - 1][0];
        for (int y = 0; y < height(); y++) {
            if (distTo[width() - 1][y] < min) {
                min = distTo[width() - 1][y];
                endY = y;
            }
        }

        // construct the seam by going from the tail to the head of the pathTo array
        int[] seam = new int[width()];
        seam[seam.length - 1] = endY;
        for (int x = seam.length - 2; x >= 0; x--) {
            seam[x] = pathTo[x + 1][seam[x + 1]];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        // energy 2D-array to save computing time
        double[][] energy = new double[width()][height()];
        for (int i = 0; i < energy.length; i++) {
            for (int j = 0; j < energy[i].length; j++)
                energy[i][j] = energy(i, j);
        }

        // create a DS to ask query about the shortest path to pixels
        // the top row is set to 1000
        // the rest is infinite
        double[][] distTo = new double[width()][height()];
        for (int i = 0; i < distTo.length; i++) {
            for (int j = 0; j < distTo[i].length; j++) {
                if (j == 0) distTo[i][j] = BORDER_ENERGY;
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // DS to keep track of the pixel from which we get to the current pixel in the seam
        int[][] pathTo = new int[width()][height()];

        // for every pixel in the picture going for one row at a time
        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {

                // check the pixel directly below
                if (distTo[x][y + 1] > distTo[x][y] + energy[x][y + 1]) {
                    distTo[x][y + 1] = distTo[x][y] + energy[x][y + 1];
                    pathTo[x][y + 1] = x;
                }

                // check lower-left pixel
                if ((x != 0) && (distTo[x - 1][y + 1] > distTo[x][y] + energy[x - 1][y + 1])) {
                    distTo[x - 1][y + 1] = distTo[x][y] + energy[x - 1][y + 1];
                    pathTo[x - 1][y + 1] = x;
                }

                // check lower-right pixel
                if ((x != width() - 1) && (distTo[x + 1][y + 1] > distTo[x][y] + energy[x
                        + 1][y + 1])) {
                    distTo[x + 1][y + 1] = distTo[x][y] + energy[x + 1][y + 1];
                    pathTo[x + 1][y + 1] = x;
                }
            }
        }

        // the column of the last pixel in the seam
        int endX = 0;

        // find the pixel with the shortest seam and assign its column to endX
        double min = distTo[0][height() - 1];
        for (int x = 0; x < width(); x++) {
            if (distTo[x][height() - 1] < min) {
                min = distTo[x][height() - 1];
                endX = x;
            }
        }

        // construct the seam by going from tail to head of pathTo array
        int[] seam = new int[height()];
        seam[seam.length - 1] = endX;
        for (int y = seam.length - 2; y >= 0; y--) {
            seam[y] = pathTo[seam[y + 1]][y + 1];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

        // check if the picture can be re-targeted
        if (height() <= 1) throw new IllegalArgumentException("Minimum picture width");

        // check for valid argument
        checkHor(seam);

        // create a new picture object with reduced dimensions
        Picture newPic = new Picture(width(), height() - 1);

        // copy every pixel except for the pixels on the seam
        for (int x = 0; x < newPic.width(); x++) {
            int avoid = seam[x];
            for (int y = 0; y < height(); y++) {
                if (y < avoid) newPic.setRGB(x, y, carvedPic.getRGB(x, y));
                else if (y > avoid) newPic.setRGB(x, y - 1, carvedPic.getRGB(x, y));
            }
        }

        carvedPic = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        // check if the picture can be re-targeted
        if (width() <= 1) throw new IllegalArgumentException("Minimum picture height");

        // check for valid argument
        checkVer(seam);

        // create a new picture object with reduced dimensions
        Picture newPic = new Picture(width() - 1, height());
        for (int y = 0; y < newPic.height(); y++) {
            int avoid = seam[y];
            for (int x = 0; x < width(); x++) {
                if (x < avoid) newPic.setRGB(x, y, carvedPic.getRGB(x, y));
                else if (x > avoid) newPic.setRGB(x - 1, y, carvedPic.getRGB(x, y));
            }
        }

        carvedPic = newPic;
    }

    // helper method to check argument of removeHorizontalSeam
    private void checkHor(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Argument can't be null");
        if (seam.length != width()) throw new IllegalArgumentException("Invalid array length");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException("Invalid difference between pixel");
        }
    }

    // helper method to check argument of removeVerticalSeam
    private void checkVer(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Argument can't be null");
        if (seam.length != height()) throw new IllegalArgumentException("Invalid array length");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException("Invalid difference between pixel");
        }
    }

    // helper method to calculate the total-x-difference-squared of 4 pixels adjacent to (x,y)
    private double deltaSquaredX(int x, int y) {
        int rgb0 = carvedPic.getRGB(x - 1, y);
        int rbg1 = carvedPic.getRGB(x + 1, y);
        int r = ((rgb0 >> 16) & 0xFF) - ((rbg1 >> 16) & 0xFF);
        int g = ((rgb0 >> 8) & 0xFF) - ((rbg1 >> 8) & 0xFF);
        int b = ((rgb0) & 0xFF) - ((rbg1) & 0xFF);
        return (r * r + g * g + b * b) * 1.0;
    }

    // helper method to calculate the total-y-difference-squared of 4 pixels adjacent to (x,y)
    private double deltaSquaredY(int x, int y) {
        int rgb0 = carvedPic.getRGB(x, y - 1);
        int rbg1 = carvedPic.getRGB(x, y + 1);
        int r = ((rgb0 >> 16) & 0xFF) - ((rbg1 >> 16) & 0xFF);
        int g = ((rgb0 >> 8) & 0xFF) - ((rbg1 >> 8) & 0xFF);
        int b = ((rgb0) & 0xFF) - ((rbg1) & 0xFF);
        return (r * r + g * g + b * b) * 1.0;
    }
}
