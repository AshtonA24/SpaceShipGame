package Space;
import java.awt.*;

public class SpacePellet {
    int posX;
    int posY;
    int width;
    int height;
    int timer;
    int moveX;
    int moveY;
    int previousX;
    int previousY;
    Color color;
    

    public SpacePellet(int x, int y, int width, int height, int moveX, int moveY, int timer, Color color){
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
        this.timer = 0;
        this.moveX = moveX;
        this.moveY = moveY;
        this.timer = timer;
        this.color = color;
    }
}
