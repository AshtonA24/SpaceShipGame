package snake;
public class SnakeApple {
    private int appleX;
    private int appleY;
    private int appleLevel;
    

    public SnakeApple(int x, int y, int level){
        appleX = x;
        appleY = y;
        appleLevel = level;
    }

    public int getAppleX(){
        return appleX;
    }

    public int getAppleY(){
        return appleY;
    }

    public int getAppleLevel(){
        return appleLevel;
    }
}