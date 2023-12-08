package Space;
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
    boolean laserHit;
    boolean bossy;
    boolean staticShooting;
    int bossStageTimer;
    int staticShootingCount;

    // changeable commands
    int speed = 5;
    int maxLaserJuice = 200;
    int laserJuice = maxLaserJuice;
    int frameWidth = 1200;
    int frameHeight = 800;
    int starCount = 500;
    int delay = 7;
    int fighterCount = 15;
    int maxProjectileSpeed = 15;
    int projectileFrequency = 15;
    int projectileSize = 30;
    int scoreToBoss = 1;
    int bossHealth = 500;

    boolean rechargeLaserJuice;
    ArrayList<SpaceFighter> fighters = new ArrayList<>();
    ArrayList<SpaceProjectile> projectiles = new ArrayList<>();
    ArrayList<SpaceProjectile> removeProjectiles = new ArrayList<>();
    SpaceShip player = new SpaceShip(0, frameHeight / 2, 10, 10, Color.GREEN);
    SpacePellet pellet = new SpacePellet(r.nextInt(400) + 50, r.nextInt(400) + 50, 20, 20, 0, 0, 0, Color.MAGENTA);
    SpaceTarget target = new SpaceTarget(r.nextInt(350) + 50, r.nextInt(350) + 50, 30, 30, 0, 0, 50, Color.ORANGE);
    SpaceBoss boss = new SpaceBoss(-200, -200, 100, 100, 1, 1, 0, bossHealth, Color.YELLOW);;
    Font font = new Font("ArAkayaKanadakaial", Font.BOLD, 16);
    int score;
    boolean scoreDeposit;
    ArrayList<Integer> stars = new ArrayList<>();

    public SpaceGame() {
        JFrame frame = new JFrame("Space Fighters");
        frame.add(this);
        frame.setSize(frameWidth, frameHeight + 27); // +27 for mac, + 40 for windows
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenX = (screenSize.width - frame.getWidth()) / 2;
        int screenY = (screenSize.height - frame.getHeight()) / 2 - 50;
        frame.setLocation(screenX, screenY);
        frame.setVisible(true);
        setFocusTraversalKeysEnabled(false);
        frame.addKeyListener(this);
        frame.setFocusable(true);

        // implement fighters
        for (int i = 0; i < fighterCount; i++) {
            fighters.add(new SpaceFighter(r.nextInt(frameWidth - 150) + 100, r.nextInt(frameHeight - 100) + 100, 30, 30,
                    1, 1, r.nextInt(125)));
        }

        for (int i = 0; i < starCount; i++) {
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
            g.fillRect(0, 0, frameWidth, frameWidth);
            draw(g);
            g.fillRect(maxLaserJuice, delay, WIDTH, HEIGHT);
            restartGame();
        }
    }

    public void draw(Graphics g) {

        // stars
        g.setColor(Color.WHITE);
        for (int i = 0; i < starCount; i += 2) {
            g.fillOval(stars.get(i), stars.get(i + 1), 2, 4);
            g.fillOval(stars.get(i) - 1, stars.get(i + 1) + 1, 2, 4);
        }

        // lasers
        g.setColor(Color.RED);

        if (bossy) {
            if (upLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, boss.posY + boss.height);
            else if (upLaser)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, 0);
            if (downLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, boss.posY);
            else if (downLaser)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, frameHeight);
            if (leftLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, boss.posX + boss.width, player.posY + 5);
            else if (leftLaser)
                g.drawLine(player.posX + 5, player.posY + 5, 0, player.posY + 5);
            if (rightLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, boss.posX, player.posY + 5);
            else if (rightLaser)
                g.drawLine(player.posX + 5, player.posY + 5, frameWidth, player.posY + 5);
        } else {

            if (upLaser && target.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, target.posY + target.height);
            else if (upLaser)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, 0);
            if (downLaser && target.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, target.posY);
            else if (downLaser)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, frameHeight);
            if (leftLaser && target.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, target.posX + target.width, player.posY + 5);
            else if (leftLaser)
                g.drawLine(player.posX + 5, player.posY + 5, 0, player.posY + 5);
            if (rightLaser && target.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, target.posX, player.posY + 5);
            else if (rightLaser)
                g.drawLine(player.posX + 5, player.posY + 5, frameWidth, player.posY + 5);
        }

        // pelets
        g.setColor(pellet.color);
        g.fillOval(pellet.posX, pellet.posY, pellet.width, pellet.height);

        // target
        g.setColor(target.color);
        g.fillRoundRect(target.posX, target.posY, target.width, target.height, 3, 3);

        // player ship
        g.setColor(player.color);
        int[] xPoints = { player.posX + 0, player.posX + 1, player.posX + 3, player.posX + 6, player.posX + 8,
                player.posX + 9, player.posX + 9, player.posX + 8, player.posX + 1, player.posX + 0 };
        int[] yPoints = { player.posY + 5, player.posY + 5, player.posY + 0, player.posY + 0, player.posY + 5,
                player.posY + 5, player.posY + 7, player.posY + 9, player.posY + 9, player.posY + 7 };
        int nPoints = 10;
        g.fillPolygon(xPoints, yPoints, nPoints);

        // enemy fighters
        g.setColor(Color.BLUE);
        for (SpaceFighter f : fighters) {
            g.fillRoundRect(f.getX(), f.getY(), f.getHeight(), f.getWidth(), 10, 10);
        }

        // draw boss and projectiles and health bar
        if (bossy) {
            g.setColor(Color.RED);
            for (SpaceProjectile p : projectiles)
                g.fillOval(p.posX, p.posY, p.width, p.height);
            g.setColor(boss.color);
            if (boss.laserHit)
                g.setColor(Color.WHITE);
            g.fillRect(boss.posX, boss.posY, boss.width, boss.height);
            g.setColor(Color.WHITE);
            g.fillRect(boss.posX + boss.width/2 - 40, boss.posY + boss.height + 10, 80, 6);
            g.setColor(Color.GREEN);
            g.fillRect(boss.posX + boss.width/2 - 40, boss.posY + boss.height + 10, (int) (80 * (boss.health/500.0)), 6);
        }

        // laser juice meter
        g.setColor(Color.WHITE);
        g.fillRect(15, frameHeight - 50, 200, 20);
        g.setColor(Color.RED);
        g.fillRect(17, frameHeight - 48, 196 * laserJuice / maxLaserJuice, 16);

        // display score
        g.setColor(Color.WHITE);
        g.fillRect(frameWidth - 115, frameHeight - 30, 100, 15);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Score: " + projectiles.size(), frameWidth - 100, frameHeight - 17);

    }

    public void actions() {
        checkLaserJuice();
        movePlayer();
        moveFighters();
        checkShooting();
        checkCollisions();
        checkBossFight();
        bossFight();
    }

    public void movePlayer() {
        // moves player
        if (d && player.posX < frameWidth - player.width)
            player.posX += speed;
        if (a && player.posX > 0)
            player.posX -= speed;
        if (w && player.posY > 0)
            player.posY -= speed;
        if (s && player.posY < frameHeight - player.height)
            player.posY += speed;
    }

    public void checkShooting() {
        if (bossy) {
            int temp = boss.health;
            if (upLaser && boss.posY < player.posY
                    && (boss.posX - 20 <= player.posX - 10 && player.posX <= boss.posX + (boss.width)))
                boss.health--;
            if (downLaser && boss.posY > player.posY
                    && (boss.posX - 20 <= player.posX - 10 && player.posX <= boss.posX + boss.width))
                boss.health--;
            if (leftLaser && boss.posX < player.posX
                    && (boss.posY - 10 <= player.posY && player.posY <= boss.posY + boss.height))
                boss.health--;
            if (rightLaser && boss.posX > player.posX
                    && (boss.posY - 10 <= player.posY && player.posY <= boss.posY + boss.height))
                boss.health--;

            if (boss.health != temp) {
                boss.color = Color.WHITE;
                boss.laserHit = true;
            } else {
                boss.color = Color.ORANGE;
                boss.laserHit = false;
            }

            if (boss.health < 0) {
                restartGame();
            }
        } else {

            int temp = target.health;
            if (upLaser && target.posY < player.posY
                    && (target.posX - 20 <= player.posX - 10 && player.posX <= target.posX + (target.width)))
                target.health--;
            if (downLaser && target.posY > player.posY
                    && (target.posX - 20 <= player.posX - 10 && player.posX <= target.posX + target.width))
                target.health--;
            if (leftLaser && target.posX < player.posX
                    && (target.posY - 10 <= player.posY && player.posY <= target.posY + target.height))
                target.health--;
            if (rightLaser && target.posX > player.posX
                    && (target.posY - 10 <= player.posY && player.posY <= target.posY + target.height))
                target.health--;

            if (target.health != temp) {
                target.color = Color.WHITE;
                target.laserHit = true;
            } else {
                target.color = Color.ORANGE;
                target.laserHit = false;
            }

            if (target.health < 0) {
                target = new SpaceTarget(r.nextInt(500) + 50, r.nextInt(350) + 50, 30, 30, 0, 0, 50, Color.ORANGE);
                score++;
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

    }

    public void checkCollisions() {
        for (SpaceFighter f : fighters) {
            if (fighterIntersects(f)) {
                running = false;
                player.color = Color.RED;
            }
        }

        for (SpaceProjectile f : projectiles) {
            if (projectileIntersects(f)) {
                running = false;
                player.color = Color.RED;
            }
            if ((f.getX() > frameWidth || f.getX() < -10) || (f.moveX == 0 && f.moveY == 0)) {
                removeProjectiles.add(f);
            }
        }

        for (SpaceProjectile f : removeProjectiles) {
            projectiles.remove(f);
        }

        removeProjectiles.clear();

        if (bossIntersects(boss)) {
            running = false;
            player.color = Color.RED;
        }

        if (pelletIntersects(pellet)) {
            pellet = new SpacePellet(r.nextInt(frameWidth - 100) + 50, r.nextInt(frameHeight - 100) + 50, 20, 20, 0, 0,
                    0, Color.MAGENTA);
            laserJuice = maxLaserJuice;
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

    public boolean projectileIntersects(SpaceProjectile f) {
        return (f.posX - 20 <= player.posX - 10 && player.posX <= f.posX + f.width)
                && (f.posY - 10 <= player.posY && player.posY <= f.posY + f.height);
    }

    public boolean bossIntersects(SpaceBoss f) {
        return (f.posX - 20 <= player.posX - 10 && player.posX <= f.posX + f.width)
                && (f.posY - 10 <= player.posY && player.posY <= f.posY + f.height);
    }

    public void checkBossFight() {
        if (score == scoreToBoss) {
            bossy = true;
            laserJuice = maxLaserJuice;
            player.posX = frameWidth / 2;
            player.posY = frameHeight / 2;
            fighters.clear();
            target.posX = -100;
            target.posY = -100;
            boss = new SpaceBoss(frameWidth / 2 - 275, 250, 100, 100, 3, 0, 0 , bossHealth, Color.YELLOW);
            score++;

        }
    }

    public void bossFight() {
        if (bossy) {
            if (boss.timer > projectileFrequency && staticShooting) {
                staticShootingCount++;
                boss.timer = 0;
                projectiles.add(new SpaceProjectile(boss.posX + boss.width / 2, boss.posY + boss.height / 2,
                        projectileSize, projectileSize, r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2,
                        r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2, 0));
                projectiles.add(new SpaceProjectile(boss.posX + boss.width / 2, boss.posY + boss.height / 2,
                        projectileSize, projectileSize, r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2,
                        r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2, 0));
                projectiles.add(new SpaceProjectile(boss.posX + boss.width / 2, boss.posY + boss.height / 2,
                        projectileSize, projectileSize, r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2,
                        r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2, 0));
                projectiles.add(new SpaceProjectile(boss.posX + boss.width / 2, boss.posY + boss.height / 2,
                        projectileSize, projectileSize, r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2,
                        r.nextInt(maxProjectileSpeed + 1) - maxProjectileSpeed / 2, 0));
            }

            for (SpaceProjectile p : projectiles) {
                p.posX += p.moveX;
                p.posY += p.moveY;
            }
            // while not in shooting stage
            if (!staticShooting) {
                boss.posX += boss.moveX;
                bossStageTimer++;
            }

            // starts shooting stage
            if (bossStageTimer > 150) {
                staticShooting = true;
                bossStageTimer = 0;
            }
            // ends shooting stage
            if (staticShootingCount > 20) {
                staticShooting = false;
                staticShootingCount = 0;
                boss.moveX *= -1;
            }

            boss.timer++;
        }
    }

    public void restartGame() {
        laserJuice = maxLaserJuice;
        player.posX = 0;
        player.posY = frameHeight / 2;
        fighters.clear();
        projectiles.clear();
        player.color = Color.GREEN;
        pellet = new SpacePellet(r.nextInt(400) + 50, r.nextInt(400) + 50, 20, 20, 0, 0, 0, Color.MAGENTA);
        target = new SpaceTarget(r.nextInt(350) + 50, r.nextInt(350) + 50, 30, 30, 0, 0, 50, Color.ORANGE);
        for (int i = 0; i < fighterCount; i++)
            fighters.add(new SpaceFighter(r.nextInt(frameWidth - 150) + 100, r.nextInt(frameHeight - 100) + 100, 30, 30,
                    1, 1, r.nextInt(125)));
        score = 0;
        boss.health = bossHealth;
        bossy = false;
        bossStageTimer = 0;
        staticShooting = false;
        boss.posX = -200;
        boss.posY = -200;
        delay(2000);
        running = true;
        super.repaint();
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

    public static void main(String[] args) {
        new SpaceGame();
    }
}
