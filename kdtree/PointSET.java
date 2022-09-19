/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {

    // declare set
    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        throwIfNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        throwIfNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        throwIfNull(rect);
        Stack<Point2D> iterator = new Stack<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) iterator.push(p);
        }
        return iterator;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        throwIfNull(p);

        // return null if empty
        if (points.isEmpty()) return null;

        Point2D nearest = points.min();
        for (Point2D p2 : points) {
            if (p.distanceSquaredTo(p2) < p.distanceSquaredTo(nearest)) nearest = p2;
        }
        return nearest;
    }

    // helper method for throwing exception
    private void throwIfNull(Object... arg) {
        for (Object object : arg) {
            if (object == null) throw new IllegalArgumentException("Argument can't be null.");
        }
    }

    // // unit testing of the methods (optional)
    // public static void main(String[] args) {
    //
    // }
}
