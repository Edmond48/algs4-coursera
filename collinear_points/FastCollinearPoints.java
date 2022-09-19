/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    // an array to store line segments
    private ArrayList<LineSegment> seg = new ArrayList<LineSegment>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        // check input and throw exception if necessary
        if (points == null) {
            throw new IllegalArgumentException("Input can't be null!");
        }

        for (Point a : points) {
            if (a == null) throw new IllegalArgumentException("Argument can't be null");
        }

        // check for dupes and null
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Input can't have duplicates");
            }
        }


        // create a copy of the original array and sort it
        Point[] copy = points.clone();
        Arrays.sort(copy);

        // create a copy of the array of points for sorting
        Point[] aux = copy.clone();

        // start the finding process
        for (int i = 0; i < copy.length - 3; i++) {

            // sort the first n-i elements of the array
            Arrays.sort(aux, i, aux.length, copy[i].slopeOrder());

            // look for the points
            for (int p = i, q = p + 2; q < aux.length; p++, q++) {
                if (copy[i].slopeTo(aux[p]) == copy[i].slopeTo(aux[q])) {

                    Point max = (aux[p].compareTo(aux[q]) > 0) ?
                                (aux[p].compareTo(aux[q - 1]) > 0 ? aux[p] : aux[q - 1]) :
                                (aux[q].compareTo(aux[q - 1]) > 0 ? aux[q] : aux[q - 1]);

                    boolean exist = false;
                    while ((++q < aux.length) && (copy[i].slopeTo(aux[q - 1]) == copy[i]
                            .slopeTo(aux[q]))) {
                        if (aux[q].compareTo(max) > 0) max = aux[q];
                    }
                    for (int n = 0; n < i; n++) {
                        if (copy[i].slopeTo(aux[n]) == copy[i].slopeTo(max)) exist = true;
                    }
                    if (!exist) seg.add(new LineSegment(copy[i], max));
                    p = q - 1;
                    q += 1;
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return seg.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segArr = new LineSegment[seg.size()];
        return seg.toArray(segArr);
    }
}
