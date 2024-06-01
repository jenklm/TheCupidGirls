import javax.swing.*;
import java.awt.*;

public class Enemy {
    Image monster;
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
        Image originalImage = new ImageIcon("src/images/monster01.png").getImage();
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        // Scale the image to 50%
        width = originalWidth / 2;
        height = originalHeight / 2;
        monster = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Load and scale the explosion image if needed
        Image originalExplosionImage = new ImageIcon("src/images/enemy.png").getImage();
        explosionImage = originalExplosionImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void move() {
    	 if (state == ALIVE) {
             this.x -= 7;
         }
    }
    
    public void hit() {
        this.state = HIT;
        this.animationCnt = 20; // duration of the hit animation
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