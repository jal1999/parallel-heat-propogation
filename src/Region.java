import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Region {

    /** The temperature of the region */
    public float temperature;

    /** The indexes of the region in the mesh */
    public int row, col;

    /** Flag to determine if the region is either the top left or bottom right corner of the mesh */
    boolean isHeatedCorner;

    /** Collection of the region's neighbors */
    ArrayList<Region> neighbors = new ArrayList<>();

    /** Percent values of each metal in the region */
    public float[] metalPercentages;

    /**
     * Creates a region with a specified temperature and (semi) random metal percentages.
     *
     * @param row The row index of the region in the mesh
     * @param col The column index of the region in the mesh
     * @param isHeatedCorner Flag to determine if the region is either the top left or bottom right corner of the mesh
     * @param temperature The temperature of the region
     */
    public Region(int row, int col, boolean isHeatedCorner, float temperature) {
        this.row = row;
        this.col = col;
        metalPercentages = new float[3];
        metalPercentages[0] = 1 / 3f;
        metalPercentages[1] = (float) ThreadLocalRandom.current().nextDouble(2/3f);
        metalPercentages[2] = Math.abs(metalPercentages[0] - metalPercentages[1]);
        this.temperature = temperature;
        this.isHeatedCorner = isHeatedCorner;
    }

    /**
     * Computes the temperature of the region.
     *
     * @param previousTemperatures the temperatures of the mesh in the previous iteration
     * @param metalConstants the thermal constants of the metals
     */
    public void computeTemperature(float[][] previousTemperatures, float[] metalConstants) {
        if (isHeatedCorner) return; /* we're not computing temps for the two heated corners */
        float newTemperature = 0;
        for (int i = 0; i < 3; i++) {
            float tempMetalSummation = 0;
            for (Region neighbor : neighbors) {
                tempMetalSummation += previousTemperatures[neighbor.row][neighbor.col] * neighbor.metalPercentages[i];
            }
            newTemperature += metalConstants[i] * tempMetalSummation;
        }
        previousTemperatures[row][col] = temperature;
        temperature = newTemperature / neighbors.size();
    }
}