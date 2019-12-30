/* *****************************************************************************
 *  Name: SeamCarver
 *  Date: 30/12/19
 *  Description: Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time.
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Null picture");

        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IllegalArgumentException("Indices out of bounds");
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000.0;
        return Math.sqrt(xGradient(x, y) + yGradient(x, y));
    }

    private double xGradient(int x, int y) {
        return gradient(x - 1, y, x + 1, y);
    }

    private double yGradient(int x, int y) {
        return gradient(x, y - 1, x, y + 1);
    }

    private double gradient(int x1, int y1, int x2, int y2) {
        int before = picture.getRGB(x1, y1);
        int r = (before >> 16) & 0xFF;
        int g = (before >>  8) & 0xFF;
        int b = (before >>  0) & 0xFF;

        int after = picture.getRGB(x2, y2);
        int rr = (after >> 16) & 0xFF;
        int gg = (after >>  8) & 0xFF;
        int bb = (after >>  0) & 0xFF;

        int dR = r - rr;
        int dG = g - gg;
        int dB = b - bb;

        return dR*dR + dG*dG + dB*dB;
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

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException(msg);
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture testPicture = new Picture(3, 4);
        testPicture.set(0, 0, new Color(255, 101, 51));
        testPicture.set(1, 0, new Color(255, 101, 153));
        testPicture.set(2, 0, new Color(255, 101, 255));

        testPicture.set(0, 1, new Color(255, 153, 51));
        testPicture.set(1, 1, new Color(255, 153, 153));
        testPicture.set(2, 1, new Color(255, 153, 255));

        testPicture.set(0, 2, new Color(255, 203, 51));
        testPicture.set(1, 2, new Color(255, 204, 153));
        testPicture.set(2, 2, new Color(255, 205, 255));

        testPicture.set(0, 3, new Color(255, 255, 51));
        testPicture.set(1, 3, new Color(255, 255, 153));
        testPicture.set(2, 3, new Color(255, 255, 255));

        SeamCarver sc = new SeamCarver(testPicture);

        assertTrue(sc.height() == 4, "SeamCarver height");
        assertTrue(sc.width() == 3, "SeamCarver width");

        assertTrue(sc.energy(0, 0) == 1000.0, "Energy of (0, 0)");
        assertTrue(Math.pow(sc.energy(1, 2), 2) - 52024.0 < 0.001, "Energy of (1, 2)");
    }
}
