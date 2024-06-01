import javax.swing.*;
import java.awt.*;

public class Enemy {
    Image enemy01_01;
    Image explosionImage; // Replace with your explosion image
    int x, y;
    int width;
    int height;
    int hp = 10;
    
    private int state;
    private int animationCnt;

    public static final int ALIVE = 0;
    public static final int HIT = 1;
    public static final int DESTROYED = 2;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = ALIVE;
        
        // Load the original monster image
        Image originalImage01 = new ImageIcon("src/images/enemy01_01.png").getImage();
        int originalWidth01 = originalImage01.getWidth(null);
        int originalHeight01 = originalImage01.getHeight(null);

        // Scale the image
        width = (int) (originalWidth01 * 2 / 3.0);
        height = (int) (originalHeight01 * 2 / 3.0);
        enemy01_01 = originalImage01.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Load and scale the explosion image if needed
        Image originalExplosionImage = new ImageIcon("src/images/enemy01_02.png").getImage();
        int originalWidth02 = originalExplosionImage.getWidth(null);
        int originalHeight02 = originalExplosionImage.getHeight(null);
        width = (int) (originalWidth02 * 2 / 3.0);
        height = (int) (originalHeight02 * 2 / 3.0);
        explosionImage = originalExplosionImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void move() {
    	 if (state == ALIVE) {
             this.x -= 7;
         }
    }
    
    public void hit() {
        this.state = HIT;
        this.animationCnt = 10; // duration of the hit animation
    }

    public void update() {
        if (state == HIT) {
            animationCnt--;
            if (animationCnt <= 0) {
                state = DESTROYED;
            }
        }
    }

    public boolean isAlive() {
        return state != DESTROYED;
    }

    public int getState() {
        return state;
    }

    public Image getAnimationFrame() {
        // Return the explosion image. You can modify this method to return different frames if you have an animated sequence
        return explosionImage;
    }
}