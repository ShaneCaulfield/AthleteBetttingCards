import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class Wordle implements ActionListener{
	static int guess_count = 1;
	
	JLabel outcome;
	JPanel outcome_panel;
	JPanel guess_panel;
	JLabel title;
	JPanel title_panel;
	JFrame frame;
	JTextField playerguesses;
	JPanel playerguesspanel;
	JPanel playersPanel;
	ArrayList<JPanel> teams;
	JLabel firstGuessName;
	FileInputStream database =new FileInputStream(new File("C:\\Users\\Admin\\Documents\\WordlePlayerDatabase.xlsx"));
	XSSFWorkbook wb = new XSSFWorkbook(database);
	Sheet sheet = wb.getSheetAt(0);
	FormulaEvaluator formeval = wb.getCreationHelper().createFormulaEvaluator();
	String randomPlayerName;
	String randomPlayerPos;
	String randomPlayerTeam;
	String randomPlayerNation;
	String randomPlayerNum;
	String randomPlayerAge;
	String randomPlayerHand;
	String randomPlayerDiv;
	int heightofplayers = 150;
	boolean correctGuess = false;
	JButton playAgain;
	int correctPlayer;
	boolean gameAlreadyPlayed = false;
	
	JPanel[] playerNamePanels = new JPanel[8];
	JLabel[] playerNameLabels = new JLabel[8];
	
	JPanel[] playerPosPanels = new JPanel[8];
	JLabel[] playerPosLabels = new JLabel[8];
	
	JPanel[] playerTeamPanels = new JPanel[8];
	JLabel[] playerTeamLabels = new JLabel[8];
	
	JPanel[] playerNationPanels = new JPanel[8];
	JLabel[] playerNationLabels = new JLabel[8];
	
	JPanel[] playerNumPanels = new JPanel[8];
	JLabel[] playerNumLabels = new JLabel[8];
	JLabel[] playerUpDownNumLabels = new JLabel[8];
	
	JPanel[] playerAgePanels = new JPanel[8];
	JLabel[] playerAgeLabels = new JLabel[8];
	JLabel[] playerUpDownAgeLabels = new JLabel[8];
	
	JPanel[] playerHandedPanels = new JPanel[8];
	JLabel[] playerHandedLabels = new JLabel[8];
	
	JPanel revealPlayerPanel;
	JLabel revealPlayer;
	
	Wordle() throws IOException{
		frame = new JFrame("Hockey Wordle");
		randomPlayer();
		setGame();
		
		title = new JLabel("Hockey Wordle");
		title.setHorizontalTextPosition(JLabel.CENTER);
		title.setVerticalTextPosition(JLabel.TOP);
		title.setFont(new Font("Arial",Font.BOLD,30));
		
		title_panel = new JPanel();
		title_panel.setBounds(340, 0, 300, 40);
		title_panel.add(title);
		title_panel.setBackground(Color.white);
		
		playerguesses = new JTextField();
		playerguesses.setPreferredSize(new Dimension(700,50));
		playerguesses.setText("");
		playerguesses.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	boolean realGuess = false;
			    	if(correctGuess) {
			    		return;
			    	}
			    	DataFormatter formatter = new DataFormatter();
			    	for(Row row : sheet) {
			    		for(Cell cell : row) {
			    			if(formatter.formatCellValue(cell).toLowerCase().equals(playerguesses.getText().toLowerCase())){
	    						guess_count++;
	    						heightofplayers = heightofplayers + 75;
	    						realGuess = true;
	    						outcome.setText("");
	    					}
			    		}
			    	}
			    	if(!realGuess) {
			    		outcome.setText("That player is not in our database.");
			    	}
			    	if(guess_count >= 9) {
			    		outcome.setText("Sorry, No More Guesses!");
					}
			    	else {
			    		for(Row row : sheet) {
			    			boolean playerFound = false;
			    			int infoType = 0;
			    			boolean correctTeam = false;
			    			correctPlayer = 0;
			    			for(Cell cell : row) {
			    				if(playerFound || formatter.formatCellValue(cell).toLowerCase().equals(playerguesses.getText().toLowerCase())) {
			 
			    					if(infoType == 0) {
			    						playerNamePanels[guess_count-1].setBackground(Color.gray);
		    							playerNameLabels[guess_count-1].setText(formatter.formatCellValue(cell));
		    							playerNameLabels[guess_count-1].setVerticalAlignment(JLabel.CENTER);
		    							playerNameLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							if(formatter.formatCellValue(cell).equals(randomPlayerName)) {
		    								playerNamePanels[guess_count-1].setBackground(Color.green);
		    								correctPlayer++;
		    							}
		    						
		    						}
		    						else if(infoType == 1) {
		    							playerPosPanels[guess_count-1].setBackground(Color.gray);
		    							playerPosLabels[guess_count-1].setText(formatter.formatCellValue(cell));
		    							playerPosLabels[guess_count-1].setVerticalAlignment(JLabel.CENTER);
		    							playerPosLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							if(formatter.formatCellValue(cell).equals(randomPlayerPos)) {
		    								playerPosPanels[guess_count-1].setBackground(Color.green);
		    								correctPlayer++;
		    							}
		    						}
		    						else if(infoType == 2) {
		    							playerTeamPanels[guess_count-1].setBackground(Color.gray);
		    							playerTeamLabels[guess_count-1].setText(formatter.formatCellValue(cell));
		    							playerTeamLabels[guess_count-1].setVerticalTextPosition(JLabel.CENTER);
		    							playerTeamLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							if(formatter.formatCellValue(cell).equals(randomPlayerTeam)) {
		    								playerTeamPanels[guess_count-1].setBackground(Color.green);
		    								correctPlayer++;
		    								correctTeam = true;
		    							}
		    						}
		    						else if(infoType == 3) {
		    							playerNationPanels[guess_count-1].setBackground(Color.gray);
		    							playerNationLabels[guess_count-1].setText(formatter.formatCellValue(cell));
		    							playerNationLabels[guess_count-1].setVerticalTextPosition(JLabel.CENTER);
		    							playerNationLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							if(formatter.formatCellValue(cell).equals(randomPlayerNation)) {
		    								playerNationPanels[guess_count-1].setBackground(Color.green);
		    								correctPlayer++;
		    							}
		    						}
		    						else if(infoType == 4) {
		    							playerNumPanels[guess_count-1].setBackground(Color.gray);
		    							playerNumLabels[guess_count-1].setText(formatter.formatCellValue(cell));
		    							playerNumLabels[guess_count-1].setVerticalTextPosition(JLabel.CENTER);
		    							playerNumLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							if(formatter.formatCellValue(cell).equals(randomPlayerNum)) {
		    								playerNumPanels[guess_count-1].setBackground(Color.green);
		    								correctPlayer++;
		    							}
		    							
		    							else if(Math.abs(Integer.parseInt(formatter.formatCellValue(cell)) -Integer.parseInt(randomPlayerNum)) <= 15) {
		    								playerNumPanels[guess_count-1].setBackground(Color.yellow);
		    							}
		    							
		    							if(Integer.parseInt(formatter.formatCellValue(cell)) > Integer.parseInt(randomPlayerNum)){
		    								playerUpDownNumLabels[guess_count-1].setText("DOWN");
		    							}
		    							
		    							else if(Integer.parseInt(formatter.formatCellValue(cell)) < Integer.parseInt(randomPlayerNum)) {
		    								playerUpDownNumLabels[guess_count-1].setText("   UP   ");
		    								playerUpDownNumLabels[guess_count-1].setVerticalTextPosition(JLabel.CENTER);
		    								playerUpDownNumLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							}
		    							
		    						}
		    						else if(infoType == 5) {
		    							playerAgePanels[guess_count-1].setBackground(Color.gray);
		    							playerAgeLabels[guess_count-1].setText(formatter.formatCellValue(cell));
		    							playerAgeLabels[guess_count-1].setVerticalTextPosition(JLabel.CENTER);
		    							playerAgeLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							if(formatter.formatCellValue(cell).equals(randomPlayerAge)) {
		    								playerAgePanels[guess_count-1].setBackground(Color.green);
		    								correctPlayer++;
		    							}
		    							else if(Math.abs(Integer.parseInt(formatter.formatCellValue(cell)) -Integer.parseInt(randomPlayerAge)) <= 3) {
		    								playerAgePanels[guess_count-1].setBackground(Color.yellow);
		    							}
		    							if(Integer.parseInt(formatter.formatCellValue(cell)) > Integer.parseInt(randomPlayerAge)){
		    								playerUpDownAgeLabels[guess_count-1].setText("DOWN");
		    							}
		    							
		    							else if(Integer.parseInt(formatter.formatCellValue(cell)) < Integer.parseInt(randomPlayerAge)) {
		    								playerUpDownAgeLabels[guess_count-1].setText("   UP   ");
		    								playerUpDownAgeLabels[guess_count-1].setVerticalTextPosition(JLabel.CENTER);
		    								playerUpDownAgeLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							}
		    							
		    							
		    						}
		    						else if(infoType == 6) {
		    							playerHandedPanels[guess_count-1].setBackground(Color.gray);
		    							playerHandedLabels[guess_count-1].setText(formatter.formatCellValue(cell));
		    							playerHandedLabels[guess_count-1].setVerticalTextPosition(JLabel.CENTER);
		    							playerHandedLabels[guess_count-1].setHorizontalAlignment(JLabel.CENTER);
		    							if(formatter.formatCellValue(cell).equals(randomPlayerHand)) {
		    								playerHandedPanels[guess_count-1].setBackground(Color.green);
		    								correctPlayer++;
		    							}
		    						}
		    						else if(infoType == 7) {
		    							if(formatter.formatCellValue(cell).equals(randomPlayerDiv) && !correctTeam) {
		    								playerTeamPanels[guess_count-1].setBackground(Color.yellow);
		    							}
		    							if(correctPlayer == 7) {
		    	    						outcome.setText("Thats Correct!!");
		    	    						correctGuess = true;
		    	    						
		    	    					}
		    							else {
		    								break;
		    							}
		    						}
		    						playerFound = true;
		    						infoType++;
			    				}
			    				else {
			    					break;
			    				}
			    			}
			    		}
			    		
			    		if(guess_count == 8) {
			    			outcome.setText("Sorry, No More Guesses!");
			    			revealPlayer.setText("Player was: " + randomPlayerName);
			    			
			    		}
					}
			    	playerguesses.setText("");
			    }
			    
			});
		
		playerguesspanel = new JPanel();
		playerguesspanel.setBounds(110,100,750,60);
		playerguesspanel.add(playerguesses);
		playerguesspanel.setBackground(Color.white);
		frame.add(playerguesspanel);	
	
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.getContentPane().setBackground(Color.white);
		frame.setSize(1000,1000);
		frame.setVisible(true);
		frame.add(title_panel);
	
		playAgain = new JButton();
		playAgain.setBounds(425,850,100,50);
		playAgain.setText("RESTART");
		frame.add(playAgain);
		
		playAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < 8; i++) {
					playerNamePanels[i].removeAll();
					playerPosPanels[i].removeAll();
					playerTeamPanels[i].removeAll();
					playerNationPanels[i].removeAll();
					playerNumPanels[i].removeAll();
					playerAgePanels[i].removeAll();
					playerHandedPanels[i].removeAll();
					playerNamePanels[i].revalidate();
					playerPosPanels[i].revalidate();
					playerTeamPanels[i].revalidate();
					playerNationPanels[i].revalidate();
					playerNumPanels[i].revalidate();
					playerAgePanels[i].revalidate();
					playerHandedPanels[i].revalidate();
					playerNamePanels[i].repaint();
					playerPosPanels[i].repaint();
					playerTeamPanels[i].repaint();
					playerNationPanels[i].repaint();
					playerNumPanels[i].repaint();
					playerAgePanels[i].repaint();
					playerHandedPanels[i].repaint();
					playerNamePanels[i].setBackground(Color.white);
					playerPosPanels[i].setBackground(Color.white);
					playerTeamPanels[i].setBackground(Color.white);
					playerNationPanels[i].setBackground(Color.white);
					playerNumPanels[i].setBackground(Color.white);
					playerAgePanels[i].setBackground(Color.white);
					playerHandedPanels[i].setBackground(Color.white);
					outcome_panel.removeAll();
					outcome_panel.revalidate();
					outcome_panel.repaint();
					revealPlayerPanel.removeAll();
					revealPlayerPanel.revalidate();
					revealPlayerPanel.repaint();
				}
				frame.repaint();
				setGame();
				
			}
			
		});
	}
	public void setGame() {
		randomPlayer();
		guess_count = 0;
		correctGuess = false;
		heightofplayers = 150;
		correctPlayer = 0;
		playersPanel = new JPanel();
		playersPanel.setBounds(50,200,875,550);
		playersPanel.setBackground(Color.white);
		
		playerNamePanels = new JPanel[8];
		playerNameLabels = new JLabel[8];
		
		playerPosPanels = new JPanel[8];
		playerPosLabels = new JLabel[8];
		
		playerTeamPanels = new JPanel[8];
		playerTeamLabels = new JLabel[8];
		
		playerNationPanels = new JPanel[8];
		playerNationLabels = new JLabel[8];
		
		playerNumPanels = new JPanel[8];
		playerNumLabels = new JLabel[8];
		playerUpDownNumLabels = new JLabel[8];
		
		playerAgePanels = new JPanel[8];
		playerAgeLabels = new JLabel[8];
		playerUpDownAgeLabels = new JLabel[8];
		
		playerHandedPanels = new JPanel[8];
		playerHandedLabels = new JLabel[8];
		int heightofPlayerslocal = 225;
		for(int i = 0; i < 8; i++) {
			
			playerNamePanels[i] = new JPanel();
			playerNameLabels[i] = new JLabel();
			playerPosPanels[i] = new JPanel();
			playerPosLabels [i] = new JLabel();
			playerTeamPanels[i] = new JPanel();
			playerTeamLabels [i] = new JLabel();
			playerNationPanels[i] = new JPanel();
			playerNationLabels [i] = new JLabel();
			playerNumPanels[i] = new JPanel();
			playerNumLabels [i] = new JLabel();
			playerUpDownNumLabels[i] = new JLabel(); 
			playerAgePanels[i] = new JPanel();
			playerUpDownAgeLabels[i] = new JLabel(); 
			playerAgeLabels [i] = new JLabel();
			playerHandedPanels[i] = new JPanel();
			playerHandedLabels [i] = new JLabel();
			playerNamePanels[i].setLayout(new GridBagLayout());
			playerPosPanels[i].setLayout(new GridBagLayout());
			playerTeamPanels[i].setLayout(new GridBagLayout());
			playerNationPanels[i].setLayout(new GridBagLayout());
			playerHandedPanels[i].setLayout(new GridBagLayout());
			playerNamePanels[i].setBackground(Color.white);
			playerPosPanels[i].setBackground(Color.white);
			playerTeamPanels[i].setBackground(Color.white);
			playerNationPanels[i].setBackground(Color.white);
			playerNumPanels[i].setBackground(Color.white);
			playerAgePanels[i].setBackground(Color.white);
			playerHandedPanels[i].setBackground(Color.white);
			playerNamePanels[i].setBounds(130, heightofPlayerslocal, 125,50);
			playerPosPanels[i].setBounds(300,heightofPlayerslocal, 50,50);
			playerTeamPanels[i].setBounds(400, heightofPlayerslocal, 50,50);
			playerNationPanels[i].setBounds(500, heightofPlayerslocal, 50,50);
			playerNumPanels[i].setBounds(600, heightofPlayerslocal, 50,50);
			playerAgePanels[i].setBounds(700, heightofPlayerslocal, 50,50);
			playerHandedPanels[i].setBounds(800, heightofPlayerslocal, 50,50);
			playerNamePanels[i].add(playerNameLabels[i]);
			playerPosPanels[i].add(playerPosLabels[i]);
			playerTeamPanels[i].add(playerTeamLabels[i]);
			playerNationPanels[i].add(playerNationLabels[i]);
			playerNumPanels[i].add(playerNumLabels[i]);
			playerNumPanels[i].add(playerUpDownNumLabels[i]);
			playerAgePanels[i].add(playerAgeLabels[i]);
			playerAgePanels[i].add(playerUpDownAgeLabels[i]);
			playerHandedPanels[i].add(playerHandedLabels[i]);

			frame.add(playerNamePanels[i]);
			frame.add(playerPosPanels[i]);
			frame.add(playerTeamPanels[i]);
			frame.add(playerNationPanels[i]);
			frame.add(playerNumPanels[i]);
			frame.add(playerAgePanels[i]);
			frame.add(playerHandedPanels[i]);
			heightofPlayerslocal = heightofPlayerslocal + 75;			
		}
		
			
		outcome = new JLabel();
		outcome.setFont(new Font("Arial",Font.BOLD,22));
		outcome.setText("");
		
		outcome_panel = new JPanel();
		outcome_panel.setBounds(335,155,350,35);
		outcome_panel.add(outcome);
		outcome_panel.setBackground(Color.white);
		
		revealPlayer = new JLabel();
		revealPlayer.setFont(new Font("Arial",Font.BOLD,14));
		revealPlayer.setText("");
		
		revealPlayerPanel = new JPanel();
		revealPlayerPanel.setBounds(385,50,200,35);
		revealPlayerPanel.add(revealPlayer);
		revealPlayerPanel.setBackground(Color.white);
	
		frame.add(outcome_panel);
		frame.add(revealPlayerPanel);
	}
	
	public void randomPlayer() {
		int player = 0;
		int randomPlayer = (int) Math.floor(Math.random()*(760 - 1 + 1) + 1);
		int infoNum = 0;
		int breakFlag = 0;
		DataFormatter playerFormatter = new DataFormatter();
		for (Row row : sheet) {
			if (breakFlag == 1) {
				break;
			}
			if(player == randomPlayer) {
				for(Cell cell : row) {
					if(infoNum == 0) {
						randomPlayerName = playerFormatter.formatCellValue(cell);
					}
					else if(infoNum== 1) {
						randomPlayerPos = playerFormatter.formatCellValue(cell);
					}
					else if(infoNum == 2) {
						randomPlayerTeam = playerFormatter.formatCellValue(cell);
					}
					else if(infoNum == 3) {
						randomPlayerNation = playerFormatter.formatCellValue(cell);
					}
					else if(infoNum == 4) {
						randomPlayerNum =  playerFormatter.formatCellValue(cell);
					}
					else if(infoNum == 5) {
						randomPlayerAge = playerFormatter.formatCellValue(cell);
					}
					else if(infoNum == 6) {
						randomPlayerHand = playerFormatter.formatCellValue(cell);
					}
					else if(infoNum >= 7) {
						randomPlayerDiv = playerFormatter.formatCellValue(cell);
						breakFlag = 1;
						break;
					}
					infoNum++;
				}
				
			}
			player++;
		}
		guess_count = 0;
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}