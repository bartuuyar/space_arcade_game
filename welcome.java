package SpaceInvaders;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;


public class welcome extends JFrame implements KeyListener,ActionListener{

	
	JLabel label;
	ImageIcon icon1;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu helpMenu;
	JMenuItem regItem;
	JMenuItem playItem;
	JMenuItem scoreItem;
	JMenuItem exitItem;
	JMenuItem aboutItem;
	JMenuItem contentsItem;
	
	JButton sbutton;
	JTextField textField;
	
	welcome(){		
		
		icon1 = new ImageIcon("startscreen.png");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(425,275,400,400);
		this.setLayout(null);
		this.addKeyListener(this);
		this.setResizable(false);
		label = new JLabel();
		label.setIcon(icon1);	
		label.setBounds(0,0,385,400);
		this.add(label);
		this.setVisible(true);
		menuBar = new JMenuBar();
		  fileMenu = new JMenu("File");
		  
		  helpMenu = new JMenu("Help");
		  regItem = new JMenuItem("Register");
		  playItem = new JMenuItem("Play Game");
		  scoreItem = new JMenuItem("High Score");
		  exitItem = new JMenuItem("Exit");
		  aboutItem = new JMenuItem("about");
		  contentsItem = new JMenuItem("content");
		  playItem.addActionListener(this);
		  scoreItem.addActionListener(this); 
		  exitItem.addActionListener(this);
		  aboutItem.addActionListener(this);
		  contentsItem.addActionListener(this);
		  regItem.addActionListener(this);
		  fileMenu.add(exitItem);
		  fileMenu.add(regItem);
		  fileMenu.add(scoreItem);
		  fileMenu.add(playItem);
		  helpMenu.add(aboutItem);
		  helpMenu.add(contentsItem);
		  menuBar.add(fileMenu);
		  menuBar.add(helpMenu);
		  this.add(menuBar);
		  this.setJMenuBar(menuBar);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.dispose();
    	SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.initializeGame();
        });
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playItem) {
            // Start the game
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                Game game = new Game();
                game.initializeGame();
                
            });
		}
	    if(e.getSource()==regItem) {
	    	JOptionPane.showMessageDialog(null,"to register play the game until game overs"
					+ "\n"+"after that you can enter your name for leaderboard",
					"help",JOptionPane.QUESTION_MESSAGE);
        }
		if(e.getSource()==scoreItem) {		
			SwingUtilities.invokeLater(() -> {
				Leaderboard showLeaderboard = new Leaderboard();
		        showLeaderboard.initialize();
	        });
			
		}
		if(e.getSource()==exitItem) {
			System.exit(0);
		}
		if(e.getSource()==aboutItem) {
			JOptionPane.showMessageDialog(null,"Bartu Uyar 20210702065 bartu.uyar@std.yeditepe.edu.tr",
					"help",JOptionPane.WARNING_MESSAGE);
		}
		if(e.getSource()==contentsItem) {
			JOptionPane.showMessageDialog(null,"after score 5 level 2 unlocks"
					+ "\n"+"after score 10 level 3 unlocks",
					"help",JOptionPane.QUESTION_MESSAGE);
		}
		
	 }			
}
