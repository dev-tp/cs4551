package homework4;

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

    private static Number[] motionVectorAndMinCost(double[][] costs) {
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
        int height = targetImage.getHeight();
        int width = targetImage.getWidth();

        double[][] costs = new double[2 * searchWindow + 1][2 * searchWindow + 1];

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

                Number[] min = motionVectorAndMinCost(costs);

                System.out.println((min[0].intValue() - searchWindow) + " " + (min[1].intValue() - searchWindow) + " "
                        + min[2]);

                resetCostValues(costs);
            }
        }
    }
}
