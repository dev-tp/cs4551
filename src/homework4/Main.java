package homework4;

import java.util.Scanner;

import utils.Image;

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
                        scanner.nextLine(); // Clear buffer

                        System.out.print("Target image path: ");
                        String targetPath = scanner.nextLine();

                        System.out.print("Reference image path: ");
                        String referencePath = scanner.nextLine();

                        System.out.print("Enter block size [8, 16, 24]: ");
                        int n = scanner.nextInt();

                        System.out.print("Enter search window value [4, 8, 12, 16]: ");
                        int p = scanner.nextInt();

                        BlockBasedMotionCompensation.routine(new Image(targetPath), new Image(referencePath), n, p);

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
