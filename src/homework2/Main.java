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
