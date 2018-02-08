package homework1;

import java.util.Scanner;

public class Main {

    private static void printOptions() {
        System.out.println("1. Conversion to gray-scale image (24 to 8 bits)");
        System.out.println("2. Conversion to n-level image");
        System.out.println("3. Conversion to 8 bit Indexed Color Image using Uniform Color Quantization" +
                "(24 to 8 bits)");
        System.out.println("4. Conversion to 8 bit Indexed Color Image using ________");
        System.out.println("5. Quit\n");
        System.out.print("Please enter a task number [1-5]: ");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Main <PPM_file>");
            return;
        }

        boolean quit = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nMain Menu:\n");

        while (!quit) {
            try {
                printOptions();
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("\nYou chose option 1.\n");
                        break;
                    case 2:
                        System.out.println("\nYou chose option 2.\n");
                        break;
                    case 3:
                        System.out.println("\nYou chose option 3.\n");
                        break;
                    case 4:
                        System.out.println("\nYou chose option 4.\n");
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
