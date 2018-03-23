package activity1;

public class Main {

    public static void main(String[] args) {
        double[][] matrix = {
                {139, 144, 149, 153, 155, 155, 155, 155},
                {144, 151, 153, 156, 159, 156, 156, 156},
                {150, 155, 160, 163, 158, 156, 156, 156},
                {159, 161, 162, 160, 160, 159, 159, 159},
                {159, 160, 161, 162, 162, 155, 155, 155},
                {161, 161, 161, 161, 160, 157, 157, 157},
                {162, 162, 161, 163, 162, 157, 157, 157},
                {162, 162, 161, 161, 163, 158, 158, 158},
        };

        System.out.println("Original:");
        DiscreteCosineTransformation.printMatrix(matrix);

        double[][] dctMatrix = DiscreteCosineTransformation.apply(matrix);

        System.out.println("\nDCT:");
        DiscreteCosineTransformation.printMatrix(dctMatrix);

        System.out.println("\nInverse DCT:");
        DiscreteCosineTransformation.printMatrix(DiscreteCosineTransformation.invert(dctMatrix));
    }
}
