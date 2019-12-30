/* *****************************************************************************
 *  Name: SeamCarver
 *  Date: 30/12/19
 *  Description: Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time.
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

    }

    // current picture
    public Picture picture() {
        return new Picture(1, 1);
    }

    // width of current picture
    public int width() {
        return 1;
    }

    // height of current picture
    public int height() {
        return 1;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return 0.0;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return new int[1];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return new int[1];
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
