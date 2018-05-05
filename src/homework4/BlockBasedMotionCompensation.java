package homework4;

import utils.Image;

class BlockBasedMotionCompensation {

    static void routine(Image targetImage, Image referenceImage, int blockSize, int searchWindow) {
        int block = 0;
        int height = targetImage.getHeight();
        int width = targetImage.getWidth();

        int[] grayValues = new int[2];
        int[] rgb = new int[3];
        double[] sumPerBlock = new double[(width / blockSize) * (height / blockSize)];

        // Divide target image into nxn blocks
        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {

                // Apply Mean Square Difference (MSD) on each block
                for (int p = 0; p < blockSize; p++) {
                    for (int q = 0; q < blockSize; q++) {
                        targetImage.getPixel(x + p, y + q, rgb);
                        grayValues[0] = (int) (0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);

                        referenceImage.getPixel(x + p, y + q, rgb);
                        grayValues[1] = (int) (0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);

                        for (int i = 0; i < grayValues.length; i++) {
                            if (grayValues[i] < 0) {
                                grayValues[i] = 0;
                            } else if (grayValues[i] > 255) {
                                grayValues[i] = 255;
                            }
                        }

                        // ΣΣ(A[p, q] - B[p, q])^2
                        sumPerBlock[block] += Math.pow(grayValues[0] - grayValues[1], 2.0);
                    }
                }

                // 1/mn ΣΣ(A[p, q] - B[p, q])^2
                sumPerBlock[block] = (1.0 / (blockSize * blockSize) * sumPerBlock[block++]);
            }
        }

        double min = sumPerBlock[0];
        int index = 0;

        for (int i = 0; i < sumPerBlock.length; i++) {
            if (sumPerBlock[i] < min) {
                min = sumPerBlock[i];
                index = i;
            }
        }

        System.out.println(index + ": " + min);
    }
}
