/* *****************************************************************************
 *  Name: Outcast
 *  Date: 07/12/19
 *  Description: Determines a noun for which sum of distances for other words is max in WordNet
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = Integer.MIN_VALUE;
        String outcast = "";
        for (int i = 0; i < nouns.length; i++) {
            int dist = 0;
            String noun = nouns[i];
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                dist += wordnet.distance(noun, nouns[j]);
            }
            if (dist > maxDist) {
                maxDist = dist;
                outcast = noun;
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
