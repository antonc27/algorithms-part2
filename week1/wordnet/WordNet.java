/* *****************************************************************************
 *  Name: WordNet
 *  Date: 07/12/19
 *  Description: WordNet is a semantic lexicon for the English language.
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class WordNet {

    private Map<String, LinkedList<Integer>> nounsToSynsetIds;
    private int synsetsCount;
    private Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        initSynsets(synsets);
        initHypernyms(hypernyms);
    }

    private void initSynsets(String synsets) {
        nounsToSynsetIds = new HashMap<>();
        synsetsCount = 0;

        In in = new In(synsets);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splitted = line.split(",");
            int synsetId = Integer.parseInt(splitted[0]);
            String[] nouns = splitted[1].split(" ");
            for (String noun : nouns) {
                LinkedList<Integer> old = nounsToSynsetIds.getOrDefault(noun, new LinkedList<>());
                old.add(synsetId);
                nounsToSynsetIds.put(noun, old);
            }
            synsetsCount++;
        }
    }

    private void initHypernyms(String hypernyms) {
        digraph = new Digraph(synsetsCount);

        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splitted = line.split(",", 2);

            int id = Integer.parseInt(splitted[0]);
            if (splitted.length > 1) {
                for (String v : splitted[1].split(",")) {
                    int vv = Integer.parseInt(v);

                    digraph.addEdge(id, vv);
                }
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsToSynsetIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounsToSynsetIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return "";
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException(msg);
        }
    }

    private static int count(Iterable<String> iter) {
        int count = 0;
        for (String ignored : iter) {
            count++;
        }
        return count;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn1 = new WordNet("synsets3.txt", "hypernyms3InvalidTwoRoots.txt");

        assertTrue(wn1.isNoun("a"), "a is a synset");
        assertTrue(!wn1.isNoun("d"), "d is not a synset");

        assertTrue(count(wn1.nouns()) == 3, "3 nouns in synsets3");

        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        assertTrue(count(wn.nouns()) == 119188, "119,188 nouns in synsets");

        assertTrue(wn.digraph.V() == 82192, "82,192 vertices in hypernyms");
        assertTrue(wn.digraph.E() == 84505, "84,505 edges in hypernyms");
    }
}
