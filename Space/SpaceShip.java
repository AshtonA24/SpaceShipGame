package Space;
import java.awt.*;

public class SpaceShip {
    int posX;
    int posY;
    int width;
    int height;
    int moveX;
    int moveY;
    Color color;
    

    public SpaceShip(int x, int y, int width, int height, Color color){
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }
}


