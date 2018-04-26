package homework4;

import java.util.Scanner;

public class Main {

    private static void printOptions() {
        System.out.println("\n1. Block-based motion compensation");
        System.out.println("2. Remove moving objects");
        System.out.println("3. Quit");
        System.out.print("\nEnter an option [1-3]: ");
    }

    public static void main(String[] args) {
        boolean quit = false;
        int exitStatus = 0;

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nMain Menu:");

        while (!quit) {
            try {
                printOptions();
                switch (scanner.nextInt()) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        quit = true;
                        break;
                    default:
                        System.out.println("The option you entered is not a valid option.");
                }
            } catch (Exception exception) {
                exitStatus = 1;
                quit = true;
            }
        }

        scanner.close();

        System.exit(exitStatus);
    }
}
