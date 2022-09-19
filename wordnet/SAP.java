/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description: basic concept of every block of length and ancestor is do BFS for both sources until we meet a common point
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public final class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        throwIfNull(v, w);
        int n = G.V();
        boolean[] vMarked = new boolean[n];
        boolean[] wMarked = new boolean[n];
        int[] vDis = new int[n];
        int[] wDis = new int[n];
        Queue<Integer> vq = new Queue<Integer>();
        Queue<Integer> wq = new Queue<Integer>();
        vMarked[v] = true;
        wMarked[w] = true;
        vDis[v] = 0;
        wDis[w] = 0;
        vq.enqueue(v);
        wq.enqueue(w);
        int lastV = 0;
        int lastW = 0;
        int min = Integer.MAX_VALUE;
        while (!vq.isEmpty() || !wq.isEmpty()) {
            if (!vq.isEmpty()) {
                int vd = vq.dequeue();
                if (vDis[vd] > min && lastW > min) return min;
                lastV = vDis[vd];
                int path = lastV + wDis[vd];
                if (wMarked[vd] && path < min) min = path;
                for (int va : G.adj(vd)) {
                    if (!vMarked[va]) {
                        vMarked[va] = true;
                        vDis[va] = vDis[vd] + 1;
                        vq.enqueue(va);
                    }
                }
            }
            if (!wq.isEmpty()) {
                int wd = wq.dequeue();
                if (wDis[wd] > min && lastV > min) return min;
                lastW = wDis[wd];
                int path = vDis[wd] + lastW;
                if (vMarked[wd] && path < min) min = path;
                for (int wa : G.adj(wd)) {
                    if (!wMarked[wa]) {
                        wMarked[wa] = true;
                        wDis[wa] = wDis[wd] + 1;
                        wq.enqueue(wa);
                    }
                }
            }
        }
        if (min != Integer.MAX_VALUE) return min;
        else return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        throwIfNull(v, w);
        int n = G.V();
        boolean[] vMarked = new boolean[n];
        boolean[] wMarked = new boolean[n];
        int[] vDis = new int[n];
        int[] wDis = new int[n];
        Queue<Integer> vq = new Queue<Integer>();
        Queue<Integer> wq = new Queue<Integer>();
        vMarked[v] = true;
        wMarked[w] = true;
        vDis[v] = 0;
        wDis[w] = 0;
        vq.enqueue(v);
        wq.enqueue(w);
        int min = Integer.MAX_VALUE;
        int minID = 0;
        int lastV = 0;
        int lastW = 0;
        while (!vq.isEmpty() || !wq.isEmpty()) {
            if (!vq.isEmpty()) {
                int vd = vq.dequeue();
                if (vDis[vd] > min && lastW > min) return minID;
                lastV = vDis[vd];
                int path = lastV + wDis[vd];
                if (wMarked[vd] && path < min) {
                    min = path;
                    minID = vd;
                }
                for (int va : G.adj(vd)) {
                    if (!vMarked[va]) {
                        vMarked[va] = true;
                        vDis[va] = vDis[vd] + 1;
                        vq.enqueue(va);
                    }
                }
            }
            if (!wq.isEmpty()) {
                int wd = wq.dequeue();
                if (wDis[wd] > min && lastV > min) return minID;
                lastW = wDis[wd];
                int path = vDis[wd] + lastW;
                if (vMarked[wd] && path < min) {
                    min = path;
                    minID = wd;
                }
                for (int wa : G.adj(wd)) {
                    if (!wMarked[wa]) {
                        wMarked[wa] = true;
                        wDis[wa] = wDis[wd] + 1;
                        wq.enqueue(wa);
                    }
                }
            }
        }
        if (min != Integer.MAX_VALUE) return minID;
        else return -1;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        throwIfNullIterable(v, w);
        int n = G.V();
        boolean[] vMarked = new boolean[n];
        boolean[] wMarked = new boolean[n];
        int[] vDis = new int[n];
        int[] wDis = new int[n];
        Queue<Integer> vq = new Queue<Integer>();
        Queue<Integer> wq = new Queue<Integer>();
        for (int vi : v) {
            vMarked[vi] = true;
            vDis[vi] = 0;
            vq.enqueue(vi);
        }
        for (int wi : w) {
            wMarked[wi] = true;
            wDis[wi] = 0;
            wq.enqueue(wi);
        }
        int lastV = 0;
        int lastW = 0;
        int min = Integer.MAX_VALUE;
        while (!vq.isEmpty() || !wq.isEmpty()) {
            if (!vq.isEmpty()) {
                int vd = vq.dequeue();
                if (vDis[vd] > min && lastW > min) return min;
                lastV = vDis[vd];
                int path = lastV + wDis[vd];
                if (wMarked[vd] && path < min) min = path;
                for (int va : G.adj(vd)) {
                    if (!vMarked[va]) {
                        vMarked[va] = true;
                        vDis[va] = vDis[vd] + 1;
                        vq.enqueue(va);
                    }
                }
            }
            if (!wq.isEmpty()) {
                int wd = wq.dequeue();
                if (wDis[wd] > min && lastV > min) return min;
                lastW = wDis[wd];
                int path = vDis[wd] + lastW;
                if (vMarked[wd] && path < min) min = path;
                for (int wa : G.adj(wd)) {
                    if (!wMarked[wa]) {
                        wMarked[wa] = true;
                        wDis[wa] = wDis[wd] + 1;
                        wq.enqueue(wa);
                    }
                }
            }
        }
        if (min != Integer.MAX_VALUE) return min;
        else return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        throwIfNullIterable(v, w);
        int n = G.V();
        boolean[] vMarked = new boolean[n];
        boolean[] wMarked = new boolean[n];
        int[] vDis = new int[n];
        int[] wDis = new int[n];
        Queue<Integer> vq = new Queue<Integer>();
        Queue<Integer> wq = new Queue<Integer>();
        for (int vi : v) {
            vMarked[vi] = true;
            vDis[vi] = 0;
            vq.enqueue(vi);
        }
        for (int wi : w) {
            wMarked[wi] = true;
            wDis[wi] = 0;
            wq.enqueue(wi);
        }
        int min = Integer.MAX_VALUE;
        int minID = 0;
        int lastV = 0;
        int lastW = 0;
        while (!vq.isEmpty() || !wq.isEmpty()) {
            if (!vq.isEmpty()) {
                int vd = vq.dequeue();
                if (vDis[vd] > min && lastW > min) return minID;
                lastV = vDis[vd];
                int path = lastV + wDis[vd];
                if (wMarked[vd] && path < min) {
                    min = path;
                    minID = vd;
                }
                for (int va : G.adj(vd)) {
                    if (!vMarked[va]) {
                        vMarked[va] = true;
                        vDis[va] = vDis[vd] + 1;
                        vq.enqueue(va);
                    }
                }
            }
            if (!wq.isEmpty()) {
                int wd = wq.dequeue();
                if (wDis[wd] > min && lastV > min) return minID;
                lastW = wDis[wd];
                int path = vDis[wd] + lastW;
                if (vMarked[wd] && path < min) {
                    min = path;
                    minID = wd;
                }
                for (int wa : G.adj(wd)) {
                    if (!wMarked[wa]) {
                        wMarked[wa] = true;
                        wDis[wa] = wDis[wd] + 1;
                        wq.enqueue(wa);
                    }
                }
            }
        }
        if (min != Integer.MAX_VALUE) return minID;
        else return -1;
    }

    private void throwIfNull(Integer... obj) {
        int ver = G.V();
        for (Integer a : obj) {
            if (a == null || a < 0 || a >= ver)
                throw new IllegalArgumentException("Argument can't be null.");
        }
    }

    @SafeVarargs
    private final void throwIfNullIterable(Iterable<Integer>... obj) {
        int ver = G.V();
        for (Iterable<Integer> a : obj) {
            if (a == null) throw new IllegalArgumentException("Argument is null");
            for (Integer v : a) {
                if (v == null || v < 0 || v >= ver)
                    throw new IllegalArgumentException("Invalid Vertex");
            }
        }
    }

    // do unit testing of this class
    // takes a filename as cmd line argument pair of numbers from standard input and output the length and ancestor
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}













