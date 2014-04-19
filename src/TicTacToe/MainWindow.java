package TicTacToe;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainWindow extends JFrame
{
	// GUI components
	private JPanel gamePanel,
				   basePanel,
				   innerBasePanel;
	private JLabel playerXLabel,
				   playerOLabel,
				   xScoreLabel,
				   oScoreLabel,
				   turnLabel;
	private JPanel[][] gameSquare;
	private JMenu options,
				  help;
	private JMenuItem reset,
					  exit,
					  learnToPlay,
					  newGame;
	private JMenuBar menuBar;
	private JLabel[][] gameSquareLabel;
	private Font gamePieceFont;
	private int xScore,
				oScore,
				turn;
	private boolean isXTurn;
	
	// global constants
	final static int SIZE = 3,
					 GAP = 5;
	
	// ENUM status
	private enum Status {X, O, BLANK};
	private Status[][] gameSquareStatus;
	private enum Game {CONTINUE, WON, CAT};
	private Game gameStatus;
	
	public MainWindow() // constructor start
	{
		super("Tic-Tac-Toe");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(300, 400);
		
		// initializing primitive data
		oScore = xScore = 0;
		
		// initializing game square status
		gameSquareStatus = new Status[SIZE][SIZE];
		
		// initializing GUI components
		gamePanel = new JPanel();
		
		// building panels
		buildMenuBar();
		buildGamePanel();
		buildBasePanel();
		
		// chooses random player to start
		newGame();
		
		// adding panels to frame
		setJMenuBar(menuBar);
		add(gamePanel, BorderLayout.CENTER);
		add(basePanel, BorderLayout.SOUTH);
		
		setVisible(true);
	} // constructor end
	
	public static void main(String[] args) // main method start
	{
		MainWindow mw = new MainWindow();
	} // main method end

	// buildMenuBar method builds the menu bar
	private void buildMenuBar() // buildMenuBar method start
	{
		// initializing components
		options = new JMenu("Options");
		help = new JMenu("Help");
		newGame = new JMenuItem("New Game");
		reset = new JMenuItem("Reset Scores");
		exit = new JMenuItem("Exit");
		learnToPlay = new JMenuItem("How to play");
		menuBar = new JMenuBar();
		
		// component properties
		options.setMnemonic('O');
		help.setMnemonic('H');
		newGame.setMnemonic('N');
		newGame.setEnabled(false);
		newGame.setToolTipText("Finish the game you are playing");
		reset.setMnemonic('R');
		exit.setMnemonic('x');
		learnToPlay.setMnemonic('L');
		
		// adding components
		options.add(newGame);
		options.add(reset);
		options.add(exit);
		help.add(learnToPlay);
		menuBar.add(options);
		menuBar.add(help);
		
		// menu event handlers
		newGame.addActionListener(new ActionListener() // anonymous inner class
		{
			@Override
			public void actionPerformed(ActionEvent e) // actionPerformed method start
			{
				newGame();
			} // actionPerformed method end
		});
		reset.addActionListener(new ActionListener() // anonymous inner class
		{
			@Override
			public void actionPerformed(ActionEvent e) // actionPerformed method start
			{
				reset();
			} // actionPerformed method end
		});
		exit.addActionListener(new ActionListener() // anonymous inner class
		{
			@Override
			public void actionPerformed(ActionEvent e) // actionPerformed method start
			{
				System.exit(0);
			} // actionPerformed method end
		});
		learnToPlay.addActionListener(new ActionListener() // anonymous inner class
		{
			@Override
			public void actionPerformed(ActionEvent e) // actionPerformed method start
			{
				JOptionPane.showMessageDialog(gamePanel, "How the hell do you not know how to play Tic-Tac-Toe?\nYou should be ashamed of yourself!", "You're Joking Right?", JOptionPane.WARNING_MESSAGE);
			} // actionPerformed method end
		});
	} // buildMenuBar method end
	
	// buildGamePanel method builds the game panel
	private void buildGamePanel() // buildGamePanel method start
	{
		// initializing components.
		gameSquare = new JPanel[SIZE][SIZE];
		gameSquareLabel = new JLabel[SIZE][SIZE];
		gamePieceFont = new Font( "Aharoni", Font.BOLD, 70);
		for (int row = 0; row < SIZE; row++)
		{
			for (int column = 0; column < SIZE; column++)
			{
				gameSquare[row][column] = new JPanel();
				gameSquare[row][column].setLayout(new GridLayout(1, 1));
				gameSquare[row][column].setFocusable(true);
				gameSquareLabel[row][column] = new JLabel("");
				gameSquareLabel[row][column].setFont(gamePieceFont);
				gameSquareLabel[row][column].setHorizontalAlignment(JLabel.CENTER);
				gameSquareLabel[row][column].setVerticalAlignment(JLabel.CENTER);
				gameSquareLabel[row][column].setAlignmentX(CENTER_ALIGNMENT);
				gameSquareLabel[row][column].setAlignmentY(CENTER_ALIGNMENT);
			} // end for
		} // end for
		
		// component properties
		gamePanel.setLayout(new GridLayout(SIZE, SIZE, GAP, GAP));
		gamePanel.setBackground(Color.BLACK);
		
		// adding components
		for (int row = 0; row < SIZE; row++)
		{
			for (int column = 0; column < SIZE; column++)
			{
				gameSquare[row][column].add(gameSquareLabel[row][column]);
				gamePanel.add(gameSquare[row][column]);
			} // end for
		} // end for
		
		// registering handlers
		for (int row = 0; row < SIZE; row++)
		{
			for (int column = 0; column < SIZE; column++)
			{
				gameSquare[row][column].addMouseListener(new MouseListener()
				{ // anonymous inner class
					@Override
					public void mouseClicked(MouseEvent e)
					{
						for (int row = 0; row < SIZE; row++)
						{
							for (int column = 0; column < SIZE; column++)
							{
								if (e.getComponent() == gameSquare[row][column] &&
										gameStatus == Game.CONTINUE)
								{
									if (isXTurn) // if Player X turn
									{
										gameSquareLabel[row][column].setText("X");
										gameSquareStatus[row][column] = Status.X;
										turn++;
										turnLabel.setText("Your turn, Player O.");
										checkForWin();
										if (gameStatus == Game.CONTINUE)
										{
											isXTurn = (!isXTurn);
										} // end if
									} // end if
									else // if Player O turn
									{
										gameSquareLabel[row][column].setText("O");
										gameSquareStatus[row][column] = Status.O;
										turn++;
										turnLabel.setText("Your turn, Player X.");
										checkForWin();
										if (gameStatus == Game.CONTINUE)
										{
											isXTurn = (!isXTurn);
										} // end if
									} // end else
								} // end if
							} // end for
						} // end for
					}

					@Override
					public void mouseEntered(MouseEvent arg0){} // does nothing

					@Override
					public void mouseExited(MouseEvent arg0){} // does nothing

					@Override
					public void mousePressed(MouseEvent arg0){} // does nothing

					@Override
					public void mouseReleased(MouseEvent arg0){} // does nothing
				}); // end anonymous inner class
			} // end for
		} // end for
	} // buildGamePanel method end
	
	// buildBasePanel method builds the base panel
	private void buildBasePanel() // buildBasePanel method start
	{
		// initializing components
		basePanel = new JPanel();
		innerBasePanel = new JPanel();
		playerXLabel = new JLabel("Player X");
		playerOLabel = new JLabel("Player O");
		xScoreLabel = new JLabel("Score: 0");
		oScoreLabel = new JLabel("Score: 0");
		turnLabel = new JLabel("Turn");
		
		// component properties
		basePanel.setLayout(new GridLayout(2, 1));
		innerBasePanel.setLayout(new GridLayout(2, 2));
		turnLabel.setHorizontalAlignment(JLabel.CENTER);
		playerXLabel.setHorizontalAlignment(JLabel.LEFT);
		xScoreLabel.setHorizontalAlignment(JLabel.LEFT);
		playerOLabel.setHorizontalAlignment(JLabel.RIGHT);
		oScoreLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		// adding components
		innerBasePanel.add(playerXLabel);
		innerBasePanel.add(playerOLabel);
		innerBasePanel.add(xScoreLabel);
		innerBasePanel.add(oScoreLabel);
		basePanel.add(innerBasePanel);
		basePanel.add(turnLabel);
	} // buildBasePanel method end
	
	// newGame method sets the board up for a new game
	private void newGame() // newGame method start
	{
		Random ranNum = new Random();
		
		isXTurn = ranNum.nextBoolean(); // randomly chooses turn
		turn = 0;
		gameStatus = Game.CONTINUE;
		
		// resets board and statuses
		for (int row = 0; row < SIZE; row++)
		{
			for (int column = 0; column < SIZE; column++)
			{
				gameSquareLabel[row][column].setText("");
				gameSquareStatus[row][column] = Status.BLANK;
			} // end for
		} // end for
		
		if (isXTurn)
		{
			turnLabel.setText("Player X starts the game.");
		} // end if
		else
		{
			turnLabel.setText("Player O starts the game.");
		} // end else
	} // method end
	
	// checkForWin method checks the board to see if a player has won
	private void checkForWin() // checkForWin method start
	{
		if (isXTurn)
		{
			if ((gameSquareStatus[0][0] == Status.X && gameSquareStatus[0][1] == Status.X &&
				gameSquareStatus[0][2] == Status.X) || (gameSquareStatus[1][0] == Status.X &&
				gameSquareStatus[1][1] == Status.X && gameSquareStatus[1][2] == Status.X) || (
				gameSquareStatus[2][0] == Status.X && gameSquareStatus[2][1] == Status.X &&
				gameSquareStatus[2][2] == Status.X) || (gameSquareStatus[0][0] == Status.X &&
				gameSquareStatus[1][0] == Status.X && gameSquareStatus[2][0] == Status.X) || (
				gameSquareStatus[0][1] == Status.X && gameSquareStatus[1][1] == Status.X &&
				gameSquareStatus[2][1] == Status.X) || (gameSquareStatus[0][2] == Status.X &&
				gameSquareStatus[1][2] == Status.X && gameSquareStatus[2][2] == Status.X) || (
				gameSquareStatus[0][0] == Status.X && gameSquareStatus[1][1] == Status.X &&
				gameSquareStatus[2][2] == Status.X) || (gameSquareStatus[0][2] == Status.X &&
				gameSquareStatus[1][1] == Status.X && gameSquareStatus[2][0] == Status.X))
			{
				xScore++;
				xScoreLabel.setText(String.format("Score %d", xScore));
				JOptionPane.showMessageDialog(gamePanel, "Player X Wins!", "WIN!!", JOptionPane.INFORMATION_MESSAGE);
				gameStatus = Game.WON;
			} // end if
			else if (turn == 9)
			{
				JOptionPane.showMessageDialog(gamePanel, "No winners this game.", "No Winner", JOptionPane.INFORMATION_MESSAGE);
				gameStatus = Game.CAT;
			} // end else if
		} // end if
		else
		{
			if ((gameSquareStatus[0][0] == Status.O && gameSquareStatus[0][1] == Status.O &&
					gameSquareStatus[0][2] == Status.O) || (gameSquareStatus[1][0] == Status.O &&
					gameSquareStatus[1][1] == Status.O && gameSquareStatus[1][2] == Status.O) || (
					gameSquareStatus[2][0] == Status.O && gameSquareStatus[2][1] == Status.O &&
					gameSquareStatus[2][2] == Status.O) || (gameSquareStatus[0][0] == Status.O &&
					gameSquareStatus[1][0] == Status.O && gameSquareStatus[2][0] == Status.O) || (
					gameSquareStatus[0][1] == Status.O && gameSquareStatus[1][1] == Status.O &&
					gameSquareStatus[2][1] == Status.O) || (gameSquareStatus[0][2] == Status.O &&
					gameSquareStatus[1][2] == Status.O && gameSquareStatus[2][2] == Status.O) || (
					gameSquareStatus[0][0] == Status.O && gameSquareStatus[1][1] == Status.O &&
					gameSquareStatus[2][2] == Status.O) || (gameSquareStatus[0][2] == Status.O &&
					gameSquareStatus[1][1] == Status.O && gameSquareStatus[2][0] == Status.O))
			{
				oScore++;
				oScoreLabel.setText(String.format("Score %d", oScore));
				JOptionPane.showMessageDialog(gamePanel, "Player O Wins!", "WIN!!", JOptionPane.INFORMATION_MESSAGE);
				gameStatus = Game.WON;
			} // end if
			else if (turn == 9)
			{
				JOptionPane.showMessageDialog(gamePanel, "No winners this game.", "No Winner", JOptionPane.INFORMATION_MESSAGE);
				gameStatus = Game.CAT;
			} // end else if
		} // end else
		
		if (gameStatus != Game.CONTINUE)
		{
			newGame.setEnabled(true);
			newGame.setToolTipText("Start a new game");
		} // end if
	} // checkForWin method end
	
	// reset method resets scores and starts new game
	private void reset() // reset method start
	{
		newGame();
		oScore = 0;
		xScore = 0;
		oScoreLabel.setText("Score 0");
		xScoreLabel.setText("Score 0");
	} // reset method end
}
