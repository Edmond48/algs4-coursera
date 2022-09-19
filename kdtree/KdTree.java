/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private int sz = 0;

    private Node root;

    private class Node {
        private final Point2D point;
        // private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D point) {
            this.point = point;
        }
    }

    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return sz;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        throwIfNull(p);
        root = insert(root, p, true);
    }

    // recursive method for insert
    private Node insert(Node node, Point2D p, boolean useX) {
        if (node == null) {
            sz++;
            return new Node(p);
        }
        else if (node.point.equals(p)) return node;
        if (useX) {
            int cmp = Double.compare(p.x(), node.point.x());
            if (cmp < 0) node.lb = insert(node.lb, p, false);
            else node.rt = insert(node.rt, p, false);
        }
        else {
            int cmp = Double.compare(p.y(), node.point.y());
            if (cmp < 0) node.lb = insert(node.lb, p, true);
            else node.rt = insert(node.rt, p, true);
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        throwIfNull(p);
        return contains(root, p, true);
    }

    // recursive function for contains
    private boolean contains(Node node, Point2D p, boolean useX) {
        if (node == null) return false;
        if (node.point.equals(p)) return true;
        if (useX) {
            if (p.x() < node.point.x()) return contains(node.lb, p, false);
            else return contains(node.rt, p, false);
        }
        else {
            if (p.y() < node.point.y()) return contains(node.lb, p, true);
            else return contains(node.rt, p, true);
        }

    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true, 0.0, 0.0, 1.0, 1.0);
    }

    private void draw(Node node, boolean useX, double minX, double minY, double maxX, double maxY) {
        if (node != null) {
            if (useX) {
                drawLine(node.point.x(), minY, node.point.x(), maxY, true);
                draw(node.lb, false, minX, minY, node.point.x(), maxY);
                draw(node.rt, false, node.point.x(), minY, maxX, maxY);
            }
            else {
                drawLine(minX, node.point.y(), maxX, node.point.y(), false);
                draw(node.lb, true, minX, minY, maxX, node.point.y());
                draw(node.rt, true, minX, node.point.y(), maxX, maxY);
            }
            drawPoint(node.point);
        }
    }

    private void drawPoint(Point2D p) {
        StdDraw.setPenColor();
        StdDraw.setPenRadius(0.01);
        p.draw();
    }

    private void drawLine(double x0, double y0, double x1, double y1, boolean useRed) {
        StdDraw.setPenRadius();
        if (useRed) StdDraw.setPenColor(StdDraw.RED);
        else StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(x0, y0, x1, y1);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        throwIfNull(rect);
        Stack<Point2D> iterator = new Stack<Point2D>();
        return rangeSearch(iterator, root, rect, true, 0.0, 0.0, 1.0, 1.0);
    }

    // recursive method to check if point is in search rectangle
    private Stack<Point2D> rangeSearch(Stack<Point2D> stack, Node node, RectHV rect, boolean useX,
                                       double minX, double minY, double maxX, double maxY) {
        if ((node != null) && (rect.intersects(new RectHV(minX, minY, maxX, maxY)))) {
            if (rect.contains(node.point)) stack.push(node.point);
            if (useX) {
                rangeSearch(stack, node.lb, rect, false, minX, minY, node.point.x(), maxY);
                rangeSearch(stack, node.rt, rect, false, node.point.x(), minY, maxX, maxY);
            }
            else {
                rangeSearch(stack, node.lb, rect, true, minX, minY, maxX, node.point.y());
                rangeSearch(stack, node.rt, rect, true, minX, node.point.y(), maxX, maxY);
            }
        }
        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        throwIfNull(p);
        if (isEmpty()) return null;
        return nearest(root, p, root.point, true, 0.0, 0.0, 1.0, 1.0);
    }

    // recursive method for nearest point search
    private Point2D nearest(Node node, Point2D query, Point2D min, boolean useX,
                            double minX, double minY, double maxX, double maxY) {
        if ((node != null) && (new RectHV(minX, minY, maxX, maxY).distanceSquaredTo(query) < min
                .distanceSquaredTo(query))) {
            if (query.distanceSquaredTo(node.point) <= query.distanceSquaredTo(min))
                min = node.point;
            if (useX) {
                if (query.x() < node.point.x()) {
                    min = nearest(node.lb, query, min, false, minX, minY, node.point.x(), maxY);
                    min = nearest(node.rt, query, min, false, node.point.x(), minY, maxX, maxY);
                }
                else {
                    min = nearest(node.rt, query, min, false, node.point.x(), minY, maxX, maxY);
                    min = nearest(node.lb, query, min, false, minX, minY, node.point.x(), maxY);
                }
            }
            else {
                if (query.y() < node.point.y()) {
                    min = nearest(node.lb, query, min, true, minX, minY, maxX, node.point.y());
                    min = nearest(node.rt, query, min, true, minX, node.point.y(), maxX, maxY);
                }
                else {
                    min = nearest(node.rt, query, min, true, minX, node.point.y(), maxX, maxY);
                    min = nearest(node.lb, query, min, true, minX, minY, maxX, node.point.y());
                }
            }

        }
        return min;
    }

    private void throwIfNull(Object... obj) {
        for (Object a : obj) {
            if (a == null) throw new IllegalArgumentException("Argument(s) can't be null");
        }
    }

    // // unit testing of the methods (optional)
    // public static void main(String[] args) {
    //     KdTree kdtree = new KdTree();
    //     In in = new In(args[0]);
    //
    //     while (!in.isEmpty()) {
    //         double x = in.readDouble();
    //         double y = in.readDouble();
    //         Point2D point = new Point2D(x, y);
    //         kdtree.insert(point);
    //         // StdOut.println("Inserted (" + x + "," + y + ")");
    //     }
    //     kdtree.draw();
    // }
}
