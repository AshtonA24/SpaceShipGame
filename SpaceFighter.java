public class SpaceFighter {
    int posX;
    int posY;
    int width;
    int height;
    int timer;
    int moveX;
    int moveY;
    int previousX;
    int previousY;
    

    public SpaceFighter(int x, int y, int width, int height, int moveX, int moveY, int timer){
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
        this.timer = 0;
        this.moveX = moveX;
        this.moveY = moveY;
        this.timer = timer;
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
}
