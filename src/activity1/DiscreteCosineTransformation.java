package activity1;

class DiscreteCosineTransformation {

    static double[][] apply(double[][] matrix) {
        double[][] dctMatrix = new double[8][8];

        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                double sum = 0.0;

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        double a = Math.cos((2 * i + 1) * u * Math.PI / 16);
                        double b = Math.cos((2 * j + 1) * v * Math.PI / 16);

                        sum += a * b * (matrix[i][j] - 128);
                    }

                    double c = ((u == 0 ? Math.sqrt(2.0) / 2 : 1) * (v == 0 ? Math.sqrt(2.0) / 2 : 1)) / 4.0;

                    dctMatrix[u][v] = c * sum;
                }
            }
        }

        return dctMatrix;
    }

    static double[][] invert(double[][] dctMatrix) {
        double[][] matrix = new double[8][8];

        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                double sum = 0.0;

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        double a = ((i == 0 ? Math.sqrt(2.0) / 2 : 1) * (j == 0 ? Math.sqrt(2.0) / 2 : 1)) / 4.0;
                        double b = Math.cos((2 * u + 1) * i * Math.PI / 16);
                        double c = Math.cos((2 * v + 1) * j * Math.PI / 16);

                        sum += a * b * c * dctMatrix[i][j];
                    }

                    matrix[u][v] = sum + 128;
                }
            }
        }

        return matrix;
    }

    static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double column : row) {
                System.out.printf("%.1f\t", column);
            }

            System.out.println();
        }
    }
}
