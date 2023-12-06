import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.event.*;

public class SpaceGame extends JPanel implements KeyListener {
    Random r = new Random();
    boolean running = true;
    boolean w;
    boolean s;
    boolean a;
    boolean d;
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    boolean upLaser;
    boolean downLaser;
    boolean leftLaser;
    boolean rightLaser;
    int laserJuice = 100;
    int maxLaserJuice = 100;
    int laserJuiceBufferCount = 0;
    int frameWidth = 1200;
    int frameHeight = 800;
    int starCount = 1000;
    boolean rechargeLaserJuice;
    ArrayList<SpaceFighter> fighters = new ArrayList<>();
    SpaceShip player = new SpaceShip(0, frameHeight/2, 10, 10, Color.GREEN);
    SpacePellet pellet = new SpacePellet(r.nextInt(400) + 50, r.nextInt(400) + 50, 20, 20, 0, 0, 0, Color.MAGENTA);
    Spacetarget target = new Spacetarget(r.nextInt(350) + 50, r.nextInt(350) + 50, 50, 50, 0, 0, 20, Color.ORANGE);
    Font font = new Font("ArAkayaKanadakaial", Font.BOLD, 16);
    int delay = 7;
    int fighterCount = 20;
    int score;
    boolean scoreDeposit;
    ArrayList<Integer> stars = new ArrayList<>();

    




    public SpaceGame() {
        JFrame frame = new JFrame("Space Fighters");
        frame.add(this);
        frame.setSize(frameWidth, frameHeight+25);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2 - 50;
        frame.setLocation(x, y);
        frame.setVisible(true);
        setFocusTraversalKeysEnabled(false);
        frame.addKeyListener(this);
        frame.setFocusable(true);

        // implement fighters
        for (int i = 0; i < fighterCount; i++) {
            fighters.add(new SpaceFighter(r.nextInt(frameWidth-150) + 100, r.nextInt(frameHeight - 100) + 100, 30, 30, 1, 1, r.nextInt(125)));
        }

        for(int i = 0; i < starCount; i ++){
            stars.add(r.nextInt(frameWidth));
            stars.add(r.nextInt(frameHeight));
        }

    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            w = true;
        } else if (key == KeyEvent.VK_A) {
            a = true;
        } else if (key == KeyEvent.VK_S) {
            s = true;
        } else if (key == KeyEvent.VK_D) {
            d = true;
        } else if (key == KeyEvent.VK_UP) {
            up = true;
        } else if (key == KeyEvent.VK_DOWN) {
            down = true;
        } else if (key == KeyEvent.VK_LEFT) {
            left = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            right = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            w = false;
        } else if (key == KeyEvent.VK_A) {
            a = false;
        } else if (key == KeyEvent.VK_S) {
            s = false;
        } else if (key == KeyEvent.VK_D) {
            d = false;
        } else if (key == KeyEvent.VK_UP) {
            up = false;
        } else if (key == KeyEvent.VK_DOWN) {
            down = false;
        } else if (key == KeyEvent.VK_LEFT) {
            left = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }

