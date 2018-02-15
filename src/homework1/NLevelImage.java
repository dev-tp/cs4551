package homework1;

import utils.Image;

class NLevelImage {

    private double averageGrayValue;
    private Image image;

    NLevelImage(Image image) {
        Object[] tuple = grayScale(image, new Image(image.getWidth(), image.getHeight()));
        this.image = (Image) tuple[0];
        averageGrayValue = (Double) tuple[1];
    }

    Image applyLevel() {
        int[] rgb = new int[3];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.getPixel(x, y, rgb);
                rgb[0] = rgb[1] = rgb[2] = rgb[0] < averageGrayValue ? 0 : 255;
                image.setPixel(x, y, rgb);
            }
        }

        return image;
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
}
