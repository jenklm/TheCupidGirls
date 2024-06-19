import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class ShootingGame extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;

    private JPanel nicknamePanel;
    private JTextField nicknameField;
    private JButton confirmButton;
    
    private Image mainScreen = new ImageIcon("src/images/main_screen.png").getImage();
    private Image howtoplayscreen = new ImageIcon("src/images/howtoplay_screen.png").getImage();
    private Image selectcharScreen = new ImageIcon("src/images/selectchar_screen.png").getImage(); //
    private Image selectchar01Screen = new ImageIcon("src/images/selectchar01_screen.png").getImage(); //
    private Image selectchar02Screen = new ImageIcon("src/images/selectchar02_screen.png").getImage(); //
    private Image selectchar03Screen = new ImageIcon("src/images/selectchar03_screen.png").getImage(); //
    private Image nicknameScreen = new ImageIcon("src/images/nickname_screen.png").getImage(); //
    private Image gamestartalertScreen = new ImageIcon("src/images/gamestartalert_screen.png").getImage(); //

    private Image game02Screen = new ImageIcon("src/images/game02_screen.png").getImage();
    
    private Image character01 = new ImageIcon("src/images/pinku_fly01.png").getImage();
    private Image character02 = new ImageIcon("src/images/greeny_fly01.png").getImage();
    private Image character03 = new ImageIcon("src/images/bluecat_fly01.png").getImage();
    private Image selectedCharacterImage;
    
    private boolean isMainScreen, isSelectCharScreen, isSelectChar01Screen, isSelectChar02Screen, isSelectChar03Screen, isNicknameScreen, isGameStartAlertScreen, isHowtoPlayScreen, isGame02Screen; //
    
    private boolean isPaused = false;
    
    private Game game = new Game();

    
    private Audio backgroundMusic;
    private Audio gameMusic = new Audio("src/audio/gameBGM.wav", false);
    private float currentVolume = 0.5f;
   

    public ShootingGame() {
        setTitle("TheCupidGirls");
        // setUndecorated(true);  // 제거함으로써 제목 표시줄, 닫기, 최소화 버튼 생성됨. 
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);

        nicknamePanel = new JPanel();
        nicknamePanel.setBounds(300, 300, 600, 200); 
        nicknamePanel.setBackground(Color.WHITE);
        nicknamePanel.setLayout(null);
        
        nicknameField = new JTextField();
        nicknameField.setBounds(50, 50, 300, 30);
        nicknameField.setEditable(true);
        nicknamePanel.add(nicknameField);
        
        confirmButton = new JButton("확인"); 
        confirmButton.setBounds(380, 50, 100, 30);
        confirmButton.addActionListener(e -> handleConfirmButtonClick());
        nicknamePanel.add(confirmButton);
        
        // nicknameField에 텍스트 입력 안되는 오류 해결하기 위한 수정코드01
        SwingUtilities.invokeLater(() -> {
            nicknameField.requestFocusInWindow();
        });
        
        add(nicknamePanel);
        
        
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
        isGame02Screen = false;
        

        backgroundMusic = new Audio("src/audio/menuBGM.wav", true);
        backgroundMusic.start();
        backgroundMusic.setVolume(currentVolume);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handlekeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handlekeyReleased(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handlemouseClicked(e);
            }
        });
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
    
    private void handleConfirmButtonClick() {
        String nickname = nicknameField.getText().trim();
        
        if (!nickname.isEmpty()) {

            isNicknameScreen = false; 
            gameStart(selectedCharacterImage, nickname); 
        } else {
            JOptionPane.showMessageDialog(this, "닉네임을 입력해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void gameStart(Image playerImage, String nickname) {
        isMainScreen = false;
        isSelectCharScreen = false;
        isSelectChar01Screen = false;
        isSelectChar02Screen = false;
        isSelectChar03Screen = false;
        isNicknameScreen = false;
        isGameStartAlertScreen = true;
        
        game.setPlayer(playerImage);
        game.setNickname(nickname); // 게임 객체에 닉네임 설정
        
        Timer loadingTimer = new Timer();
        TimerTask loadingTask = new TimerTask() {
            @Override
            public void run() {
            	game.setPlayer(playerImage);
                backgroundMusic.stop();
                isGameStartAlertScreen = false;
                isGame02Screen = true;
                
                game.start();
                requestFocus();
                
             // nicknameField에 텍스트 입력 안되는 오류 해결하기 위한 수정코드02
                SwingUtilities.invokeLater(() -> {
                    requestFocus();
                });
            }
        };
        loadingTimer.schedule(loadingTask, 3000);
    }
     
    private void returnToMainScreen() {
        isMainScreen = true;
        isSelectCharScreen = false;
        isSelectChar01Screen = false;
        isSelectChar02Screen = false;
        isSelectChar03Screen = false;
        isNicknameScreen = false;
        isGameStartAlertScreen = false;
        isHowtoPlayScreen = false;
        isGame02Screen = false;
        
        // 게임 상태 초기화
        game.reset();

        // 배경 음악 재생
        if (gameMusic != null) {
            gameMusic.stop();
        }
        backgroundMusic = new Audio("src/audio/menuBGM.wav", true);
        backgroundMusic.start();
        
        isPaused = false;
    }
    
  

    public void paint(Graphics g) {
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage, 0, 0, this);
        
        if (isNicknameScreen) {
            g.drawImage(nicknameScreen, 0, 0, null);
            
            // nicknamePanel의 위치를 계산.
            int panelX = (Main.SCREEN_WIDTH - nicknamePanel.getWidth()) / 2;
            int panelY = (Main.SCREEN_HEIGHT - nicknamePanel.getHeight()) / 2;
            nicknamePanel.setBounds(panelX, panelY, nicknamePanel.getWidth(), nicknamePanel.getHeight());
            
            nicknamePanel.paint(g);
        }
    }
    
    private void adjustVolume(float volume) {
        currentVolume = volume; // Update current volume
        backgroundMusic.setVolume(volume);
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
        if (!isPaused) {
            if (isMainScreen) {
                g.drawImage(mainScreen, 0, 0, null);
            }
            // Draw other screens and game components here
            if (isGame02Screen) {
                g.drawImage(game02Screen, 0, 0, null);
                game.gameDraw(g); // Draw game components
            }
        }
        this.repaint();
    }
    private void handlekeyPressed(KeyEvent e) {
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
            	if (isGame02Screen) {
                    if (!isPaused) {
                        isPaused = true;
                        showSettingsPanel();
                    } else {
                        isPaused = false;
                        
                    }
                } else {
                    System.exit(0);
                }
                break;
        }
    }

    private void handlekeyReleased(KeyEvent e) {
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

    private void handlemouseClicked(MouseEvent e) {
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
            } else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                isSelectCharScreen = false;
                isMainScreen = true;
            } else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
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
            } else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                isSelectChar01Screen = false;
                isMainScreen = true;
            } else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
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
            } else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                isSelectChar02Screen = false;
                isMainScreen = true;
            } else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
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
            } else if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                isSelectChar03Screen = false;
                isMainScreen = true;
            } else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
                isSelectChar03Screen = false;
                isNicknameScreen = true;
                selectedCharacterImage = character03;
            }
        } else if (isNicknameScreen) {
            if (mouseX >= 73 && mouseX <= 108 && mouseY >= 424 && mouseY <= 494) {
                isNicknameScreen = false;
                isSelectCharScreen = true;
            } else if (mouseX >= 1171 && mouseX <= 1206 && mouseY >= 424 && mouseY <= 494) {
                isNicknameScreen = false;
                String nickname = nicknameField.getText();
                gameStart(selectedCharacterImage, nickname);
            }
        } else if (isGame02Screen) {
            if (mouseX >= 1200 && mouseX <= 1255 && mouseY >= 30 && mouseY <= 85) {
                showSettingsPanel();
            }
        }

        if (isNicknameScreen && confirmButton.getBounds().contains(e.getPoint())) {
            handleConfirmButtonClick();
        }
    }

    private void showSettingsPanel() {
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(900, 500);
        settingsFrame.setLayout(new BorderLayout());
        settingsFrame.getContentPane().setBackground(Color.lightGray);

        JLabel settingsLabel = new JLabel("Settings Panel", SwingConstants.CENTER);
        settingsLabel.setForeground(Color.WHITE);
        settingsFrame.add(settingsLabel, BorderLayout.CENTER);
        
        JPanel volumePanel = new JPanel();   // 소리 조절 기능 관련 
        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.Y_AXIS));
        volumePanel.setBackground(Color.lightGray);

        JLabel volumeLabel = new JLabel("Volume Control", SwingConstants.CENTER);
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        volumePanel.add(volumeLabel);

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (currentVolume * 100));  // 소리 조절 가능 슬라이더 만들기..
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setPaintTicks(true);
        volumePanel.add(volumeSlider);

        volumeSlider.addChangeListener(e -> {
            float volume = volumeSlider.getValue() / 100.0f; 
            adjustVolume(volume); 
        });
       


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.black);

        JButton returnButton = new JButton("Return to Main Screen");
        returnButton.setFont(new Font("Arial", Font.PLAIN, 25));
        returnButton.setBackground(Color.WHITE);
        returnButton.setFocusPainted(false);

        returnButton.addActionListener(e -> {
            returnToMainScreen();
            isPaused = false;
            settingsFrame.dispose();
        });

        buttonPanel.add(returnButton);

        settingsFrame.add(settingsLabel, BorderLayout.CENTER);
        settingsFrame.add(volumePanel, BorderLayout.CENTER);
        settingsFrame.add(buttonPanel, BorderLayout.WEST);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //setting 화면 사이즈 관련 
        int x = (screenSize.width - settingsFrame.getWidth()) / 2;
        int y = (screenSize.height - settingsFrame.getHeight()) / 2;
        settingsFrame.setLocation(x, y);
        
        

        settingsFrame.setVisible(true);
        isPaused = true;
    }
    
    
    
    
    public static void main(String[] args) {
    	 SwingUtilities.invokeLater(() -> {
             new ShootingGame();
         });    }
}