    public void paintComponent(Graphics g) {
        if (running) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, frameWidth, frameWidth);
            actions();
            draw(g);
            delay(delay);
            this.repaint();
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, frameWidth, frameHeight);
            draw(g);
            g.fillRect(maxLaserJuice, delay, WIDTH, HEIGHT);
        }
    }

    public void draw(Graphics g) {

        //stars
        g.setColor(Color.WHITE);
        for(int i = 0; i < starCount; i+= 2){
            g.fillOval(stars.get(i),stars.get(i+1),2,4);
            g.fillOval(stars.get(i)-1,stars.get(i+1)+1,2,4);
        }

        // lasers
        g.setColor(Color.RED);
        if (upLaser)
            g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, 0);
        if (downLaser)
            g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, frameHeight);
        if (leftLaser)
            g.drawLine(player.posX + 5, player.posY + 5, 0, player.posY + 5);
        if (rightLaser)
            g.drawLine(player.posX + 5, player.posY + 5, frameWidth, player.posY + 5);

        // pelets
        g.setColor(pellet.color);
        g.fillOval(pellet.posX, pellet.posY, pellet.width, pellet.height);

        //target
        g.setColor(target.color);
        g.fillRect(target.posX, target.posY, target.width, target.height);

        // player ship
        g.setColor(player.color);
        g.fillRect(player.posX, player.posY, player.width, player.height);

        // enemy fighters
        g.setColor(Color.BLUE);
        for (SpaceFighter f : fighters) {
            g.fillRect(f.getX(), f.getY(), f.getHeight(), f.getWidth());
        }

        // laser juice meter
        g.setColor(Color.WHITE);
        g.fillRect(15, frameHeight-25, 100, 10);
        g.setColor(Color.RED);
        g.fillRect(17, frameHeight -23, 96 * laserJuice / maxLaserJuice, 6);

        //display score
        g.setColor(Color.WHITE);
        g.fillRect(frameWidth - 115, frameHeight - 30, 100, 15);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Score: " + score, frameWidth - 100, frameHeight - 17);
    }

    public void actions() {
        checkLaserJuice();
        movePlayer();
        moveFighters();
        checkShooting();
        checkCollisions();
    }

    public void movePlayer() {
        // moves player

        if (d && player.posX < frameWidth - player.width)
            player.posX += 5;
        if (a && player.posX > 0)
            player.posX -= 5;
        if (w && player.posY > 0)
            player.posY -= 5;
        if (s && player.posY < frameHeight - player.height)
            player.posY += 5;
    }

    

    public void checkShooting(){
        if (upLaser && target.posY < player.posY && target.posX - 20 <= player.posX - 10 && player.posX <= target.posX + target.width){
            target.health --;
        
        if (target.health < 0){
            target = new Spacetarget(r.nextInt(500) + 50, r.nextInt(350) + 50, 30, 30, 0, 0, 20, Color.ORANGE);
        }
    }

    }

    

    public void moveFighters() {
        Random r = new Random();

        for (SpaceFighter f : fighters) {
            f.addTimer(1);
            f.previousX = f.getX();
            f.previousY = f.getY();

            while (f.getTimer() > 150 || f.getMoveX() == 0 || f.getMoveY() == 0) {
                f.clearTimer();
                f.setMoveX(r.nextInt(9) - 4);
                f.setMoveY(r.nextInt(9) - 4);
            }

            if (f.getX() > frameWidth - f.getWidth() || f.getX() < 10) {
                f.setMoveX(f.getMoveX() * (-1));
                f.setX(f.previousX + f.getMoveX() * 3);
            }

            if (f.getY() > frameHeight - f.getHeight() || f.getY() < 10) {
                f.setMoveY(f.getMoveY() * (-1));
                f.setY(f.previousY + f.getMoveY() * 3);
            }

            f.setX(f.getX() + f.getMoveX());
            f.setY(f.getY() + f.getMoveY());
        }
    }

    public void checkLaserJuice() {
        // turns on lasers
        if (up && laserJuice > 0)
            upLaser = true;
        else
            upLaser = false;

        if (down && laserJuice > 0)
            downLaser = true;
        else
            downLaser = false;

        if (left && laserJuice > 0)
            leftLaser = true;
        else
            leftLaser = false;

        if (right && laserJuice > 0)
            rightLaser = true;
        else
            rightLaser = false;

        // uses laser juice

        if (upLaser)
            laserJuice--;

        if (downLaser)
            laserJuice--;

        if (leftLaser)
            laserJuice--;

        if (rightLaser)
            laserJuice--;

        // recharges laser juice

        if (!upLaser && !downLaser && !leftLaser && !rightLaser && laserJuice < 100) {
            laserJuiceBufferCount++;
        }

        if (upLaser || downLaser || leftLaser || rightLaser) {
            rechargeLaserJuice = false;
            laserJuiceBufferCount = 0;
        }

        if (laserJuiceBufferCount == 100) {
            laserJuiceBufferCount = 0;
            rechargeLaserJuice = true;
        }
        if (laserJuice > 99) {
            rechargeLaserJuice = false;
        }

        if (rechargeLaserJuice) {
            laserJuice++;
        }
    }

    public void checkCollisions() {
        for (SpaceFighter f : fighters) {
            if (fighterIntersects(f)) {
                running = false;
                player.color = Color.RED;
            }
        }
        if (pelletIntersects(pellet)) {
            pellet = new SpacePellet(r.nextInt(frameWidth-100) + 50, r.nextInt(frameHeight-100) + 50, 20, 20, 0, 0, 0, Color.MAGENTA);
            score ++;
        }
    }

    public boolean fighterIntersects(SpaceFighter f) {
        return (f.posX - 20 <= player.posX - 10 && player.posX <= f.posX + f.width)
                && (f.posY - 10 <= player.posY && player.posY <= f.posY + f.height);
    }

    public boolean pelletIntersects(SpacePellet f) {
        return (f.posX - 20 <= player.posX - 10 && player.posX <= f.posX + f.width)
                && (f.posY - 10 <= player.posY && player.posY <= f.posY + f.height);
    }

    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
