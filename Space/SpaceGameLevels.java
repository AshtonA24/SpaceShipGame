package Space;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.event.*;

public class SpaceGameLevels extends JPanel implements KeyListener {
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
    int level = 1;
    boolean moveUp = true;
    boolean moveDown = true;
    boolean moveRight = true;
    boolean moveLeft = true;

    // changeable commands
    int playerSpeed = 5;
    int maxLaserJuice = 200;
    int delay = 7; //lower = more
    int fighterCount = 10;
    int maxProjectileSpeed = 5; 
    int projectileFrequency = 30; //lower = more
    int projectileSize = 30;
    int scoreToBoss = 5;
    int maxBossHealth = 500;
    int fighterSpeed = 4; //rounds up to nearest even number
    int bossAnimationSpeed = 10; //lower = more
    int bossShootingLength = 20;
    int targetSize = 40;


    int shootingStageTime;
    int bossHealth = maxBossHealth;
    String targetIntersectDirection;
    int frameWidth = 1200;
    int frameHeight = 800;
    int laserJuice = maxLaserJuice;
    boolean rechargeLaserJuice;
    ArrayList<SpaceFighter> fighters = new ArrayList<>();
    ArrayList<SpaceProjectile> projectiles = new ArrayList<>();
    ArrayList<SpaceProjectile> removeProjectiles = new ArrayList<>();
    SpaceShip player = new SpaceShip(0, frameHeight / 2, 10, 10, Color.GREEN);
    SpacePellet pellet = new SpacePellet(r.nextInt(400) + 50, r.nextInt(400) + 50, 20, 20, 0, 0, 0, Color.MAGENTA);
    SpaceTarget target = new SpaceTarget(r.nextInt(350) + 50, r.nextInt(350) + 50, targetSize, targetSize, 0, 0, 50, Color.ORANGE);
    SpaceBoss boss = new SpaceBoss(-200, -200, 100, 100, 1, 1, 0, bossHealth, Color.YELLOW);;
    SpaceBackground background = new SpaceBackground();
    Font font = new Font("ArAkayaKanadakaial", Font.BOLD, 16);
    int score;
    boolean scoreDeposit;

