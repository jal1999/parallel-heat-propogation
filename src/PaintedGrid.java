import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.GridLayout;

public class PaintedGrid {

    JFrame frame = new JFrame();
    JButton[][] grid;

    public PaintedGrid(Region[][] mesh) {
        float maxValue = maxValue(mesh);
        frame.setLayout(new GridLayout(mesh[0].length, mesh.length));
        grid = new JButton[mesh.length][mesh[0].length];
        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[0].length; x++) {
                float temp = mesh[y][x].temperature;
                int red = (int) (255 * (temp / maxValue));
                int blue = 255 - red;
                grid[y][x] = new JButton(String.valueOf(temp));
                grid[y][x].setBackground(new Color(red, 0, blue));
                grid[y][x].setOpaque(true);
                frame.add(grid[y][x]);
            }

        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public float maxValue(Region[][] mesh) {
        float max = 0;
        for (Region[] regions : mesh) {
            for (int x = 0; x < mesh[0].length; x++) {
                if (regions[x].temperature > max) {
                    max = regions[x].temperature;
                }
            }
        }
        return max;
    }
}
