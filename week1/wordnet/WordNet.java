/* *****************************************************************************
 *  Name: WordNet
 *  Date: 07/12/19
 *  Description: WordNet is a semantic lexicon for the English language.
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class WordNet {

    private Map<String, LinkedList<Integer>> nounsToSynsetIds;
    private Map<Integer, String> synsetIdToSynset;
    private int synsetsCount;

    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("Null synsets or hypernyms filename(s)");
        initSynsets(synsets);
        initHypernyms(hypernyms);
    }

    private void initSynsets(String synsets) {
        nounsToSynsetIds = new HashMap<>();
        synsetIdToSynset = new HashMap<>();
        synsetsCount = 0;

        In in = new In(synsets);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splitted = line.split(",");
            int synsetId = Integer.parseInt(splitted[0]);
            String synset = splitted[1];
            synsetIdToSynset.put(synsetId, synset);

            String[] nouns = synset.split(" ");
            for (String noun : nouns) {
                LinkedList<Integer> old = nounsToSynsetIds.getOrDefault(noun, new LinkedList<>());
                old.add(synsetId);
                nounsToSynsetIds.put(noun, old);
            }
            synsetsCount++;
        }
    }

    private void initHypernyms(String hypernyms) {
        Digraph digraph = new Digraph(synsetsCount);

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

        if (!isDAG(digraph) || !isSingleRooted(digraph)) throw new IllegalArgumentException("Hypernyms is not rooted DAG");

        sap = new SAP(digraph);
    }

    private boolean isDAG(Digraph digraph) {
        DirectedCycle dc = new DirectedCycle(digraph);
        return !dc.hasCycle();
    }

    private boolean isSingleRooted(Digraph digraph) {
        int rootsCount = 0;
        for (int v = 0; v < digraph.V(); v++) {
            if (digraph.outdegree(v) == 0) rootsCount++;
        }
        return rootsCount == 1;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsToSynsetIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Null word");
        return nounsToSynsetIds.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Bad nouns");
        LinkedList<Integer> a = nounsToSynsetIds.get(nounA);
        LinkedList<Integer> b = nounsToSynsetIds.get(nounB);
        return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Bad nouns");
        LinkedList<Integer> a = nounsToSynsetIds.get(nounA);
        LinkedList<Integer> b = nounsToSynsetIds.get(nounB);
        int ancestorId = sap.ancestor(a, b);
        return synsetIdToSynset.get(ancestorId);
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
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        assertTrue(count(wn.nouns()) == 119188, "119,188 nouns in synsets");

        assertTrue(wn.synsetsCount == 82192, "82,192 vertices in hypernyms");

        assertTrue(wn.distance("white_marlin", "mileage") == 23, "(distance = 23) white_marlin, mileage");
        assertTrue(wn.distance("Black_Plague", "black_marlin") == 33, "(distance = 33) Black_Plague, black_marlin");
        assertTrue(wn.distance("American_water_spaniel", "histology") == 27, "(distance = 27) American_water_spaniel, histology");
        assertTrue(wn.distance("Brown_Swiss", "barrel_roll") == 29, "(distance = 29) Brown_Swiss, barrel_roll");

        assertTrue(wn.sap("individual", "edible_fruit").equals("physical_entity"), "The synsets individual and edible_fruit have common ancestor physical_entity");
    }
}
