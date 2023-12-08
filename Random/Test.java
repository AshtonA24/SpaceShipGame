package Random;
import java.awt.*;
import java.io.File;

import javax.swing.*;

public class Test extends JPanel {
    int laserXStart = 100, laserYStart = 400, laserXStop = 200, laserYStop = 0;
    int side = 30, objectX = 130, objectY = 30;
    ImageIcon myPic;

    public Test() {
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setSize(200, 200);  // Increase the width of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        constructImage("star.png");
    }

    public void paintComponent(Graphics g) {
        g.fillRect(0, 0, 2000, 2000);
        g.drawImage(myPic.getImage(), 0, 0, this);
        
    }
    
    private void constructImage(String fileName) {
        File imageFile = new File("./images/" + fileName);
        myPic = new ImageIcon (imageFile.toString());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Test());
    }
}
