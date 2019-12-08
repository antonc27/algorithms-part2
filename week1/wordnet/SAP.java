/* *****************************************************************************
 *  Name: SAP
 *  Date: 07/12/19
 *  Description: Shortest ancestral path (SAP) is an ancestral path of minimum total length.
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph original;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        original = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(original, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(original, w);

        int minLength = Integer.MAX_VALUE;
        for (int u = 0; u < original.V(); u++) {
            if (bfsV.hasPathTo(u) && bfsW.hasPathTo(u)) {
                int lengthV = bfsV.distTo(u);
                int lengthW = bfsW.distTo(u);

                if (lengthV + lengthW < minLength) {
                    minLength = lengthV + lengthW;
                }
            }
        }
        return (minLength == Integer.MAX_VALUE) ? -1 : minLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(original, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(original, w);

        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int u = 0; u < original.V(); u++) {
            if (bfsV.hasPathTo(u) && bfsW.hasPathTo(u)) {
                int lengthV = bfsV.distTo(u);
                int lengthW = bfsW.distTo(u);

                if (lengthV + lengthW < minLength) {
                    minLength = lengthV + lengthW;
                    ancestor = u;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(original, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(original, w);

        int minLength = Integer.MAX_VALUE;
        for (int u = 0; u < original.V(); u++) {
            if (bfsV.hasPathTo(u) && bfsW.hasPathTo(u)) {
                int lengthV = bfsV.distTo(u);
                int lengthW = bfsW.distTo(u);

                if (lengthV + lengthW < minLength) {
                    minLength = lengthV + lengthW;
                }
            }
        }
        return (minLength == Integer.MAX_VALUE) ? -1 : minLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(original, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(original, w);

        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int u = 0; u < original.V(); u++) {
            if (bfsV.hasPathTo(u) && bfsW.hasPathTo(u)) {
                int lengthV = bfsV.distTo(u);
                int lengthW = bfsW.distTo(u);

                if (lengthV + lengthW < minLength) {
                    minLength = lengthV + lengthW;
                    ancestor = u;
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
