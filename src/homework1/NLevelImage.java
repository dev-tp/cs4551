package homework1;

import utils.Image;

class NLevelImage {

    private static final int[] LEVEL_4_THRESHOLD_VALUES = {0, 85, 170, 255};
    private static final int[] LEVEL_8_THRESHOLD_VALUES = {0, 32, 64, 96, 128, 160, 192, 224};
    private static final int[] LEVEL_16_THRESHOLD_VALUES = {
            0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240
    };

    private double averageGrayValue;
    private Image image;

    NLevelImage(Image image) {
        Object[] tuple = grayScale(image, new Image(image.getWidth(), image.getHeight()));
        this.image = (Image) tuple[0];
        averageGrayValue = (Double) tuple[1];
    }

    // FIXME
    Image errorDiffuse() {
        int[] rgb = new int[3];

        int height = image.getHeight();
        int width = image.getWidth();

        Image errorDiffusedImage = new Image(width, height);

        for (int y = 0; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                image.getPixel(x, y, rgb);

                int gray = rgb[0];

                rgb[0] = rgb[1] = rgb[2] = rgb[0] < 127 ? 0 : 255;

                int error = gray - rgb[0];

                image.getPixel(x + 1, y, rgb);
                rgb[0] = rgb[1] = rgb[2] = rgb[0] + (int) (7.0 / 16.0 * error);
                errorDiffusedImage.setPixel(x + 1, y, rgb);

                image.getPixel(x - 1, y + 1, rgb);
                rgb[0] = rgb[1] = rgb[2] = rgb[0] + (int) (3.0 / 16.0 * error);
                errorDiffusedImage.setPixel(x - 1, y + 1, rgb);

                image.getPixel(x, y + 1, rgb);
                rgb[0] = rgb[1] = rgb[2] = rgb[0] + (int) (5.0 / 16.0 * error);
                errorDiffusedImage.setPixel(x, y + 1, rgb);

                image.getPixel(x + 1, y + 1, rgb);
                rgb[0] = rgb[1] = rgb[2] = rgb[0] + (int) (1.0 / 16.0 * error);
                errorDiffusedImage.setPixel(x + 1, y + 1, rgb);
            }
        }

        return errorDiffusedImage;
    }

    static Image grayScale(Image image) {
        return (Image) grayScale(image, new Image(image.getWidth(), image.getHeight()))[0];
    }

    private static Object[] grayScale(Image image, Image grayScaleImage) {
        int[] rgb = new int[3];
        double averageGrayValue = 0.0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.getPixel(x, y, rgb);

                int gray = (int) (0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);

                if (gray < 0) {
                    gray = 0;
                } else if (gray > 255) {
                    gray = 255;
                }

                averageGrayValue += gray;

                rgb[0] = rgb[1] = rgb[2] = gray;
                grayScaleImage.setPixel(x, y, rgb);
            }
        }

        averageGrayValue = averageGrayValue / (image.getWidth() * image.getHeight());

        return new Object[]{grayScaleImage, averageGrayValue};
    }

    Image thresholdImage(int level) {
        int[] rgb = new int[3];

        int height = image.getHeight();
        int width = image.getWidth();

        Image thresholdImage = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.getPixel(x, y, rgb);

                switch (level) {
                    case 2:
                        rgb[0] = rgb[1] = rgb[2] = rgb[0] < averageGrayValue ? 0 : 255;
                        break;
                    case 4:
                        thresholdValue(LEVEL_4_THRESHOLD_VALUES, rgb);
                        break;
                    case 8:
                        thresholdValue(LEVEL_8_THRESHOLD_VALUES, rgb);
                        break;
                    default:
                        thresholdValue(LEVEL_16_THRESHOLD_VALUES, rgb);
                }

                thresholdImage.setPixel(x, y, rgb);
            }
        }

        return thresholdImage;
    }

    private static void thresholdValue(int[] thresholdValues, int[] rgb) {
        for (int i = 0; i < thresholdValues.length - 1; i++) {
            if (thresholdValues[i] <= rgb[0] && rgb[0] < thresholdValues[i + 1]) {
                rgb[0] = rgb[1] = rgb[2] = thresholdValues[i];
                return;
            }
        }

        rgb[0] = rgb[1] = rgb[2] = thresholdValues[thresholdValues.length - 1];
    }
}
