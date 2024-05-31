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
    private Image howtoplayscreen = new ImageIcon("src/images/howtoplay_screen.png").getImage();
    private Image selectcharScreen = new ImageIcon("src/images/selectchar_screen.png").getImage(); //
    private Image selectchar01Screen = new ImageIcon("src/images/selectchar01_screen.png").getImage(); //
    private Image selectchar02Screen = new ImageIcon("src/images/selectchar02_screen.png").getImage(); //
    private Image selectchar03Screen = new ImageIcon("src/images/selectchar03_screen.png").getImage(); //
    private Image nicknameScreen = new ImageIcon("src/images/nickname_screen.png").getImage(); //
    private Image gamestartalertScreen = new ImageIcon("src/images/gamestartalert_screen.png").getImage(); //

    private Image gameScreen = new ImageIcon("src/images/game_screen.png").getImage();
    
    private Image character01 = new ImageIcon("src/images/pinku_fly01.png").getImage();
    private Image character02 = new ImageIcon("src/images/greeny_fly01.png").getImage();
    private Image character03 = new ImageIcon("src/images/bluecat_fly01.png").getImage();
    private Image selectedCharacterImage;
    
    private boolean isMainScreen, isSelectCharScreen, isSelectChar01Screen, isSelectChar02Screen, isSelectChar03Screen, isNicknameScreen, isGameStartAlertScreen, isHowtoPlayScreen, isGameScreen; //

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
        isSelectChar01Screen = false;
        isSelectChar02Screen = false;
        isSelectChar03Screen = false;
        isNicknameScreen = false;
        isGameStartAlertScreen = false;
        isHowtoPlayScreen = false;
        isGameScreen = false;
        

        backgroundMusic = new Audio("src/audio/menuBGM.wav", true);
        backgroundMusic.start();

        addKeyListener(new KeyListener());
        addMouseListener(new MouseListener()); //
    }
    
    
    //
    private void showHowtoPlayScreen() {
        isMainScreen = false;
        isHowtoPlayScreen = true;
    }
    
    //   
    private void selectChar() {
        isMainScreen = false;
        isSelectCharScreen = true;
    }
    

    private void gameStart(Image playerImage) {
        isMainScreen = false;
        isSelectCharScreen = false;
        isSelectChar01Screen = false;
        isSelectChar02Screen = false;
        isSelectChar03Screen = false;
        isNicknameScreen = false;
        isGameStartAlertScreen = true;
        
        game.setPlayer(playerImage);
        
        Timer loadingTimer = new Timer();
        TimerTask loadingTask = new TimerTask() {
            @Override
            public void run() {
            	game.setPlayer(playerImage);
                backgroundMusic.stop();
                isGameStartAlertScreen = false;
                isGameScreen = true;
                
                game.start();
            }
        };
        loadingTimer.schedule(loadingTask, 3000);
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
        if (isHowtoPlayScreen) {
            g.drawImage(howtoplayscreen, 0, 0, null);
        }
        if (isSelectCharScreen) {
        	 g.drawImage(selectcharScreen, 0, 0, null);
        }
        if (isSelectChar01Screen) {
            g.drawImage(selectchar01Screen, 0, 0, null);
        }
        if (isSelectChar02Screen) {
            g.drawImage(selectchar02Screen, 0, 0, null);
        }
        if (isSelectChar03Screen) {
            g.drawImage(selectchar03Screen, 0, 0, null);
        }
        if (isNicknameScreen) {
            g.drawImage(nicknameScreen, 0, 0, null);
        }
        if (isGameStartAlertScreen) {
            g.drawImage(gamestartalertScreen, 0, 0, null);
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
                    selectChar();
                } else if (mouseX >= 827 && mouseX <= 1160 && mouseY >= 494 && mouseY <= 608) {
                    showHowtoPlayScreen();
                }
            } else if (isHowtoPlayScreen) {
                if (mouseX >= 62 && mouseX <= 97 && mouseY >= 64 && mouseY <= 99) {
                    isHowtoPlayScreen = false;
                    isMainScreen = true;
                } else if (mouseX >= 519 && mouseX <= 762 && mouseY >= 569 && mouseY <= 648) {
                    isHowtoPlayScreen = false;
                    isSelectCharScreen = true;
                }
            } else if (isSelectCharScreen) {
                if (mouseX >= 170 && mouseX <= 448 && mouseY >= 274 && mouseY <= 629) {
                    isSelectCharScreen = false;
                    isSelectChar01Screen = true;
                } else if (mouseX >= 501 && mouseX <= 779 && mouseY >= 274 && mouseY <= 629) {
                    isSelectCharScreen = false;
                    isSelectChar02Screen = true;
                    
                } else if (mouseX >= 832 && mouseX <= 1110 && mouseY >= 274 && mouseY <= 629) {
                    isSelectCharScreen = false;
                    isSelectChar03Screen = true;
                    
                }
                // 뒤로 가기 (메인 화면) 구현. 
                else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                    isSelectCharScreen = false;
                    isMainScreen = true;
                }
                // 다음 스크린으로 가기 구현.
                else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
                    isSelectCharScreen = false;
                    isNicknameScreen = true;
                }
            } else if (isSelectChar01Screen) {
                if (mouseX >= 501 && mouseX <= 779 && mouseY >= 274 && mouseY <= 629) {
                    isSelectChar01Screen = false;
                    isSelectChar02Screen = true;
                } else if (mouseX >= 832 && mouseX <= 1110 && mouseY >= 274 && mouseY <= 629) {
                    isSelectChar01Screen = false;
                    isSelectChar03Screen = true;
                }
                // 뒤로 가기 구현. 
                else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                    isSelectChar01Screen = false;
                    isMainScreen = true;
                }
                // 다음 스크린으로 가기 구현.
                else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
                    isSelectChar01Screen = false;
                    isNicknameScreen = true;
                    
                    selectedCharacterImage = character01;
                }
            } else if (isSelectChar02Screen) {
                if (mouseX >= 170 && mouseX <= 448 && mouseY >= 274 && mouseY <= 629) {
                    isSelectChar02Screen = false;
                    isSelectChar01Screen = true;
                } else if (mouseX >= 832 && mouseX <= 1110 && mouseY >= 274 && mouseY <= 629) {
                    isSelectChar02Screen = false;
                    isSelectChar03Screen = true;
                }
                // 뒤로 가기 구현. 
                else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                    isSelectChar02Screen = false;
                    isMainScreen = true;
                }
                // 다음 스크린으로 가기 구현.
                else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
                    isSelectChar02Screen = false;
                    isNicknameScreen = true;
                    
                    selectedCharacterImage = character02;
                }
            } else if (isSelectChar03Screen) {
                if (mouseX >= 170 && mouseX <= 448 && mouseY >= 274 && mouseY <= 629) {
                    isSelectChar03Screen = false;
                    isSelectChar01Screen = true;
                } else if (mouseX >= 501 && mouseX <= 779 && mouseY >= 274 && mouseY <= 629) {
                    isSelectChar03Screen = false;
                    isSelectChar02Screen = true;
                }
                // 뒤로 가기 구현. 
                else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                    isSelectChar03Screen = false;
                    isMainScreen = true;
                }
                // 다음 스크린으로 가기 구현.
                else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
                    isSelectChar03Screen = false;
                    isNicknameScreen = true;
                    
                    selectedCharacterImage = character03;
                }
            } else if (isNicknameScreen) {
            	if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                    isNicknameScreen = false;
                    isSelectCharScreen = true;
                }
                // 다음 스크린으로 가기 구현.
                else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
                	 isNicknameScreen = false;
                	 gameStart(selectedCharacterImage);
                }
            } 
        
        }
    }
    
    
    public static void main(String[] args) {
        new ShootingGame();
    }
}