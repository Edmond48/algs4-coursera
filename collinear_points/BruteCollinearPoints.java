/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    // array of line segments
    private final ArrayList<LineSegment> seg = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        // check input and throw exception if necessary
        if (points == null) {
            throw new IllegalArgumentException("Input can't be null!");
        }

        for (Point a : points) {
            if (a == null) throw new IllegalArgumentException("Argument can't be null!");
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

        // find 4 points
        for (int p = 0; p < copy.length - 3; p++) {
            for (int q = p + 1; q < copy.length - 2; q++) {
                for (int r = q + 1; r < copy.length - 1; r++) {
                    for (int s = r + 1; s < copy.length; s++) {
                        if (copy[p].slopeTo(copy[q]) == copy[p].slopeTo(copy[r])
                                && copy[p].slopeTo(copy[r]) == copy[p].slopeTo(copy[s])) {
                            seg.add(new LineSegment(copy[p], copy[s]));
                        }
                    }
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
