package homework3;

import utils.Image;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java Main <PPM File>");
            return;
        }

        DctCompression dct = new DctCompression();

        Image compressedImage = dct.compress(args[0]);
        compressedImage.display();

        Image restoredImage = dct.restore();
        restoredImage.display();
    }
}
