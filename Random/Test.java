package Random;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.random.*;

import javax.swing.*;

public class Test extends JPanel {
    int laserXStart = 100, laserYStart = 400, laserXStop = 200, laserYStop = 0;
    int side = 30, objectX = 130, objectY = 30;
    ImageIcon myPic;

    public Test() {
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setSize(1800, 1800);  // Increase the width of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        constructImage("SpaceStar.png");
    }

    public void paintComponent(Graphics g) {
        Random r = new Random();

        g.fillRect(0, 0, 2000, 2000);
        for(int i = 0; i < 1000; i ++)
        g.drawImage(myPic.getImage(),r.nextInt(1800) , r.nextInt(1800), this);

        
    }
    
    public void constructImage(String fileName) {
        File imageFile = new File("./images/" + fileName);
        myPic = new ImageIcon (imageFile.toString());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Test());
    }
}
