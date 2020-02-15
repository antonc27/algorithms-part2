/* *****************************************************************************
 *  Name: CircularSuffixArray
 *  Date: 15/02/2020
 *  Description: The abstraction of a sorted array of the n circular suffixes of a string of length n.
 **************************************************************************** */

import java.util.Arrays;

public class CircularSuffixArray {

    private String input;
    private CircularSuffix[] suffixes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Null input string");

        input = s;

        suffixes = new CircularSuffix[input.length()];
        for (int i = 0; i < input.length(); i++) {
            suffixes[i] = new CircularSuffix(i);
        }

        Arrays.sort(suffixes);
    }

    // length of s
    public int length() {
        return input.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (!(0 <= i && i < input.length())) throw new IllegalArgumentException("Index out of bounds");
        return suffixes[i].offset;
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException(msg);
        }
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        int offset;

        CircularSuffix(int offset) {
            this.offset = offset;
        }

        public int compareTo(CircularSuffix other) {
            for (int i = 0; i < input.length(); i++) {
                char c = charAt(i);
                char otherC = other.charAt(i);
                if (c < otherC) return -1;
                if (c > otherC) return +1;
            }
            return 0;
        }

        private char charAt(int i) {
            return input.charAt((offset + i) % input.length());
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");

        assertTrue(csa.length() == 12, "length");

        assertTrue(csa.index(0) == 11, "index[0]");
        assertTrue(csa.index(1) == 10, "index[1]");
        assertTrue(csa.index(2) == 7, "index[2]");
        assertTrue(csa.index(3) == 0, "index[3]");
        assertTrue(csa.index(4) == 3, "index[4]");
        assertTrue(csa.index(5) == 5, "index[5]");
        assertTrue(csa.index(6) == 8, "index[6]");
        assertTrue(csa.index(7) == 1, "index[7]");
        assertTrue(csa.index(8) == 4, "index[8]");
        assertTrue(csa.index(9) == 6, "index[9]");
        assertTrue(csa.index(10) == 9, "index[10]");
        assertTrue(csa.index(11) == 2, "index[11]");
    }
}
