import java.awt.*;
public class Spacetarget {
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
    Color color;
    

    public Spacetarget(int x, int y, int width, int height, int moveX, int moveY, int health, Color color){
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
        this.timer = 0;
        this.moveX = moveX;
        this.moveY = moveY;
        this.health = health;
        this.color = color;
    }
}
