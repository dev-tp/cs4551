package homework2;

import java.util.Scanner;

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
                        break;
                    case 2:
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
