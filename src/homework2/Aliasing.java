package homework2;

import utils.Image;

class Aliasing {

    private Image image;

    Aliasing(Image image) {
        this.image = image;
    }

    private void horizontalLine(int x0, int x1, int y, int[] rgb) {
        while (x0 <= x1) {
            image.setPixel(x0++, y, rgb);
        }
    }

    private void verticalLine(int x, int y0, int y1, int[] rgb) {
        while (y0 <= y1) {
            image.setPixel(x, y0++, rgb);
        }
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

    void drawCircle(int thickness, int buffer) {
        for (int i = buffer; i <= 256; i += (thickness + buffer)) {
            bresenhamCircle(i, i + thickness);
        }

        image.display();
    }
}
