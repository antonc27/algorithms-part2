/* *****************************************************************************
 *  Name: MoveToFront
 *  Date: 15/02/2020
 *  Description: Maintains an ordered sequence of the characters in the alphabet
 *  by repeatedly reading a character from the input message;
 *  printing the position in the sequence in which that character appears;
 *  and moving that character to the front of the sequence.
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int r = 256;
        char[] seq = new char[r];
        for (int i = 0; i < r; i++) {
            seq[i] = (char) (i & 0xff);
        }

        while (!BinaryStdIn.isEmpty()) {
            char nextC = BinaryStdIn.readChar();

            int idx = -1;
            for (int i = 0; i < r; i++) {
                if (seq[i] == nextC) {
                    idx = i;
                    break;
                }
            }

            BinaryStdOut.write((char) (idx & 0xff));

            for (int i = idx; i > 0; i--) {
                char tmp = seq[i];
                seq[i] = seq[i - 1];
                seq[i - 1] = tmp;
            }
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int r = 256;
        char[] seq = new char[r];
        for (int i = 0; i < r; i++) {
            seq[i] = (char) (i & 0xff);
        }

        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readChar();

            BinaryStdOut.write(seq[idx]);

            for (int i = idx; i > 0; i--) {
                char tmp = seq[i];
                seq[i] = seq[i - 1];
                seq[i - 1] = tmp;
            }
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
