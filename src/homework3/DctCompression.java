package homework3;

import utils.Image;

class DctCompression {

    private Image image;

    DctCompression(String imagePath) throws Exception {
        image = new Image(imagePath);
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
                resizedImage.setPixel(x, y, rgb);
            }
        }

        resizedImage.display();
    }
}
