package snake;
import java.awt.*;
import javax.swing.*;

import java.util.Random;
import java.util.ArrayList;
import java.awt.event.*;

public class SnakeGame extends JPanel implements ActionListener {

    boolean running = true;
    SnakeApple apple;
    ArrayList<Integer> snakeListX = new ArrayList<>();
    ArrayList<Integer> snakeListY = new ArrayList<>();
    ArrayList<Integer> prevSnakeListX = new ArrayList<>();
    ArrayList<Integer> prevSnakeListY = new ArrayList<>();
    boolean RR; // has gone right
    boolean RL; // has gone left
    boolean RU; // etc
    boolean RD;
    int tongueTimer = 0;
    int startingLength = 4;
    String direction = "X";
    JFrame myFrame;
    int frameWidth = 550;
    int frameLength = 575;

    public SnakeGame() {
        myFrame = new JFrame("snake");
        myFrame.add(this);
        myFrame.setSize(frameWidth, frameLength);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - myFrame.getWidth()) / 2;
        int y = (screenSize.height - myFrame.getHeight()) / 2 - 50;
        myFrame.setLocation(x, y);

        

        myFrame.setVisible(true);




        for (int i = 0; i < startingLength * 25; i += 25) {
            snakeListX.add(i + 125);
            snakeListY.add(250);
        }

        apple = new SnakeApple(300, 250, 1);

        myFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        myFrame.setFocusable(true);

    }

    public void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W && !RD) {
            direction = "U";
        } else if (key == KeyEvent.VK_A && !RR && !direction.equals("X")) {
            direction = "L";
        } else if (key == KeyEvent.VK_S && !RU) {
            direction = "D";
        } else if (key == KeyEvent.VK_D && !RL) {
            direction = "R";
        }
    }

    public void paintComponent(Graphics g) {
        if (running) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 550, 575);
            for (int i = 25; i < 525; i += 50) {
                for (int j = 25; j < 525; j += 50) {
                    g.setColor(Color.GRAY);

                    g.fillRect(i, j, 25, 25);
                    g.setColor(Color.GRAY);
                    g.fillRect(i + 25, j + 25, 25, 25);

                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(i + 25, j, 25, 25);
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(i, j + 25, 25, 25);
                }
            }

            drawApple(g);
            move(g);
            delay(100);
            tongueTimer++;
            if (tongueTimer == 4)
                tongueTimer = 0;
            super.repaint();
        } else {
            Font font = new Font("Arial", Font.BOLD, 36); // Change the font and size as needed
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 155, 225);

            // JFrame myFrame2 = new JFrame();
            JButton button = new JButton("REPLAY");
            JPanel panel = new JPanel();
            myFrame.add(panel);
            panel.add(button);
            myFrame.setVisible(true);
        }
    }

    public void checkEdge() {
        int head = snakeListX.size() - 1;
        if (snakeListX.get(head) == 525 || snakeListX.get(head) == 0 || snakeListY.get(head) == 550
                || snakeListY.get(head) == 0) {
            running = false;
        }
    }

    public void drawSnake(Graphics g) {
        int even = 0;
        int size = snakeListX.size();
        int headX = snakeListX.get(size - 1);
        int headY = snakeListY.get(size - 1);
        float[] darkGreen = Color.RGBtoHSB(88, 196, 65, null);
        float[] green = Color.RGBtoHSB(95, 217, 69, null);

        // draws head

        g.setColor(Color.getHSBColor(green[0], green[1], green[2]));
        g.fillRect(snakeListX.get(size - 1), snakeListY.get(size - 1), 25, 25);

        if (direction == "R" || direction == "X") {
            g.setColor(Color.BLACK);
            g.fillRect(headX + 13, headY + 5, 5, 3);
            g.fillRect(headX + 13, headY + 17, 5, 3);
            if (tongueTimer > 1) {
                g.setColor(Color.PINK);
                int[] xPoints = { headX + 25, headX + 25, headX + 50 };
                int[] yPoints = { headY + 10, headY + 15, headY + 13 };
                int nPoints = 3;
                g.fillPolygon(xPoints, yPoints, nPoints);
            }

        } else if (direction == "L") {
            g.setColor(Color.BLACK);
            g.fillRect(headX + 7, headY + 5, 5, 3);
            g.fillRect(headX + 7, headY + 17, 5, 3);
            if (tongueTimer > 1) {
                g.setColor(Color.PINK);
                int[] xPoints = { headX, headX, headX - 25 };
                int[] yPoints = { headY + 10, headY + 15, headY + 13 };
                int nPoints = 3;
                g.fillPolygon(xPoints, yPoints, nPoints);
            }

        } else if (direction == "D") {
            g.setColor(Color.BLACK);
            g.fillRect(headX + 5, headY + 13, 3, 5);
            g.fillRect(headX + 17, headY + 13, 3, 5);
            if (tongueTimer > 1) {
                g.setColor(Color.PINK);
                int[] xPoints = { headX + 10, headX + 15, headX + 13 };
                int[] yPoints = { headY + 25, headY + 25, headY + 50 };
                int nPoints = 3;
                g.fillPolygon(xPoints, yPoints, nPoints);
            }

        } else if (direction == "U") {
            g.setColor(Color.BLACK);
            g.fillRect(headX + 5, headY + 7, 3, 5);
            g.fillRect(headX + 17, headY + 7, 3, 5);
            if (tongueTimer > 1) {
                g.setColor(Color.PINK);
                int[] xPoints = { headX + 10, headX + 15, headX + 13 };
                int[] yPoints = { headY, headY, headY - 25 };
                int nPoints = 3;
                g.fillPolygon(xPoints, yPoints, nPoints);
            }

        }

        // draws body
        for (int i = size - 2; i >= 0; i--) {
            if (even % 2 != 0) {
                g.setColor(Color.getHSBColor(green[0], green[1], green[2]));
                g.fillRect(snakeListX.get(i), snakeListY.get(i), 25, 25);
            } else {
                g.setColor(Color.getHSBColor(darkGreen[0], darkGreen[1], darkGreen[2]));
                g.fillRect(snakeListX.get(i), snakeListY.get(i), 25, 25);
            }
            even++;
        }
    }

    public void move(Graphics g) {
        checkEdge();
        checkSnakeCollision();

        if (direction != "X") {
            


            int size = snakeListX.size();
            int lastSegmentX = snakeListX.get(0);
            int lastSegmentY = snakeListY.get(0);

            for (int i = 0; i < size - 1; i++) {
                snakeListX.set(i, snakeListX.get(i + 1));
                snakeListY.set(i, snakeListY.get(i + 1));
            }

            if (direction.equals("R")) {
                snakeListX.set(size - 1, snakeListX.get(size - 1) + 25);
                RR = true;
                RL = false;
                RU = false;
                RD = false;
            } else if (direction.equals("D")) {
                snakeListY.set(size - 1, snakeListY.get(size - 1) + 25);
                RR = false;
                RL = false;
                RU = false;
                RD = true;
            } else if (direction.equals("L")) {
                snakeListX.set(size - 1, snakeListX.get(size - 1) - 25);
                RR = false;
                RL = true;
                RU = false;
                RD = false;
            } else if (direction.equals("U")) {
                snakeListY.set(size - 1, snakeListY.get(size - 1) - 25);
                RR = false;
                RL = false;
                RU = true;
                RD = false;

            }
            if (checkFood()) {
                snakeListX.add(0, lastSegmentX);
                snakeListY.add(0, lastSegmentY);
                createApple();
            }

        }
        if (running)
            drawSnake(g);

    }

    public void createApple() {
        Random r = new Random();
        int x = snakeListX.get(0);
        int y = snakeListY.get(0);
        while ((snakeListX.contains(x))) {
            x = r.nextInt(20) * 25 + 25;
        }
        while ((snakeListY.contains(y))) {
            y = r.nextInt(20) * 25 + 25;
        }
        int level = r.nextInt(3) + 1;
        apple = new SnakeApple(x, y, level);
    }

    public boolean checkFood() {
        int head = snakeListX.size() - 1;
        return (snakeListX.get(head) == apple.getAppleX() && snakeListY.get(head) == apple.getAppleY());
    }

    public void checkSnakeCollision() {
        int headX = snakeListX.get(snakeListX.size() - 1);
        int headY = snakeListY.get(snakeListY.size() - 1);
        for (int i = 0; i < snakeListX.size() - 1; i++) {
            if (snakeListX.get(i) == headX && snakeListY.get(i) == headY) {
                running = false;
            }
        }
    }

    public void drawApple(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(apple.getAppleX() + 2, apple.getAppleY() + 2, 21, 21);
    }

    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}