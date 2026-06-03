import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class JuliaSet extends JPanel {

    public JuliaSet() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public static void main() {
        JFrame frame = new JFrame("Множество на Джулия");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new JuliaSet());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = renderJuliaSet();
        g.drawImage(image, ZERO_CONSTANT, ZERO_CONSTANT, null);
    }

    private BufferedImage renderJuliaSet() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int px = 0; px < WIDTH; px++) {
            for (int py = 0; py < HEIGHT; py++) {
                double zx = map(px, WIDTH, X_MIN, X_MAX);
                double zy = map(py, HEIGHT, Y_MIN, Y_MAX);

                int iterations = computeIterations(zx, zy);
                int color = getColor(iterations);
                image.setRGB(px, py, color);
            }
        }
        return image;
    }

    private int computeIterations(double zx, double zy) {
        int iterator = 0;
        while (zx * zx + zy * zy < ESCAPE_RADIUS_SQ && iterator < MAX_ITERATIONS) {
            double tmp = zx * zx - zy * zy + C_REAL;
            zy = COMPLEX_2AB_FACTOR * zx * zy + C_IMAG;
            zx = tmp;
            iterator++;
        }
        return iterator;
    }

    private int getColor(int iterations) {
        if (iterations == MAX_ITERATIONS) {
            return COLOR_INSIDE_SET;
        }

        double t = (double) iterations / MAX_ITERATIONS;

        int r = (int) (Math.sin(COLOR_CYCLE_FREQUENCY * t + PHASE_RED) * COLOR_AMPLITUDE + COLOR_MIDPOINT);
        int g = (int) (Math.sin(COLOR_CYCLE_FREQUENCY * t + PHASE_GREEN) * COLOR_AMPLITUDE + COLOR_MIDPOINT);
        int b = (int) (Math.sin(COLOR_CYCLE_FREQUENCY * t + PHASE_BLUE) * COLOR_AMPLITUDE + COLOR_MIDPOINT);

        return (r << RED_BIT_SHIFT) | (g << GREEN_BIT_SHIFT) | b;
    }

    private double map(double value, double fromHigh, double toLow, double toHigh) {
        return toLow + (value - (double) JuliaSet.ZERO_CONSTANT) * (toHigh - toLow) / (fromHigh -
            (double) JuliaSet.ZERO_CONSTANT);
    }


    // window dimensions
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // fractal params
    private static final int MAX_ITERATIONS = 300;
    private static final double C_REAL = -0.7;
    private static final double C_IMAG = 0.27015;

    // limits of complex plane
    private static final double X_MIN = -1.5;
    private static final double X_MAX = 1.5;
    private static final double Y_MIN = -1.125;
    private static final double Y_MAX = 1.125;

    // mathematical constants
    private static final double ESCAPE_RADIUS_SQ = 4.0;
    private static final double COMPLEX_2AB_FACTOR = 2.0;
    private static final int ZERO_CONSTANT = 0;

    // color constants
    private static final int COLOR_INSIDE_SET = 0x000000;
    private static final double COLOR_CYCLE_FREQUENCY = 18.0;

    // phase offsets for colors
    private static final double PHASE_RED = 0.0;
    private static final double PHASE_GREEN = 0.5;
    private static final double PHASE_BLUE = 1.0;

    // Amplitude and center for scaling in the range [0, 255]
    private static final double COLOR_MIDPOINT = 127.5;
    private static final double COLOR_AMPLITUDE = 127.5;

    // Bit shifts for the RGB channels
    private static final int RED_BIT_SHIFT = 16;
    private static final int GREEN_BIT_SHIFT = 8;

}