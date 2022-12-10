import java.util.Scanner;

public class Main {
    public static float[] metalConstants;
    public static float[][] previousTemperatures;
    public static double convergenceThreshold;
    public static int MAX_ITERATIONS = 4000;
    public static boolean hasConverged = false;
    public static int i;

    public static double getMaxDifference(float[][] previousTemperatures, Region[][] currentTemperatures) {
        double maxDifference = 0;
        for (int i = 0; i < previousTemperatures.length; i++) {
            for (int j = 0; j < previousTemperatures[0].length; j++) {
                double difference = Math.abs(previousTemperatures[i][j] - currentTemperatures[i][j].temperature);
                if (difference > maxDifference) {
                    maxDifference = difference;
                }
            }
        }
        return maxDifference;
    }

    public static void main(String[] args) {

        /* Get all necessary inputs from the user */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Temperature of the top left corner: ");
        float s = scanner.nextFloat();
        System.out.println("Temperature of the bottom right corner: ");
        float t = scanner.nextFloat();
        System.out.println("Thermal constant for metal #1: ");
        float c1 = scanner.nextFloat();
        System.out.println("Thermal constant for metal #2: ");
        float c2 = scanner.nextFloat();
        System.out.println("Thermal constant for metal #3: ");
        float c3 = scanner.nextFloat();
        System.out.println("Number of rows in the mesh: ");
        int rows = scanner.nextInt();
        System.out.println("Number of columns in the mesh: ");
        int cols = scanner.nextInt();
        System.out.println("Convergence threshold: ");
        convergenceThreshold = scanner.nextDouble();
        previousTemperatures = new float[rows][cols];
        metalConstants = new float[]{c1, c2, c3};

        /* Build the mesh */
        Region[][] mesh = new Region[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 && j == 0 || i == rows - 1 && j == cols - 1) {
                    mesh[i][j] = new Region(i, j, true, i == 0 && j == 0 ? s : t);
                    previousTemperatures[i][j] = 60;
                } else {
                    mesh[i][j] = new Region(i, j, false, 0);
                    previousTemperatures[i][j] = 0;
                }
            }
        }

        /* Compute the temperature of each region until convergence */
        Recurse u = new Recurse(mesh, 0, rows, 0, cols, previousTemperatures, metalConstants);
        while (i < MAX_ITERATIONS && !hasConverged) {
            u.compute();
            if (getMaxDifference(previousTemperatures, mesh) < convergenceThreshold) {
                hasConverged = true;
            }
            i++;
        }
        new PaintedGrid(mesh);
    }
}