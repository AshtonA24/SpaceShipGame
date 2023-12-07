import java.awt.*;
import javax.swing.*;

public class Test extends JPanel {
    int laserXStart = 100, laserYStart = 400, laserXStop = 200, laserYStop = 0;
    int side = 30, objectX = 130, objectY = 30;

    public Test() {
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setSize(200, 428);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 200, 428);
        g.setColor(Color.white);
        g.fillRect(objectX, objectY, side, side);
        g.drawLine(laserXStart, laserYStart, laserXStop, laserYStop);
        if (intersects()) {
            g.setColor(Color.red);
            g.drawString("Intersection", 10, 20);
        }
    }

    public boolean intersects() {
        int x1 = laserXStart, y1 = laserYStart, x2 = laserXStop, y2 = laserYStop;
        int x3 = objectX, y3 = objectY, x4 = objectX + side, y4 = objectY + side;

        // Check if the line intersects with the rectangle
        return x1 < x4 && x2 > x3 && y1 > y4 && y2 < y3;
    }

    public static void main(String[] args) {
        new Test();
    }
}
