package homework3;

import utils.Image;

class DctCompression {

    private Image image;

    DctCompression(String imagePath) throws Exception {
        image = new Image(imagePath);
    }

    private int[] rgbToYCbCr(int[] rgb) {
        int[] ycbcr = new int[3];

        ycbcr[0] = (int) Math.round((0.2990 * rgb[0] + 0.5870 * rgb[1] + 0.1140 * rgb[2])); // - 128);
        ycbcr[1] = (int) Math.round((-0.1687 * rgb[0] - 0.3313 * rgb[1] + 0.5000 * rgb[2])); // - 0.5);
        ycbcr[2] = (int) Math.round((0.5000 * rgb[0] - 0.4187 * rgb[1] - 0.0813 * rgb[2])); // - 0.5);

        return ycbcr;
    }

    void resize() {
        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = width % 8;

        if (newWidth != 0) {
            newWidth = (8 - newWidth) + width;
        } else {
            newWidth = width;
        }

        int newHeight = height % 8;

        if (newHeight != 0) {
            newHeight = (8 - newHeight) + height;
        } else {
            newHeight = height;
        }

        Image resizedImage = new Image(newWidth, newHeight);

        int[] rgb = new int[3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.getPixel(x, y, rgb);
                resizedImage.setPixel(x, y, yCbCrToRgb(rgbToYCbCr(rgb)));
            }
        }

        resizedImage.display();
    }

    private int[] yCbCrToRgb(int[] ycbcr) {
        int[] rgb = new int[3];

        rgb[0] = (int) Math.round((ycbcr[0] + 1.4020 * ycbcr[2])); // + 128);
        rgb[1] = (int) Math.round((ycbcr[0] - 0.3441 * ycbcr[1] - 0.7141 * ycbcr[2])); // + 0.5);
        rgb[2] = (int) Math.round((ycbcr[0] + 1.7720 * ycbcr[1])); // + 0.5);

        return rgb;
    }
}
