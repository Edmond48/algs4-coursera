/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public final class WordNet {

    // a data structure to look up synsets with their id
    private String[] synsets;

    // a data structure to look up nouns
    private final RedBlackBST<String, Bag<Integer>> nounTree
            = new RedBlackBST<String, Bag<Integer>>();

    // graph for hypernyms
    private Digraph hypernyms;

    // sap ds
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        throwIfNull(synsets, hypernyms);

        // make DS for synsets and noun
        int n = makeSyn(synsets);

        // make a graph of hypernyms
        makeHyp(hypernyms, n);

        // sap ds
        this.sap = new SAP(this.hypernyms);
    }

    // helper method to build DS for nouns and synsets
    private int makeSyn(String syn) {

        // input stream for two files
        In synIn = new In(syn);

        String[] synAll = synIn.readAllLines();

        int n = synAll.length;

        // initialize array
        this.synsets = new String[n];

        // put the nouns into a Red-Black BST
        for (int i = 0; i < n; i++) {
            String line = synAll[i];
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);

            this.synsets[id] = fields[1];

            String[] nouns = fields[1].split(" ");
            for (int j = 0; j < nouns.length; j++) {
                String noun = nouns[j];
                if (nounTree.contains(noun)) {
                    nounTree.get(noun).add(id);
                }
                else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    nounTree.put(noun, bag);
                }
            }
        }
        return n;
    }

    // helper method for graph construction
    private void makeHyp(String hyp, int n) {

        In hypIn = new In(hyp);

        this.hypernyms = new Digraph(n);

        while (hypIn.hasNextLine()) {
            String line = hypIn.readLine();
            String[] fields = line.split(",");

            int id = Integer.parseInt(fields[0]);
            for (int j = 1; j < fields.length; j++) {
                this.hypernyms.addEdge(id, Integer.parseInt(fields[j]));
            }
        }

        // check the graph for topological order
        Topological finder = new Topological(this.hypernyms);
        if (!finder.hasOrder())
            throw new IllegalArgumentException("Graph not a rooted DAG");
        Iterable<Integer> iterator = finder.order();
        int count = 0;
        for (int a : iterator) {
            if (hypernyms.outdegree(a) == 0) count++;
            if (count > 1)
                throw new IllegalArgumentException("Graph not a rooted DAG");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Queue<String> iterator = new Queue<String>();
        for (String a : nounTree.keys()) {
            iterator.enqueue(a);
        }
        return iterator;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounTree.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        throwIfNotNoun(nounA, nounB);
        return sap.length(nounTree.get(nounA), nounTree.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        throwIfNotNoun(nounA, nounB);
        int id = sap.ancestor(nounTree.get(nounA), nounTree.get(nounB));
        if (id != -1) return synsets[id];
        else return null;
    }

    private void throwIfNotNoun(String... str) {
        for (String s : str) {
            if (!isNoun(s)) throw new IllegalArgumentException("Not a noun in WordNet!");
        }
    }

    private void throwIfNull(Object... obj) {
        for (Object a : obj) {
            if (a == null) throw new IllegalArgumentException("Argument can't be null.");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        while (!StdIn.isEmpty()) {
            try {
                String nounA = StdIn.readString();
                String nounB = StdIn.readString();
                int length = wordnet.distance(nounA, nounB);
                String sap = wordnet.sap(nounA, nounB);
                StdOut.printf("distance: %d, sap: %s\n", length, sap);
            }
            catch (IllegalArgumentException e) {
                StdOut.println(e);
            }
        }
    }
}
