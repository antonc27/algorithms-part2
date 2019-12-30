/* *****************************************************************************
 *  Name: SeamCarver
 *  Date: 30/12/19
 *  Description: Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time.
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

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
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void transpose() {
        int w = width();
        int h = height();

        Picture tmp = new Picture(h, w);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                tmp.setRGB(j, i, picture.getRGB(i, j));
            }
        }

        picture = tmp;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int w = width();
        int h = height();

        double[][] distTo = new double[w][h];
        for (int i = 0; i < w; i++) {
            distTo[i][0] = 1000.0;
            for (int j = 1; j < h; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        int[][] edgeTo = new int[w][h];
        for (int i = 0; i < w; i++) {
            edgeTo[i][0] = -1;
        }

        double[][] energy = new double[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                energy[i][j] = energy(i, j);
            }
        }

        for (int y = 0; y < h-1; y++) {
            for (int x = 0; x < w; x++) {
                for (int k = -1; k <= 1; k++) {
                    int t = x+k;
                    if (t < 0 || t >= w) continue;

                    if (distTo[t][y + 1] > distTo[x][y] + energy[t][y + 1]) {
                        distTo[t][y + 1] = distTo[x][y] + energy[t][y + 1];
                        edgeTo[t][y + 1] = x;
                    }
                }
            }
        }

        int minPathIdx = -1;
        double minPathVal = Double.POSITIVE_INFINITY;

        for (int i = 0; i < w; i++) {
            if (distTo[i][h - 1] < minPathVal) {
                minPathVal = distTo[i][h - 1];
                minPathIdx = i;
            }
        }

        int[] seam = new int[h];
        for (int j = h-1; j >= 0; j--) {
            seam[j] = minPathIdx;
            minPathIdx = edgeTo[minPathIdx][j];
        }

        return seam;
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
        Picture testPicture = new Picture("3x4.png");
        SeamCarver sc1 = new SeamCarver(testPicture);

        assertTrue(sc1.height() == 4, "SeamCarver height");
        assertTrue(sc1.width() == 3, "SeamCarver width");

        assertTrue(sc1.energy(0, 0) == 1000.0, "Energy of (0, 0)");
        assertTrue(Math.pow(sc1.energy(1, 2), 2) - 52024.0 < 0.001, "Energy of (1, 2)");

        Picture testPicture2 = new Picture("6x5.png");
        SeamCarver sc2 = new SeamCarver(testPicture2);

        assertTrue(Arrays.equals(sc2.findVerticalSeam(), new int[] { 3, 4, 3, 2, 1 }), "Vertical seam");
        assertTrue(Arrays.equals(sc2.findHorizontalSeam(), new int[] { 1, 2, 1, 2, 1, 0 }), "Horizontal seam");
    }
}
