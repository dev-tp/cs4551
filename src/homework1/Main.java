package homework1;

import java.util.Scanner;

import utils.Image;

public class Main {

    private static void printOptions() {
        System.out.println("\n1. Conversion to gray-scale image (24 to 8 bits)");
        System.out.println("2. Conversion to n-level image");
        System.out.println("3. Conversion to 8 bit Indexed Color Image using Uniform Color Quantization" +
                " (24 to 8 bits)");
        System.out.println("4. Conversion to 8 bit Indexed Color Image using ________");
        System.out.println("5. Quit\n");
        System.out.print("Please enter a task number [1-5]: ");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Main <PPM_file>");
            return;
        }

        Image image;

        try {
            image = new Image(args[0]);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            return;
        }

        boolean quit = false;
        Scanner scanner = new Scanner(System.in);
        String fileName = args[0].split("\\.")[0];

        System.out.println("\nMain Menu:");

        while (!quit) {
            try {
                printOptions();
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("\nCreating gray-scale image...");
                        Image grayScaleImage = image.grayScale();
                        grayScaleImage.display();
                        grayScaleImage.writeToPPM(fileName + "-gray.ppm");
                        break;
                    case 2:
                        System.out.println("\nYou chose option 2.");
                        break;
                    case 3:
                        System.out.println("\nCreating image using Uniform Color Quantization...");
                        Image quantizedImage = UniformColorQuantization.quantizeImage(image);
                        quantizedImage.display();
                        quantizedImage.writeToPPM(fileName + "-index.ppm");
                        break;
                    case 4:
                        System.out.println("\nYou chose option 4.");
                        break;
                    default:
                        quit = true;
                }
            } catch (Exception exception) {
                quit = true;
            }
        }

        scanner.close();
    }
}
