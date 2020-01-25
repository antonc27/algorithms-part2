/* *****************************************************************************
 *  Name: BoggleSolver
 *  Date: 25/02/2020
 *  Description: Finds all valid words in a given Boggle board, using a given dictionary.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieST;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver
{
    private TrieST<Boolean> trie = new TrieST<>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            trie.put(word, Boolean.TRUE);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> found = new HashSet<>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                searchWords(board, row, col, found);
            }
        }
        return found;
    }

    private void searchWords(BoggleBoard board, int startRow, int startCol, Set<String> found) {
        boolean[][] seen = new boolean[board.rows()][board.cols()];
        seen[startRow][startCol] = true;
        searchWordsRec(board, startRow, startCol, found, seen, "" + board.getLetter(startRow, startCol));
    }

    private void searchWordsRec(BoggleBoard board, int row, int col, Set<String> found, boolean[][] seen, String current) {
        if (current.length() >= 3) {
            if (isInDictionary(current)) {
                found.add(current);
            }
            if (!trie.keysWithPrefix(current).iterator().hasNext()) return;
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (!isValidIndex(board, row+i, col+j)) continue;
                if (seen[row+i][col+j]) continue;

                seen[row+i][col+j] = true;
                searchWordsRec(board, row+i, col+j, found, seen, current + board.getLetter(row+i, col+j));
                seen[row+i][col+j] = false;
            }
        }
    }

    private boolean isValidIndex(BoggleBoard board, int row, int col) {
        return 0 <= row && row < board.rows() && 0 <= col && col < board.cols();
    }

    private boolean isInDictionary(String word) {
        return trie.contains(word) && trie.get(word);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!isInDictionary(word)) return 0;
        int len = word.length();
        if (len <= 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
