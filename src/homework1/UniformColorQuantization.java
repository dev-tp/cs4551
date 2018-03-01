package homework1;

import utils.Image;

class UniformColorQuantization {

    private static int[][] uniformColors;

    private static int findIndex(int red, int green, int blue) {
        return 32 * (red / 32) + 4 * (green / 32) + (blue / 64);
    }

    private static int[] getUniformColor(int colorIndex) {
        if (uniformColors == null) {
            uniformColors = new int[256][3];

            for (int r = 0; r < 8; r++) {
                for (int g = 0; g < 8; g++) {
                    for (int b = 0; b < 4; b++) {
                        int i = 32 * r + 4 * g + b;
                        uniformColors[i][0] = 16 + r * 32;
                        uniformColors[i][1] = 16 + g * 32;
                        uniformColors[i][2] = 32 + b * 64;
                    }
                }
            }
        }

        return uniformColors[colorIndex];
    }

    static Image quantizeImage(Image image) {
        int height = image.getHeight();
        int width = image.getWidth();

        int[] rgb = new int[3];

        Image newImage = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.getPixel(x, y, rgb);
                newImage.setPixel(x, y, getUniformColor(findIndex(rgb[0], rgb[1], rgb[2])));
            }
        }

        return newImage;
    }
}
