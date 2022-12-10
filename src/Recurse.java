import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

public class Recurse extends RecursiveAction {

    /** The mesh of regions. */
    public Region[][] mesh;

    /** The minimum value in the vertical direction. */
    public int verticalLow;

    /** The maximum value in the vertical direction. */
    public int verticalHigh;

    /** The minimum value in the horizontal direction. */
    public int horizontalLow;

    /** The maximum value in the horizontal direction. */
    public int horizontalHigh;

    /** The previous temperatures of the regions. */
    public float[][] previousTemperatures;

    /** The thermal constants of the metals. */
    public float[] metalConstants;

    /**
     * Creates a new Recurse object.
     *
     * @param mesh The mesh of regions
     * @param verticalLow The lowest y-value of the current region in the mesh
     * @param verticalHigh The highest y-value of the current region in the mesh
     * @param horizontalLow The lowest x-value of the current region in the mesh
     * @param horizontalHigh The highest x-value of the current region in the mesh
     * @param previousTemperatures The previous temperatures of the regions
     * @param metalConstants The thermal constants of the metals
     */
    public Recurse(Region[][] mesh, int verticalLow, int verticalHigh, int horizontalLow, int horizontalHigh, float[][] previousTemperatures, float[] metalConstants) {
        this.mesh = mesh;
        this.verticalLow = verticalLow;
        this.verticalHigh = verticalHigh;
        this.horizontalLow = horizontalLow;
        this.horizontalHigh = horizontalHigh;
        this.previousTemperatures = previousTemperatures;
        this.metalConstants = metalConstants;
        if (Main.i == 0) computeNeighbors();
    }

    /**
     * Computes the neighbors of each region.
     */
    public void computeNeighbors() {
        for (int i = verticalLow; i < verticalHigh; i++) {
            for (int j = horizontalLow; j < horizontalHigh; j++) {
                if (i > 0) {
                    ArrayList<Region> list = mesh[i][j].neighbors;
                    list.add(mesh[i - 1][j]);
                } if (i < mesh.length - 1) {
                    mesh[i][j].neighbors.add(mesh[i + 1][j]);
                } if (j > 0) {
                    mesh[i][j].neighbors.add(mesh[i][j - 1]);
                } if (j < mesh[0].length - 1) {
                    mesh[i][j].neighbors.add(mesh[i][j + 1]);
                }
            }
        }
    }

    /**
     * Computes the temperature of the regions by recursively dividing the mesh into
     * smaller and smaller pieces until each piece is a 2x2 mesh.
     */
    @Override
    protected void compute() {
        boolean tooTall = (verticalHigh - verticalLow) > 2;
        boolean tooWide = (horizontalHigh - horizontalLow) > 2;

        if (tooTall) { /* split the mesh vertically */
            int mid = (verticalHigh + verticalLow) / 2;
            Recurse left = new Recurse(mesh, verticalLow, mid, horizontalLow, horizontalHigh, previousTemperatures, metalConstants);
            Recurse right = new Recurse(mesh, mid, verticalHigh, horizontalLow, horizontalHigh, previousTemperatures, metalConstants);
            invokeAll(left, right);
        } else if (tooWide) { /* split the mesh horizontally */
            int mid = (horizontalLow + horizontalHigh) / 2;
            Recurse left = new Recurse(mesh, verticalLow, verticalHigh, horizontalLow, mid, previousTemperatures, metalConstants);
            Recurse right = new Recurse(mesh, verticalLow, verticalHigh, mid, horizontalHigh, previousTemperatures, metalConstants);
            invokeAll(left, right);
        } else { /* compute the temperature of the region */
            for (int i = horizontalLow; i < horizontalHigh; i++) {
                for (int j = verticalLow; j < verticalHigh; j++) {
                    mesh[j][i].computeTemperature(previousTemperatures, metalConstants);
                }
            }
        }
    }
}
