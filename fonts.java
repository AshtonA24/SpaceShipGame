import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class fonts extends JFrame {
    private static final int NUM_FONTS_TO_DISPLAY = 100;

    public fonts() {
        setTitle("Font Display Example");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new FontPanel();
        add(panel);
    }

    private class FontPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Get the available font names
            String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

            // Display the first 10 fonts
            for (int i = 0; i < NUM_FONTS_TO_DISPLAY && i < fontNames.length; i++) {
                Font font = new Font(fontNames[i], Font.PLAIN, 20);
                g.setFont(font);
                g.drawString("Sample Text " + (i + 1), 20, 30 * (i + 1));
                System.out.println(fontNames[i]);
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            fonts example = new fonts();
            example.setVisible(true);
        });
    }
}
