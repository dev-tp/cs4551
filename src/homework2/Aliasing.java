package homework2;

import utils.Image;

class Aliasing {

    private Image image;
    private Image subSample;

    Aliasing(Image image) {
        this.image = image;
    }

    // Bresenham's Circle Algorithm
    // https://stackoverflow.com/questions/27755514/circle-with-thickness-drawing-algorithm
    private void bresenhamCircle(int ir, int or) {
        int cx = image.getWidth() / 2;

        if (or >= cx) {
            return;
        }

        int cy = image.getHeight() / 2;

        int xi = ir;
        int xo = or;

        int y = 0;

        int eo = 1 - xo;
        int er = 1 - xi;

        int[] rgb = {0, 0, 0};

        while (xo >= y) {
            horizontalLine(cx + xi, cx + xo, cy + y, rgb);
            verticalLine(cx + y, cy + xi, cy + xo, rgb);
            horizontalLine(cx - xo, cx - xi, cy + y, rgb);
            verticalLine(cx - y, cy + xi, cy + xo, rgb);
            horizontalLine(cx - xo, cx - xi, cy - y, rgb);
            verticalLine(cx - y, cy - xo, cy - xi, rgb);
            horizontalLine(cx + xi, cx + xo, cy - y, rgb);
            verticalLine(cx + y++, cy - xo, cy - xi, rgb);

            if (eo < 0) {
                eo += 2 * y + 1;
            } else {
                eo += 2 * (y - --xo + 1);
            }

            if (y > ir) {
                xi = y;
            } else {
                if (er < 0) {
                    er += 2 * y + 1;
                } else {
                    er += 2 * (y - --xi + 1);
                }
            }
        }
    }

    void display() {
        image.display();
        subSample.display();
    }

    void drawCircle(int thickness, int buffer) {
        for (int i = buffer; i <= 256; i += (thickness + buffer)) {
            bresenhamCircle(i, i + thickness);
        }
    }

    private void pixel(int[] rgb, int x, int y, int[] p, double f, int w, int h) {
        if ((w <= x && x < w) && (h <= y && y < h)) {
            subSample.getPixel(x, y, rgb);
            p[1] = (int) (rgb[1] * f);
            p[2] = (int) (rgb[2] * f);
            p[3] = (int) (rgb[3] * f);
        }
    }

    void filter1() {
        int[] rgb = {0, 0, 0};

        int[] c = {0, 0, 0};
        int[] p1 = {0, 0, 0};
        int[] p2 = {0, 0, 0};
        int[] p3 = {0, 0, 0};
        int[] p4 = {0, 0, 0};
        int[] p5 = {0, 0, 0};
        int[] p6 = {0, 0, 0};
        int[] p7 = {0, 0, 0};
        int[] p8 = {0, 0, 0};

        double f = 1.0 / 9.0;

        int width = subSample.getWidth();
        int height = subSample.getHeight();

        for (int y = 0; y < subSample.getHeight(); y++) {
            for (int x = 0; x < subSample.getWidth(); x++) {
                pixel(rgb, x - 1, y - 1, p1, f, width, height);
                pixel(rgb, x, y - 1, p2, f, width, height);
                pixel(rgb, x + 1, y - 1, p3, f, width, height);
                pixel(rgb, x - 1, y, p4, f, width, height);
                pixel(rgb, x, y, c, f, width, height);
                pixel(rgb, x + 1, y, p5, f, width, height);
                pixel(rgb, x - 1, y + 1, p6, f, width, height);
                pixel(rgb, x, y + 1, p7, f, width, height);
                pixel(rgb, x + 1, y + 1, p8, f, width, height);

                rgb[0] = c[0] + p1[0] + p2[0] + p3[0] + p4[0] + p5[0] + p6[0] + p7[0] + p8[0];
                rgb[1] = c[1] + p1[1] + p2[1] + p3[1] + p4[1] + p5[1] + p6[1] + p7[1] + p8[1];
                rgb[2] = c[2] + p1[2] + p2[2] + p3[2] + p4[2] + p5[2] + p6[2] + p7[2] + p8[2];

                subSample.setPixel(x, y, rgb);
            }
        }
    }

    void filter2() {
        int[] rgb = {0, 0, 0};

        int[] c = {0, 0, 0};
        int[] p1 = {0, 0, 0};
        int[] p2 = {0, 0, 0};
        int[] p3 = {0, 0, 0};
        int[] p4 = {0, 0, 0};
        int[] p5 = {0, 0, 0};
        int[] p6 = {0, 0, 0};
        int[] p7 = {0, 0, 0};
        int[] p8 = {0, 0, 0};

        int width = subSample.getWidth();
        int height = subSample.getHeight();

        for (int y = 0; y < subSample.getHeight(); y++) {
            for (int x = 0; x < subSample.getWidth(); x++) {
                pixel(rgb, x - 1, y - 1, p1, 1.0 / 16.0, width, height);
                pixel(rgb, x, y - 1, p2, 2.0 / 16.0, width, height);
                pixel(rgb, x + 1, y - 1, p3, 1.0 / 16.0, width, height);
                pixel(rgb, x - 1, y, p4, 2.0 / 16.0, width, height);
                pixel(rgb, x, y, c, 4.0 / 16.0, width, height);
                pixel(rgb, x + 1, y, p5, 2.0 / 16.0, width, height);
                pixel(rgb, x - 1, y + 1, p6, 1.0 / 16.0, width, height);
                pixel(rgb, x, y + 1, p7, 2.0 / 16.0, width, height);
                pixel(rgb, x + 1, y + 1, p8, 1.0 / 16.0, width, height);

                rgb[0] = c[0] + p1[0] + p2[0] + p3[0] + p4[0] + p5[0] + p6[0] + p7[0] + p8[0];
                rgb[1] = c[1] + p1[1] + p2[1] + p3[1] + p4[1] + p5[1] + p6[1] + p7[1] + p8[1];
                rgb[2] = c[2] + p1[2] + p2[2] + p3[2] + p4[2] + p5[2] + p6[2] + p7[2] + p8[2];

                subSample.setPixel(x, y, rgb);
            }
        }
    }

    private void horizontalLine(int x0, int x1, int y, int[] rgb) {
        while (x0 <= x1) {
            image.setPixel(x0++, y, rgb);
        }
    }

    void subSample(int k) {
        int[] rgb = {255, 255, 255};

        subSample = new Image(image.getWidth() / k, image.getHeight() / k, rgb);

        for (int y = 0; y < image.getHeight(); y += k) {
            for (int x = 0; x < image.getWidth(); x += k) {
                image.getPixel(x, y, rgb);
                subSample.setPixel(x / k, y / k, rgb);
            }
        }
    }

    private void verticalLine(int x, int y0, int y1, int[] rgb) {
        while (y0 <= y1) {
            image.setPixel(x, y0++, rgb);
        }
    }
}
