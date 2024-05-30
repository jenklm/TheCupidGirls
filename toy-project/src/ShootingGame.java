import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter; //
import java.awt.event.MouseEvent; //
import java.util.Timer;
import java.util.TimerTask;

public class ShootingGame extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;

    private Image mainScreen = new ImageIcon("src/images/main_screen.png").getImage();
    private Image selectcharScreen = new ImageIcon("src/images/selectchar_screen.png").getImage(); //
    private Image loadingScreen = new ImageIcon("src/images/loading_screen.png").getImage();
    private Image gameScreen = new ImageIcon("src/images/game_screen.png").getImage();
    
    private Image character1 = new ImageIcon("src/images/pinku_fly01.png").getImage();
    private Image character2 = new ImageIcon("src/images/greeny_fly01.png").getImage();
    private Image character3 = new ImageIcon("src/images/bluecat_fly01.png").getImage();

    private boolean isMainScreen, isSelectCharScreen, isLoadingScreen, isGameScreen; //

    private Game game = new Game();

    private Audio backgroundMusic;

    public ShootingGame() {
        setTitle("TheCupidGirls");
        // setUndecorated(true);  // 제거함으로써 제목 표시줄, 닫기, 최소화 버튼 생성됨. 
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);

        init();
    }

    private void init() {
        isMainScreen = true;
        isSelectCharScreen = false; // 
        isLoadingScreen = false;
        isGameScreen = false;
        

        backgroundMusic = new Audio("src/audio/menuBGM.wav", true);
        backgroundMusic.start();

        addKeyListener(new KeyListener());
        addMouseListener(new MouseListener()); //
    }
    
    //
    private void showLoadingScreen() {
        isMainScreen = false;
        isLoadingScreen = true;
    }
    
    //
    private void gameStart() {
        isMainScreen = false;
        isSelectCharScreen = true; 

        Timer loadingTimer = new Timer();
        TimerTask loadingTask = new TimerTask() {
            @Override
            public void run() {
                backgroundMusic.stop();
                isLoadingScreen = false;
                isGameScreen = true;
                game.start();
            }
        };
        loadingTimer.schedule(loadingTask, 1200);
    }

    public void paint(Graphics g) {
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage, 0, 0, null);
    }

    public void screenDraw(Graphics g) {
        if (isMainScreen) {
            g.drawImage(mainScreen, 0, 0, null);
        }
        //
        if (isSelectCharScreen) {
        	 g.drawImage(selectcharScreen, 0, 0, null);
        }
        if (isLoadingScreen) {
            g.drawImage(loadingScreen, 0, 0, null);
        }
        if (isGameScreen) {
            g.drawImage(gameScreen, 0, 0, null);
            game.gameDraw(g);
        }
        this.repaint();
    }

    class KeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    game.setUp(true);
                    break;
                case KeyEvent.VK_DOWN:
                    game.setDown(true);
                    break;
                case KeyEvent.VK_LEFT:
                    game.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.setRight(true);
                    break;
                case KeyEvent.VK_R:
                    if (game.isOver()) game.reset();
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(true);
                    break;
                case KeyEvent.VK_ENTER:
                    if (isMainScreen) gameStart(); // 
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    game.setUp(false);
                    break;
                case KeyEvent.VK_DOWN:
                    game.setDown(false);
                    break;
                case KeyEvent.VK_LEFT:
                    game.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(false);
                    break;
            }
        }
    }
    
    // 메인 화면에서 start 버튼 클릭 시, 캐릭터 선택 창으로 이동. how to play 버튼 클릭 시, 안내창으로 이동. 
    class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
        	int mouseX = e.getX();
            int mouseY = e.getY();

            if (isMainScreen) {
                if (mouseX >= 827 && mouseX <= 1160 && mouseY >= 329 && mouseY <= 443) {
                    gameStart();
                } else if (mouseX >= 827 && mouseX <= 1160 && mouseY >= 494 && mouseY <= 608) {
                    showLoadingScreen();
                }
            } else if (isSelectCharScreen) {
                if (mouseX >= 170 && mouseX <= 448 && mouseY >= 274 && mouseY <= 629) {
                    game.setPlayer(character1);
                    gameStart();
                } else if (mouseX >= 501 && mouseX <= 779 && mouseY >= 274 && mouseY <= 629) {
                    game.setPlayer(character2);
                    gameStart();
                } else if (mouseX >= 832 && mouseX <= 1110 && mouseY >= 274 && mouseY <= 629) {
                    game.setPlayer(character3);
                    gameStart();
                }
            } else if (isLoadingScreen) {
            	// 로딩 화면에서 클릭 시 메인 화면으로 돌아가도록 설정
            	isLoadingScreen = false;
            	isMainScreen = true;
            }
        }
    }
    
    
    public static void main(String[] args) {
        new ShootingGame();
    }
}