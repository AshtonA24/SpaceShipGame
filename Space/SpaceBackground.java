package Space;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.random.*;

import javax.swing.*;

public class SpaceBackground extends JPanel {

    ImageIcon myPic;

    public SpaceBackground() {
        constructImage("SpaceBackground.png");
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

}
