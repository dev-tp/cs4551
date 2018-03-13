package homework2;

import java.util.Scanner;

import utils.Image;

public class Main {

    private static void printOptions() {
        System.out.println("\n1. Aliasing");
        System.out.println("2. Dictionary Coding");
        System.out.println("3. Quit\n");
        System.out.print("Enter an option [1-3]: ");
    }

    public static void main(String[] args) {
        boolean quit = false;
        int error = 0;
        Scanner scanner = new Scanner(System.in);

        while (!quit) {
            try {
                printOptions();
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("\nDraw a target");

                        System.out.print("Enter the circles' thickness: ");
                        int m = scanner.nextInt();

                        System.out.print("Enter the padding between circles: ");
                        int n = scanner.nextInt();

                        Aliasing aliasing = new Aliasing(new Image(512, 512, new int[]{255, 255, 255}));
                        aliasing.drawCircle(m, n);

                        System.out.println("\nSub-sample");
                        System.out.print("Enter a value [2, 4, 8, 16]: ");
                        aliasing.subSample(scanner.nextInt());

                        break;
                    case 2:
                        System.out.print("Type the path of file to encode: ");
                        scanner.nextLine(); // Clear last line

                        LZW lzw = new LZW(scanner.nextLine());
                        lzw.encode();
                        lzw.decode();

                        System.out.printf("Compression ratio: %.3f\n", lzw.getCompressionRatio());

                        lzw.outputResultToFile();

                        break;
                    default:
                        quit = true;
                }
            } catch (Exception exception) {
                error = 1;
                quit = true;
            }
        }

        scanner.close();

        System.exit(error);
    }
}
