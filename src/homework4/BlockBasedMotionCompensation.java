package homework4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import utils.Image;

class BlockBasedMotionCompensation {

    private static int[][] macroBlock(Image image, int x0, int x1, int y0, int y1) {
        int m = x1 - x0;
        int n = y1 - y0;

        int[][] macroBlock = new int[m][n];

        int[] rgb = new int[3];

        for (int i = x0; i < x1; i++) {
            for (int j = y0; j < y1; j++) {
                image.getPixel(i, j, rgb);

                int grayValue = (int) (0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);

                if (grayValue < 0) {
                    grayValue = 0;
                } else if (grayValue > 255) {
                    grayValue = 255;
                }

                macroBlock[i % m][j % n] = grayValue;
            }
        }

        return macroBlock;
    }

    private static double meanSquareDifference(int[][] targetBlock, int[][] referenceBlock, int blockSize) {
        double sum = 0.0;

        // ΣΣ(A[p, q] - B[p, q])^2
        for (int p = 0; p < blockSize; p++) {
            for (int q = 0; q < blockSize; q++) {
                sum += Math.pow(targetBlock[p][q] - referenceBlock[p][q], 2.0);
            }
        }

        // 1/mn ΣΣ(A[p, q] - B[p, q])^2
        return sum / (blockSize * blockSize);
    }

    private static Number[] motionVector(double[][] costs) {
        Number[] tuple = {0, 0, costs[0][0]}; // dx, dy, minCost

        for (int x = 0; x < costs.length; x++) {
            for (int y = 0; y < costs[x].length; y++) {
                if (costs[x][y] < tuple[2].doubleValue()) {
                    tuple[0] = x;
                    tuple[1] = y;
                    tuple[2] = costs[x][y];
                }
            }
        }

        return tuple;
    }

    private static void resetCostValues(double[][] costs) {
        for (int x = 0; x < costs.length; x++) {
            for (int y = 0; y < costs[x].length; y++) {
                costs[x][y] = 0.0;
            }
        }
    }

    static void routine(Image targetImage, Image referenceImage, int blockSize, int searchWindow) {
        int block = 0;
        int height = targetImage.getHeight();
        int width = targetImage.getWidth();

        double[][] costs = new double[2 * searchWindow + 1][2 * searchWindow + 1];
        Number[][] motionVectors = new Number[(width / blockSize) * (height / blockSize)][3];

        // Divide target image into nxn blocks
        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {

                int[][] a = macroBlock(targetImage, x, x + blockSize, y, y + blockSize);

                for (int m = -searchWindow; m <= searchWindow; m++) {
                    for (int n = -searchWindow; n <= searchWindow; n++) {
                        int refX = x + m;
                        int refY = y + n;

                        if (refX < 0 || refX + blockSize > width || refY < 0 || refY + blockSize > height) {
                            continue;
                        }

                        int[][] b = macroBlock(referenceImage, refX, refX + blockSize, refY, refY + blockSize);

                        costs[m + searchWindow][n + searchWindow] = meanSquareDifference(a, b, blockSize);
                    }
                }

                motionVectors[block++] = motionVector(costs);
                resetCostValues(costs);
            }
        }

        writeMotionVectorsToFile(motionVectors, targetImage, referenceImage, blockSize, searchWindow);
    }

    private static void writeMotionVectorsToFile(
            Number[][] motionVectors, Image targetImage, Image referenceImage, int blockSize, int searchWindow) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/mv.txt"));

            writer.write("# Name:");
            writer.newLine();

            writer.write("# Target Image: " + targetImage.getFileName());
            writer.newLine();

            writer.write("# Reference Image: " + referenceImage.getFileName());
            writer.newLine();

            int width = targetImage.getWidth();
            int height = targetImage.getHeight();

            int columnSize = width / blockSize;
            int rowSize = height / blockSize;

            writer.write("# Number of macro blocks: " + columnSize + " x " + rowSize);
            writer.write(" (Original image is " + width + " x " + height + ")");
            writer.newLine();

            for (int i = 0; i < motionVectors.length; i++) {
                if (i % columnSize != 0) {
                    int dx = motionVectors[i][0].intValue() - searchWindow;
                    int dy = motionVectors[i][1].intValue() - searchWindow;

                    writer.write("[" + dx + " " + dy + "] ");
                } else {
                    writer.newLine();
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