    public SpaceGameLevels() {
        JFrame frame = new JFrame("Space Fighters");
        frame.add(this);
        frame.setSize(frameWidth, frameHeight + 27); // +27 for mac, +40 for windows
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenX = (screenSize.width - frame.getWidth()) / 2;
        int screenY = (screenSize.height - frame.getHeight()) / 2 - 50;
        frame.setLocation(screenX, screenY);
        frame.setVisible(true);
        setFocusTraversalKeysEnabled(false);
        frame.addKeyListener(this);
        frame.setFocusable(true);

        if (fighterSpeed % 2 !=0)
            fighterSpeed++;

        

        // implement fighters
        for (int i = 0; i < fighterCount; i++) {
            fighters.add(new SpaceFighter(r.nextInt(frameWidth - 450) + 300, r.nextInt(frameHeight - 400) + 200, 30, 30, 1, 1, 150));
        }

        for(int i = 0; i < level-1; i++){
            nextLevel();
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
        } else if (key == KeyEvent.VK_UP &&!down && !left && !right) {
            up = true;
        } else if (key == KeyEvent.VK_DOWN &&!up && !left && !right) {
            down = true;
        } else if (key == KeyEvent.VK_LEFT &&!down && !up && !right) {
            left = true;
        } else if (key == KeyEvent.VK_RIGHT &&!down && !left && !up) {
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
        g.drawImage(background.myPic.getImage(), 0, 0, this);

        // lasers
        g.setColor(Color.RED);

        if (bossy) {
            if (upLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, boss.posY + (int)(boss.height/1.2));
            else if (upLaser)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, 0);
            if (downLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, boss.posY + (int)(boss.height/2.8));
            else if (downLaser)
                g.drawLine(player.posX + 5, player.posY + 5, player.posX + 5, frameHeight);
            if (leftLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, boss.posX + (int)(boss.width/1.7), player.posY + 5);
            else if (leftLaser)
                g.drawLine(player.posX + 5, player.posY + 5, 0, player.posY + 5);
            if (rightLaser && boss.laserHit)
                g.drawLine(player.posX + 5, player.posY + 5, boss.posX + (int)(boss.width/2), player.posY + 5);
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
            if (staticShooting)
                bossAnimationSpeed = 8; //spin speed when shooting
            else
                bossAnimationSpeed = 25; //spin speed when moving

                if (boss.laserHit)
                    g.setColor(Color.WHITE);
                g.setColor(boss.color);
                if (boss.imageTimer < bossAnimationSpeed){
                    g.drawImage(boss.images.get(0).getImage(), boss.posX, boss.posY, this);
                    if(boss.laserHit)
                        g.drawImage(boss.imagesHit.get(0).getImage(), boss.posX, boss.posY, this);
                }
                else if (boss.imageTimer < bossAnimationSpeed * 2){
                    g.drawImage(boss.images.get(1).getImage(), boss.posX, boss.posY, this);
                    if(boss.laserHit)
                        g.drawImage(boss.imagesHit.get(1).getImage(), boss.posX, boss.posY, this);
                }
                else if (boss.imageTimer < bossAnimationSpeed * 3){
                    g.drawImage(boss.images.get(2).getImage(), boss.posX, boss.posY, this);
                    if(boss.laserHit)
                        g.drawImage(boss.imagesHit.get(2).getImage(), boss.posX, boss.posY, this);
                }
                else if (boss.imageTimer < bossAnimationSpeed * 4){
                    g.drawImage(boss.images.get(3).getImage(), boss.posX, boss.posY, this);
                    if(boss.laserHit)
                        g.drawImage(boss.imagesHit.get(3).getImage(), boss.posX, boss.posY, this);
                }
                else 
                    boss.imageTimer = 0;
                boss.imageTimer ++;
            

            g.setColor(Color.WHITE); //health bar
            g.fillRect(boss.posX + boss.width/2 - 40, boss.posY + boss.height + 10, 80, 6);
            g.setColor(Color.GREEN); //healthbar
            g.fillRect(boss.posX + boss.width/2 - 40, boss.posY + boss.height + 10, (int) (80 * (1.0*boss.health/maxBossHealth)), 6);
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
        g.drawString("Level: " + level, frameWidth - 100, frameHeight - 17);

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
        player.previousX = player.posX;
        player.previousY = player.posY;
        if ((d && player.posX < frameWidth - player.width) )
            player.posX += playerSpeed;
        if ((a && player.posX > 0) )
            player.posX -= playerSpeed;
        if ((w && player.posY > 0) )
            player.posY -= playerSpeed;
        if ((s && player.posY < frameHeight - player.height))
            player.posY += playerSpeed;
        checkTargetCollision();
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
                nextLevel();
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
                target = new SpaceTarget(r.nextInt(500) + 50, r.nextInt(350) + 50, targetSize, targetSize, 0, 0, 50, Color.ORANGE);
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
                f.timer = r.nextInt(125);
                f.setMoveX(r.nextInt(fighterSpeed+1) - fighterSpeed/2);
                f.setMoveY(r.nextInt(fighterSpeed+1) - fighterSpeed/2);
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

            checkFighterTargetCollision(f);
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


    public void checkTargetCollision(){
        boolean betweenY = (target.posY - player.height <= player.posY && player.posY  <= target.posY + target.height);
        boolean betweenX = (target.posX - player.width <= player.posX && player.posX  <= target.posX + target.width);
        boolean touchingLeft = (player.previousX + player.width < target.posX && player.posX +player.width >= target.posX);
        boolean touchingRight = (player.previousX > target.posX + target.width && player.posX <= target.posX + target.width);
        boolean touchingTop = (player.previousY + player.height < target.posY && player.posY + player.height >= target.posY);
        boolean touchingBottom = (player.previousY > target.posY + target.height && player.posY <= target.posY + target.height);
        if (betweenY && (touchingLeft || touchingRight))
            player.posX = player.previousX;
        if (betweenX && (touchingTop || touchingBottom))
            player.posY = player.previousY;
    }

    public void checkFighterTargetCollision(SpaceFighter f){
        boolean betweenY = (target.posY - f.height <= f.posY && f.posY  <= target.posY + target.height);
        boolean betweenX = (target.posX - f.width <= f.posX && f.posX  <= target.posX + target.width);
        boolean touchingLeft = (f.previousX + f.width < target.posX && f.posX +f.width >= target.posX);
        boolean touchingRight = (f.previousX > target.posX + target.width && f.posX <= target.posX + target.width);
        boolean touchingTop = (f.previousY + f.height < target.posY && f.posY + f.height >= target.posY);
        boolean touchingBottom = (f.previousY > target.posY + target.height && f.posY <= target.posY + target.height);
        if (betweenY && (touchingLeft || touchingRight)){
            f.posX = f.previousX;
            f.moveX = f.moveX * -1;
        }
        if (betweenX && (touchingTop || touchingBottom)){
            f.posY = f.previousY;
            f.moveY = f.moveY * -1;
        }
        
        
    }

    public void checkBossFight() {
        if (score == scoreToBoss) {
            bossy = true;
            laserJuice = maxLaserJuice;
            player.posX = frameWidth / 2;
            player.posY = (int)(frameHeight / 1.5);
            fighters.clear();
            target.posX = -100;
            target.posY = -100;
            boss = new SpaceBoss(frameWidth / 2 - 275, frameHeight / 2 - boss.height/2, 100, 100, 3, 0, 0 , bossHealth, Color.YELLOW);
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
                shootingStageTime = r.nextInt(bossShootingLength);
            }
            // ends shooting stage
            if (staticShootingCount > shootingStageTime) {
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
        target = new SpaceTarget(r.nextInt(350) + 50, r.nextInt(350) + 50, targetSize, targetSize, 0, 0, 50, Color.ORANGE);
        for (int i = 0; i < fighterCount; i++)
            fighters.add(new SpaceFighter(r.nextInt(frameWidth - 450) + 300, r.nextInt(frameHeight - 400) + 200, 30, 30, 1, 1, 150));
        score = 0;
        maxBossHealth = 500;
        boss.health = maxBossHealth;
        bossHealth = maxBossHealth;
        bossy = false;
        bossStageTimer = 0;
        staticShooting = false;
        boss.posX = -200;
        boss.posY = -200;
        fighterCount = 10;
        maxProjectileSpeed = 5; 
        projectileFrequency = 30; //lower = more
        maxBossHealth = 500;
        fighterSpeed = 4; //rounds up to nearest even number
        bossShootingLength = 20;
        level = 1;

        delay(2000);
        running = true;
        super.repaint();
    }

    public void nextLevel() {
        laserJuice = maxLaserJuice;
        player.posX = 0;
        player.posY = frameHeight / 2;
        fighters.clear();
        projectiles.clear();
        player.color = Color.GREEN;
        pellet = new SpacePellet(r.nextInt(400) + 50, r.nextInt(400) + 50, 20, 20, 0, 0, 0, Color.MAGENTA);
        target = new SpaceTarget(r.nextInt(350) + 50, r.nextInt(350) + 50, targetSize, targetSize, 0, 0, 50, Color.ORANGE);
        for (int i = 0; i < fighterCount; i++)
            fighters.add(new SpaceFighter(r.nextInt(frameWidth - 450) + 300, r.nextInt(frameHeight - 400) + 200, 30, 30, 1, 1, 150));
        maxBossHealth += 500;
        bossHealth+=500;
        boss.health+=500;
        fighterCount += 3;
        maxProjectileSpeed += 5;
        if (projectileFrequency > 10)
            projectileFrequency -= 5;
        fighterSpeed += 2;
        bossShootingLength +=5;
        bossy = false;
        bossStageTimer = 0;
        staticShooting = false;
        boss.posX = -200;
        boss.posY = -200;
        score = 0;
        level++;
        delay(1000);
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
        SwingUtilities.invokeLater(() ->new SpaceGameLevels());
    }
}