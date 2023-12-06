import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class test extends JFrame implements KeyListener {
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    public test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setLocationRelativeTo(null);
        setVisible(true);

        // Example: continuously move in the pressed direction
        while (true) {
            if (wPressed) {
                System.out.println("Moving UP");
            }
            if (aPressed) {
                System.out.println("Moving LEFT");
            }
            if (sPressed) {
                System.out.println("Moving DOWN");
            }
            if (dPressed) {
                System.out.println("Moving RIGHT");
            }

            try {
                Thread.sleep(100); // Adjust sleep duration as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new test();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            wPressed = true;
        } else if (key == KeyEvent.VK_A) {
            aPressed = true;
        } else if (key == KeyEvent.VK_S) {
            sPressed = true;
        } else if (key == KeyEvent.VK_D) {
            dPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            wPressed = false;
        } else if (key == KeyEvent.VK_A) {
            aPressed = false;
        } else if (key == KeyEvent.VK_S) {
            sPressed = false;
        } else if (key == KeyEvent.VK_D) {
            dPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle keyTyped event if needed
    }
}
