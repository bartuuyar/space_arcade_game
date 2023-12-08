package SpaceInvaders;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.imageio.ImageIO;

public class Game extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final int PLAYER_SIZE = 50;
    private static final int BULLET_SIZE = 5;
    int health = 500;
    int shield = 0;

    List<Rectangle> bullets = new ArrayList<>();
    List<Enemy1> enemies = new ArrayList<>();
    List<Enemy2> enemies2 = new ArrayList<>();
    List<Enemy3> enemies3 = new ArrayList<>();
    private List<BulletE> enemyBullets;
    private Leaderboard0 leaderboard;
    

    private BufferedImage rocketImage;
    private BufferedImage monster1Image;
    private BufferedImage monster2Image;
    private BufferedImage monster3Image;
    private BufferedImage backgroundImage;
    private int playerX = WIDTH / 2 - PLAYER_SIZE / 2;
    private int playerY = HEIGHT - PLAYER_SIZE;
    private int bulletX;
    private int bulletY;
    private int cameraY = 0;

    private int score = 0;
    private Timer enemy2ShootTimer;
    
    Clip laserClip;   // Sound clip for laser shooting
    Clip musicClip;   // Sound clip for background music

    public Game() {
    	try {
            // Load sound files
            laserClip = loadSound("lazer.wav");
            musicClip = loadSound("music.wav");

            // Play the background music on a loop
            //musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            
            // Rest of your code...
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    	try {
            backgroundImage = ImageIO.read(new File("space.jpg"));
            rocketImage = ImageIO.read(new File("ship.png"));
            monster1Image = ImageIO.read(new File("monster1.png"));
            monster2Image = ImageIO.read(new File("monster2.png"));
            monster3Image = ImageIO.read(new File("monster3.png"));
            backgroundImage = ImageIO.read(new File("space.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    	leaderboard = new Leaderboard0();
    	enemyBullets = new ArrayList<>();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    playerX -= 5;
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    playerX += 5;
                } else if (keyCode == KeyEvent.VK_UP) {
                    playerY -= 5;
                    if (playerY < HEIGHT)
                        cameraY -= 5;
                }  
            }
            public void keyReleased(KeyEvent c) {
            	int keyCode = c.getKeyCode();
            	if (keyCode == KeyEvent.VK_SPACE) {
                    shoot();}                   
                }            
        });

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Random rand = new Random();
            	int upperbound = 100;
            	int int_random = rand.nextInt(upperbound);
            	if(int_random<1) {
            		if(score<7) {
            			spawnEnemy1();
            		}          		
            	}
            	else {
            		if(int_random < 3) {
            			if(score>5) {
            				musicClip.stop();
            	            musicClip.close();
            				spawnEnemy2();
            			}        			
            		}
            	}
            	
            		if(int_random <4) {
            			if(score>10) {
            				spawnEnemy3();
            			}
            		}
            	
            
                update();
                updateBulletPositions();
                checkCollision();
                repaint();
                
            }
        });
        timer.start();
        enemy2ShootTimer = new Timer(2000, new ActionListener() {
        	    public void actionPerformed(ActionEvent e) {
        	        for (Enemy2 enemy : enemies2) {
        	            enemy.shoot();;
        	        }
        	        
        	    }
        	});
        	enemy2ShootTimer.start();
        	
	
}
    
     Clip loadSound(String filename) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filename));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }
     void playSound(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }
    private void shoot() {
        bulletX = playerX + (PLAYER_SIZE / 2) - (BULLET_SIZE / 2);
        bulletY = playerY;
        Rectangle bullet = new Rectangle(bulletX, bulletY, BULLET_SIZE, BULLET_SIZE);
        bullets.add(bullet);
        playSound(laserClip);
        repaint();
    }

    private void update() {
        
        Iterator<Enemy1> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy1 enemy = enemyIterator.next();
            enemy.r.y += Enemy1.ENEMY_SPEED;
            if (enemy.r.y > cameraY + HEIGHT) {
                enemyIterator.remove();
            }
        }
        Iterator<Enemy2> enemy2Iterator = enemies2.iterator();
        while (enemy2Iterator.hasNext()) {
            Enemy2 enemy = enemy2Iterator.next();
            enemy.r.y += Enemy2.ENEMY_SPEED;
            if (enemy.r.y > cameraY + HEIGHT) {
                enemy2Iterator.remove();
            }                     
            updateEnemyBullets(enemy.getBullets()); // Update enemy bullets
            
        }
        Iterator<Enemy3> enemyIterator3 = enemies3.iterator();
        while (enemyIterator3.hasNext()) {
            Enemy3 enemy = enemyIterator3.next();
            enemy.r.y += Enemy3.ENEMY_SPEED;
            if (enemy.r.y > cameraY + HEIGHT) {
                enemyIterator3.remove();
            }
        }
    }

    private void spawnEnemy1() {
        int enemyX = (int) (Math.random() * (WIDTH - Enemy1.ENEMY_SIZE));
        int enemyY = cameraY - Enemy1.ENEMY_SIZE;
        Enemy1 enemy0 = new Enemy1(new Rectangle(enemyX, enemyY, Enemy1.ENEMY_SIZE, Enemy1.ENEMY_SIZE));
        enemies.add(enemy0);
    }
    private void spawnEnemy2() {
        int enemyX = (int) (Math.random() * (WIDTH - Enemy2.ENEMY_SIZE));
        int enemyY = cameraY - Enemy2.ENEMY_SIZE;
        Enemy2 enemy2 = new Enemy2(new Rectangle(enemyX, enemyY, Enemy2.ENEMY_SIZE, Enemy2.ENEMY_SIZE));
        enemies2.add(enemy2);
    }
    private void spawnEnemy3() {
        int enemyX = (int) (Math.random() * (WIDTH - Enemy3.ENEMY_SIZE));
        int enemyY = cameraY - Enemy3.ENEMY_SIZE;
        Enemy3 enemy3 = new Enemy3(new Rectangle(enemyX, enemyY, Enemy3.ENEMY_SIZE, Enemy3.ENEMY_SIZE));
        enemies3.add(enemy3);
    }

    private void updateBulletPositions() {
        Iterator<Rectangle> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Rectangle bullet = bulletIterator.next();
            bullet.y -= 5;
            if (bullet.y + bullet.height < cameraY) {
                bulletIterator.remove();
            }
        }
    }
    private void updateEnemyBullets(List<BulletE> bullets) {
    	Rectangle player = new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        Iterator<BulletE> bulletIteratorE = bullets.iterator();
        while (bulletIteratorE.hasNext()) {
            BulletE bullet = bulletIteratorE.next();
            bullet.update();
            if (bullet.y + BulletE.BULLET_SIZE < cameraY) {
                bulletIteratorE.remove();
            }
        }
        enemyBullets.addAll(bullets);
    }

    private void checkCollision() {
        Iterator<Rectangle> bulletIterator = bullets.iterator();
        Rectangle player = new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        while (bulletIterator.hasNext()) {
            Rectangle bullet = bulletIterator.next();

            Iterator<Enemy1> enemyIterator = enemies.iterator();
            Iterator<Enemy2> enemyIterator2 = enemies2.iterator();
            Iterator<Enemy3> enemyIterator3 = enemies3.iterator();
            while (enemyIterator.hasNext()) {
                Enemy1 enemy = enemyIterator.next();
                if (bullet.intersects(enemy.r)) {
                    enemy.takeDamage();
                    bulletIterator.remove();
                    if (enemy.isDead()) {
                        enemyIterator.remove();

                        score++;
                    }
                }
            }
            while (enemyIterator2.hasNext()) {
                Enemy2 enemy2 = enemyIterator2.next();
                if (bullet.intersects(enemy2.r)) {
                	if(enemy2.isPrintible ==1) {
                		enemy2.takeDamage();
                        bulletIterator.remove();
                        if (enemy2.isDead()) {                        
                        	enemy2.makeInvincible();
                            score++;
                        }                	
                	}
                }
            }
            while (enemyIterator3.hasNext()) {
                Enemy3 enemy = enemyIterator3.next();
                if (bullet.intersects(enemy.r)) {
                    enemy.takeDamage();
                    bulletIterator.remove();
                    if (enemy.isDead()) {
                        enemyIterator3.remove();

                        score++;
                    }
                }
            }
        }
        Iterator<BulletE> enemyBulletIterator = enemyBullets.iterator();
        while (enemyBulletIterator.hasNext()) {
            BulletE enemyBullet = enemyBulletIterator.next();
            if (enemyBullet.getBounds().intersects(player)) {
                enemyBulletIterator.remove();
                health--;
            
            }
        }
        Iterator<Enemy1> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy1 enemy = enemyIterator.next();
            if (enemy.r.intersects(player)) {
            	health--;
            }
        }
        Iterator<Enemy2> enemyIterator2 = enemies2.iterator();
        while (enemyIterator2.hasNext()) {
            Enemy2 enemy2 = enemyIterator2.next();
            if (enemy2.r.intersects(player)) {
            	health--;
            }
        }
        Iterator<Enemy3> enemyIterator3 = enemies3.iterator();
        while (enemyIterator3.hasNext()) {
            Enemy3 enemy = enemyIterator3.next();
            if (enemy.r.intersects(player)) {
            	health -= 2;
            }
        }
    }

    private void gameOver() {    	
    	musicClip.stop();
    	musicClip.close();
    	// Dispose of the game frame
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame gameFrame = (JFrame) window;
            gameFrame.dispose();
        }
    	String playerName = JOptionPane.showInputDialog(null, "Game Over! Enter your name:");
        leaderboard.updateLeaderboard(playerName, score);
    	   
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
        // Draw the health bar
        g.setColor(Color.RED);
        g.fillRect(10, 30, health/5 , 10);
        g.setColor(Color.WHITE);
        g.drawRect(10, 30, 100, 10);
        if(health<0) {
        	gameOver();
        }
        // Draw the player
        g.drawImage(rocketImage, playerX, playerY - cameraY, PLAYER_SIZE, PLAYER_SIZE, null);

        // Draw the bullets
        g.setColor(Color.YELLOW);
        for (Rectangle bullet : bullets) {
            g.fillRect(bullet.x, bullet.y - cameraY, BULLET_SIZE, BULLET_SIZE);
        }
        // Draw the enemy bullets
        g.setColor(Color.RED);
        for (BulletE bullet : enemyBullets) {
            g.fillRect(bullet.x, bullet.y - cameraY, BulletE.BULLET_SIZE, BulletE.BULLET_SIZE);
        }
        
        // Draw the enemies
        for (Enemy1 enemy : enemies) {
            g.drawImage(monster1Image, enemy.r.x, enemy.r.y - cameraY, Enemy1.ENEMY_SIZE, Enemy1.ENEMY_SIZE, null);
        }
        for (Enemy2 enemy2 : enemies2) {
        	if(enemy2.isPrintible ==1) {
        		g.drawImage(monster2Image, enemy2.r.x, enemy2.r.y - cameraY, Enemy2.ENEMY_SIZE, Enemy2.ENEMY_SIZE, null);
        	}       
        }
        for (Enemy3 enemy : enemies3) {
            g.drawImage(monster3Image, enemy.r.x, enemy.r.y - cameraY, Enemy3.ENEMY_SIZE, Enemy3.ENEMY_SIZE, null);
        }
        
    }
    void initializeGame() {
        JFrame frame = new JFrame("Space Shooting Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(new Game());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    
}
class Leaderboard0 {
    private static final String LEADERBOARD_FILE ="leaderboard.txt";

    private List<ScoreEntry> leaderboardData;

    public Leaderboard0() {
        leaderboardData = new ArrayList<>();
        loadLeaderboardData();
    }

    private void loadLeaderboardData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LEADERBOARD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    ScoreEntry entry = new ScoreEntry(name, score);
                    leaderboardData.add(entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLeaderboard(String playerName, int playerScore) {
        ScoreEntry newEntry = new ScoreEntry(playerName, playerScore);
        leaderboardData.add(newEntry);
        leaderboardData.sort((e1, e2) -> Integer.compare(e2.score, e1.score)); // Sort in descending order

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LEADERBOARD_FILE))) {
            for (ScoreEntry entry : leaderboardData) {
                writer.write(entry.name + "," + entry.score);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ScoreEntry {
        private String name;
        private int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
