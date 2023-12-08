package Space;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
public class SpaceBoss {
    int posX;
    int posY;
    int width;
    int height;
    int timer;
    int moveX;
    int moveY;
    int previousX;
    int previousY;
    int health;
    boolean laserHit;
    Color color;
    int imageTimer;
    ArrayList <ImageIcon> images = new ArrayList <>();
    ArrayList <ImageIcon> imagesHit = new ArrayList <>();
    

    public SpaceBoss(int x, int y, int width, int height, int moveX, int moveY, int timer, int health, Color color){
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
        this.timer = 0;
        this.moveX = moveX;
        this.moveY = moveY;
        this.timer = timer;
        this.laserHit = false;
        this.health = health;
        this.color = color;
        constructImage("SpaceBoss1.png");
        constructImage("SpaceBoss2.png");
        constructImage("SpaceBoss3.png");
        constructImage("SpaceBoss4.png");
        constructImageHit("SpaceBossHit1.png");
        constructImageHit("SpaceBossHit2.png");
        constructImageHit("SpaceBossHit3.png");
        constructImageHit("SpaceBossHit4.png");
        this.imageTimer = 0;

    }

    public int getX(){
        return posX;
    }

    public int getY(){
        return posY;
    }

    public void setX(int x){
        posX = x;
    }

    public void setY(int x){
        posY = x;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getTimer(){
        return timer;
    }

    public void addTimer(int x){
        timer += x;
    }

    public void clearTimer(){
        timer = 0;
    }

    public int getMoveX(){
        return moveX;
    }

    public int getMoveY(){
        return moveY;
    }

    public void setMoveX(int x){
        moveX = x;
    }

    public void setMoveY(int x){
        moveY = x;
    }

    public void constructImage(String fileName) {
        File imageFile = new File("./images/" + fileName);
        images.add(new ImageIcon (imageFile.toString()));
    }

    public void constructImageHit(String fileName) {
        File imageFile = new File("./images/" + fileName);
        imagesHit.add(new ImageIcon (imageFile.toString()));
    }

    
}
