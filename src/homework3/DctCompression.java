package homework3;

import utils.Image;

class DctCompression {

    private Image resizedImage;
    private int originalHeight;
    private int originalWidth;

    private int addPadding(int length) {
        int newLength = length % 8;
        return newLength != 0 ? (8 - newLength) + length : length;
    }

    private double[][] applyDctOnChannel(double[][] channel, int width, int height) {
        double[][] dctMatrix = new double[width][height];

        double minValue = -1024.0;  // -2^10
        double maxValue = 1024.0;  // 2^10

        // Divide channel into 8x8 sections
        for (int y = 0; y < height; y += 8) {
            for (int x = 0; x < width; x += 8) {

                // Apply DCT on channel
                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {
                        double sum = 0.0;

                        for (int i = x; i < x + 8; i++) {
                            for (int j = y; j < y + 8; j++) {
                                double a = Math.cos((2 * i + 1) * u * Math.PI / 16);
                                double b = Math.cos((2 * j + 1) * v * Math.PI / 16);

                                sum += a * b * (channel[i][j] - 128);
                            }
                        }

                        double c = ((u == 0 ? Math.sqrt(2.0) / 2 : 1) * (v == 0 ? Math.sqrt(2.0) / 2 : 1)) / 4.0;

                        sum *= c;

                        if (sum < minValue) {
                            sum = minValue;
                        } else if (sum > maxValue) {
                            sum = maxValue;
                        }

                        dctMatrix[x + u][y + v] = sum;
                    }
                }
            }
        }

        return dctMatrix;
    }

    Image compress(String imagePath) throws Exception {
        Image image = new Image(imagePath);

        originalHeight = image.getHeight();
        originalWidth = image.getWidth();

        int width = addPadding(originalWidth);
        int height = addPadding(originalHeight);

        resizedImage = new Image(width, height);

        int[] rgb = new int[3];
        int[] ycbcr = new int[3];

        double[][] yChannel = new double[width][height];

        for (int y = 0; y < originalHeight; y++) {
            for (int x = 0; x < originalWidth; x++) {
                image.getPixel(x, y, rgb);

                ycbcr = rgbToYCbCr(rgb);
                yChannel[x][y] = ycbcr[0];

                resizedImage.setPixel(x, y, ycbcr);
            }
        }

        int cbCrWidth = addPadding(width / 2);
        int cbCrHeight = addPadding(height / 2);

        double[][] cbChannel = new double[cbCrWidth][cbCrHeight];
        double[][] crChannel = new double[cbCrWidth][cbCrHeight];

        double cbAverage = 0.0;
        double crAverage = 0.0;

        // Chroma Sub-sampling (4:2:0)
        for (int y = 0; y < height; y += 2) {
            for (int x = 0; x < width; x += 2) {
                resizedImage.getPixel(x, y, ycbcr);
                cbAverage += ycbcr[1];
                crAverage += ycbcr[2];

                resizedImage.getPixel(x, y + 1, ycbcr);
                cbAverage += ycbcr[1];
                crAverage += ycbcr[2];

                resizedImage.getPixel(x + 1, y, ycbcr);
                cbAverage += ycbcr[1];
                crAverage += ycbcr[2];

                resizedImage.getPixel(x + 1, y + 1, ycbcr);
                cbAverage += ycbcr[1];
                crAverage += ycbcr[2];

                cbChannel[x / 2][y / 2] = cbAverage / 4.0;
                crChannel[x / 2][y / 2] = crAverage / 4.0;

                cbAverage = 0.0;
                crAverage = 0.0;
            }
        }

        double[][] yChannelDct = applyDctOnChannel(yChannel, width, height);
        double[][] cbChannelDct = applyDctOnChannel(cbChannel, cbCrWidth, cbCrHeight);
        double[][] crChannelDct = applyDctOnChannel(crChannel, cbCrWidth, cbCrHeight);

        return resizedImage;
    }

    private int[] rgbToYCbCr(int[] rgb) {
        int[] ycbcr = new int[3];

        ycbcr[0] = (int) (0.2990 * rgb[0] + 0.5870 * rgb[1] + 0.1140 * rgb[2]);
        ycbcr[1] = (int) (-0.1687 * rgb[0] - 0.3313 * rgb[1] + 0.5000 * rgb[2] + 128.0);
        ycbcr[2] = (int) (0.5000 * rgb[0] - 0.4187 * rgb[1] - 0.0813 * rgb[2] + 128.0);

        for (int i = 0; i < ycbcr.length; i++) {
            if (ycbcr[i] < 0) {
                ycbcr[i] = 0;
            } else if (ycbcr[i] > 255) {
                ycbcr[i] = 255;
            }
        }

        return ycbcr;
    }

    Image restore() {
        Image image = new Image(originalWidth, originalHeight);

        int[] ycbcr = new int[3];

        for (int y = 0; y < originalHeight; y++) {
            for (int x = 0; x < originalWidth; x++) {
                resizedImage.getPixel(x, y, ycbcr);
                image.setPixel(x, y, yCbCrToRgb(ycbcr));
            }
        }

        return image;
    }

    private int[] yCbCrToRgb(int[] ycbcr) {
        int[] rgb = new int[3];

        ycbcr[1] -= 128;
        ycbcr[2] -= 128;

        rgb[0] = (int) (1.000 * ycbcr[0] + 0.0000 * ycbcr[1] + 1.4000 * ycbcr[2]);
        rgb[1] = (int) (1.000 * ycbcr[0] - 0.3430 * ycbcr[1] - 0.7110 * ycbcr[2]);
        rgb[2] = (int) (1.000 * ycbcr[0] + 1.7650 * ycbcr[1] + 0.0000 * ycbcr[2]);

        for (int i = 0; i < rgb.length; i++) {
            if (rgb[i] < 0) {
                rgb[i] = 0;
            } else if (rgb[i] > 255) {
                rgb[i] = 255;
            }
        }

        return rgb;
    }
}
