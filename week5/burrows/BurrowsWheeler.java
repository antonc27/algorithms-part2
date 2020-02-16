/* *****************************************************************************
 *  Name: BurrowsWheeler
 *  Date: 15/02/2020
 *  Description: The Burrows–Wheeler transform rearranges the characters in
 * the input so that there are lots of clusters with repeated characters,
 * but in such a way that it is still possible to recover the original input.
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char nextChar = BinaryStdIn.readChar();
            sb.append(nextChar);
        }
        String input = sb.toString();

        CircularSuffixArray csa = new CircularSuffixArray(input);
        int n = csa.length();

        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            char toWrite = input.charAt((csa.index(i) + n - 1) % n);
            BinaryStdOut.write(toWrite);
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();

        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char nextChar = BinaryStdIn.readChar();
            sb.append(nextChar);
        }
        String input = sb.toString();

        int[] counts = new int[257];
        for (int i = 0; i < input.length(); i++) {
            counts[input.charAt(i) + 1]++;
        }

        for (int i = 0; i < 256; i++) {
            counts[i + 1] += counts[i];
        }

        int[] next = new int[input.length()];
        char[] sorted = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int idx = counts[c];

            sorted[idx] = c;
            next[idx] = i;

            counts[c]++;
        }

        for (int i = 0; i < input.length(); i++) {
            BinaryStdOut.write(sorted[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
