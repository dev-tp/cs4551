package homework3;

import utils.Image;

class DctCompression {

    private Image resizedImage;
    private int originalHeight;
    private int originalWidth;

    Image compress(String imagePath) throws Exception {
        Image image = new Image(imagePath);

        originalHeight = image.getHeight();
        originalWidth = image.getWidth();

        int width = originalWidth % 8;

        if (width != 0) {
            width = (8 - width) + originalWidth;
        } else {
            width = originalWidth;
        }

        int height = originalHeight % 8;

        if (height != 0) {
            height = (8 - height) + originalHeight;
        } else {
            height = originalHeight;
        }

        resizedImage = new Image(width, height);

        int[] rgb = new int[3];

        for (int y = 0; y < originalHeight; y++) {
            for (int x = 0; x < originalWidth; x++) {
                image.getPixel(x, y, rgb);
                resizedImage.setPixel(x, y, rgbToYCbCr(rgb));
            }
        }

        return resizedImage;
    }

    private int[] rgbToYCbCr(int[] rgb) {
        int[] ycbcr = new int[3];

        ycbcr[0] = (int) Math.round((0.2990 * rgb[0] + 0.5870 * rgb[1] + 0.1140 * rgb[2]) - 128);
        ycbcr[1] = (int) Math.round((-0.1687 * rgb[0] - 0.3313 * rgb[1] + 0.5000 * rgb[2]) - 0.5);
        ycbcr[2] = (int) Math.round((0.5000 * rgb[0] - 0.4187 * rgb[1] - 0.0813 * rgb[2]) - 0.5);

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

        rgb[0] = (int) Math.round((ycbcr[0] + 1.4020 * ycbcr[2]) + 128);
        rgb[1] = (int) Math.round((ycbcr[0] - 0.3441 * ycbcr[1] - 0.7141 * ycbcr[2]) + 0.5);
        rgb[2] = (int) Math.round((ycbcr[0] + 1.7720 * ycbcr[1]) + 0.5);

        return rgb;
    }
}
