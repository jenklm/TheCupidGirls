import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Thread {
    private int delay = 20;
    private int cnt;
    private int score;

    private Image player; //
    private Image playerheart;
    
    private Image settings;

    private int playerX, playerY;
    private int playerWidth;
    private int playerHeight;
    private int playerSpeed = 10;
    private int playerHp = 5;

    private String nickname;
    
    private boolean thisStage; // 
    private boolean up, down, left, right, shooting;
    

    
    private boolean isOver;
    private boolean nextStage = false; //
    private boolean gameOver = false; //

    private ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
    private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    private ArrayList<EnemyAttack> enemyAttackList = new ArrayList<EnemyAttack>();

    private PlayerAttack playerAttack;
    private Enemy enemy;
    private EnemyAttack enemyAttack;

    private Audio backgroundMusic;
    private Audio hitSound;
    
    private boolean showRestartMessage = false;
    
    // 생성자에서 이미지 로드 및 크기 조정
    public Game() {
        String imagePath = "src/images/pinku_fly01.png";
        setPlayer(new ImageIcon(imagePath).getImage());
        
        String heartImagePath = "src/images/playerheart.png";
        Image originalHeartImage = new ImageIcon(heartImagePath).getImage();
        int heartWidth = originalHeartImage.getWidth(null) / 3;
        int heartHeight = originalHeartImage.getHeight(null) / 3;
        playerheart = originalHeartImage.getScaledInstance(heartWidth, heartHeight, Image.SCALE_SMOOTH);
        
        String settingsImagePath = "src/images/settings.png";
        Image originalSettingsImage = new ImageIcon(settingsImagePath).getImage();
        int settingsWidth = originalHeartImage.getWidth(null) / 3;
        int settingsHeight = originalHeartImage.getHeight(null) / 3;
        settings = originalSettingsImage.getScaledInstance(settingsWidth, settingsHeight, Image.SCALE_SMOOTH);
        
        try {
            hitSound = new Audio("src/audio/hitSound.wav", false);
        } catch (Exception e) {
            System.err.println("Error loading hit sound: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
 


    @Override
    public void run() {
    	
    	// 게임 시작 시 배경 음악을 재생하는 코드
        if (backgroundMusic != null) {
            backgroundMusic.start();
        } else {
            backgroundMusic = new Audio("src/audio/gameBGM.wav", true);
            backgroundMusic.start();
        }
        
        while (true) {
            synchronized(this) {
                while (!isOver) {
                    long startTime = System.currentTimeMillis();
                    
                    keyProcess();
                    playerAttackProcess();
                    enemyAppearProcess();
                    enemyMoveProcess();
                    enemyAttackProcess();
                    
                    cnt++;
                    
                    long endTime = System.currentTimeMillis();
                    long sleepTime = delay - (endTime - startTime);
                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                if (isOver && !showRestartMessage) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            showRestartMessage = true;
                        }
                    }, 3000); // 3초 후에 showRestartMessage를 true로 설정
                }
                
                try {
                    wait(); // 게임이 리셋되거나 새로 시작되기를 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reset() {
        isOver = false;
        gameOver = false;
        cnt = 0;
        score = 0;
        playerX = 10;
        playerY = (Main.SCREEN_HEIGHT - playerHeight) / 2;
        playerHp = 5; // restart 했을 때, playerHP 바 안보이는 버그 해결..

        playerAttackList.clear();
        enemyList.clear();
        enemyAttackList.clear();
        
        // 배경음악 중지 및 재시작
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        backgroundMusic = new Audio("src/audio/gameBGM.wav", true);
        backgroundMusic.start();

        // 초기화 시 효과음 중지
        if (hitSound != null) {
            hitSound.stop();
        }

        // 게임 루프가 새로운 상태로 동작하도록 초기화
        nextStage = false;
        showRestartMessage = false;

        // 초기화 시 스레드 재시작
        synchronized(this) {
            notify(); // 스레드가 대기 상태일 경우 깨움
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    private void keyProcess() {
        if (up && playerY - playerSpeed > 0) playerY -= playerSpeed;
        if (down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT) playerY += playerSpeed;
        if (left && playerX - playerSpeed > 0) playerX -= playerSpeed;
        if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH) playerX += playerSpeed;
        if (shooting && cnt % 15 == 0) {
            playerAttack = new PlayerAttack(playerX + 150, playerY + 40); // 공격 나가는 위치 바꾸기..
            playerAttackList.add(playerAttack);
        }
    }

    private void playerAttackProcess() {
    	for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            playerAttack.fire();

            for (int j = 0; j < enemyList.size(); j++) {
                enemy = enemyList.get(j);
                if (playerAttack.x > enemy.x && playerAttack.x < enemy.x + enemy.width &&
                    playerAttack.y > enemy.y && playerAttack.y < enemy.y + enemy.height) {
                    enemy.hit();
                    playerAttackList.remove(playerAttack);

                    
                    if (hitSound != null) {
                        hitSound.start();
                    } else {
                        System.err.println("hitSound is not initialized.");
                    }

                    score += 1000; // 점수 즉시 증가.
                    break;
                }
            }
    	}
    	
    	if (score >= 5000) {
            isOver = true;
        }
    }

    private void enemyAppearProcess() {
        if (cnt % 80 == 0) {
            enemy = new Enemy(1120, (int)(Math.random()*621));
            enemyList.add(enemy);
        }
    }

    private void enemyMoveProcess() {
        for (int i = 0; i< enemyList.size(); i++) {
            enemy = enemyList.get(i);
            enemy.move();
            enemy.update(); 
            if (!enemy.isAlive()) {
                enemyList.remove(i);
                i--; 
            }
        }
    }

    private void enemyAttackProcess() {
        if (cnt % 50 == 0) {
            enemyAttack = new EnemyAttack(enemy.x - 79, enemy.y + 35);
            enemyAttackList.add(enemyAttack);
        }

        for (int i = 0; i < enemyAttackList.size(); i++) {
            enemyAttack = enemyAttackList.get(i);
            enemyAttack.fire();

            if (enemyAttack.x > playerX & enemyAttack.x < playerX + playerWidth && enemyAttack.y > playerY && enemyAttack.y < playerY + playerHeight) {
                hitSound.start();
                playerHp -= 1;
                enemyAttackList.remove(enemyAttack);
                if (playerHp <= 0) {
                	isOver = true; 
                	gameOver = true; //
                }
            }
        }
    }

    public void gameDraw(Graphics g) {
        playerDraw(g);
        enemyDraw(g);
        infoDraw(g);
    }

    public void infoDraw(Graphics g) {
    	g.setColor(Color.WHITE);
    	g.setFont(new Font("Inter", Font.BOLD, 35));
    	String scoreText = "SCORE : " + score;
    	int scoreY = 55; // 상단에서 15픽셀 간격 유지
    	Rectangle scoreRect = new Rectangle(0, 0, Main.SCREEN_WIDTH, g.getFontMetrics().getHeight());
    	scoreRect.setLocation(0, scoreY);  // scoreY 값을 기준으로 위치 설정
    	drawCenteredString(g, scoreText, scoreRect);
        
        if (isOver) {
            if (showRestartMessage) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Irish Grover", Font.BOLD, 80));
                drawCenteredString(g, "Press R to restart", new Rectangle(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT));
            } else {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Irish Grover", Font.BOLD, 120));
                if (score >= 5000) {
                    drawCenteredString(g, nickname + ", GAME CLEAR", new Rectangle(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT));
                } else {
                    drawCenteredString(g, nickname + ", GAME OVER", new Rectangle(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT));
                }
            }
        }
        
        int settingsX = Main.SCREEN_WIDTH - settings.getWidth(null) - 10;
        int settingsY = 10;
        g.drawImage(settings, settingsX, settingsY, null);
    }
    
    private void drawCenteredString(Graphics g, String text, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }

    public void playerDraw(Graphics g) {
        g.drawImage(player, playerX, playerY, null);
        
        
        int heartWidth = playerheart.getWidth(null);

        for (int i = 0; i < playerHp; i++) {
            int heartX = 10 + (i * heartWidth);
            g.drawImage(playerheart, heartX, 10, null);
        }

        if (!isOver) {
            g.setColor(Color.WHITE); 
            g.setFont(new Font("Inter", Font.BOLD, 30)); 
            g.drawString("5000점을 달성하세요!", 25, 140);
          }
        
        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
        }
    }

    public void enemyDraw(Graphics g) {
        for (int i = 0; i< enemyList.size(); i++) {
            enemy = enemyList.get(i);
            if (enemy.getState() == Enemy.ALIVE) {
                g.drawImage(enemy.enemy01_01, enemy.x, enemy.y, null);
                
            } else if (enemy.getState() == Enemy.HIT) {
                g.drawImage(enemy.getAnimationFrame(), enemy.x, enemy.y, null);
            }
        } 
        for (int i = 0; i < enemyAttackList.size(); i++) {
            enemyAttack = enemyAttackList.get(i);
            g.drawImage(enemyAttack.image, enemyAttack.x, enemyAttack.y, null);
        }
    }

    //
    public void setPlayer(Image playerImage) {
    	this.player = playerImage;
    	
    	int originalWidth = player.getWidth(null);
    	int originalHeight = player.getHeight(null);
    	playerWidth = originalWidth/3;
    	playerHeight = originalHeight/3;
    	this.player=player.getScaledInstance(playerWidth, playerHeight, Image.SCALE_SMOOTH);
    	
    }
    //
    public Image getPlayer() {
    	return this.player;
    }
    
    public boolean getThisStage() {
		return this.thisStage;
		
	}
    

    
    //
    public boolean nextStage() {
        return nextStage;
    }
    
    //
    public boolean isOver() {
        return isOver;
    }
    
    //
    public boolean gameOver() {
        return gameOver;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
    

}