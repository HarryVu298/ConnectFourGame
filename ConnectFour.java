package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ConnectFour {
	private JFrame frame;
	
	private Cell[][] cells;
	private boolean inProgress = true;
	private int player = 1;
	private int moves = 0;
	
	public ConnectFour() {
		frame = new JFrame("Connect Four!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel welcome = new JLabel("WELCOME TO CONNECT FOUR!", JLabel.CENTER);
		frame.add(welcome, BorderLayout.SOUTH);
		
		setupButtons();
		setupCells();
		
		frame.pack();
		frame.setVisible(true);
	}
	
	
	
	
	
	private void setupCells() {
		cells = new Cell[6][7];
		JPanel cPanel = new JPanel(new GridLayout(0, 7));
		// Create 42 Cell objects and add them to the panel
		for (int row = 0; row <cells.length; row++) {
			for (int col = 0; col < cells[0].length; col++) {
				Cell c = new Cell();
				cells[row][col] = c; // add to array
				cPanel.add(c); // add to panel
				
			}
		}
		
	
		// add panel to frame
		frame.add(cPanel, BorderLayout.CENTER);
		
		
	}
	
	
	
	// For a given column, returns the index where
	  // the next checker should go (the highest index
	  // where the player is 0), or returns -1 if the 
	  // column is full.
	  int nextSpot(int col) {
	    int spot = cells.length - 1;

	    while (spot >= 0) {
	      if (cells[spot][col].getPlayer() == 0) {
	        return spot;
	      }
	      spot--;
	    }

	    return -1;
	  }
	
	
	void makeMove(int col) {
	    // Exit method if game is in progres.
	    if (!inProgress) {
	      System.out.println("GAME IS OVER!");
	      return;
	    }

	    int row = nextSpot(col);

	    // Exit if column is full.
	    if (row == -1) {
	      System.out.printf("column %d is full%n", col);
	      return;
	    }
	    
	    // Otherwise, actually make the move
	    updateCell(row, col, player);
	    System.out.printf("player %d at %d %d%n", player, row, col);
	    player = 3 - player;
	    moves++;

	    
	    // This line redraws all the graphics.
	    frame.repaint(); // This causes all the need paintComponent() methods to be called
	    checkWin(row, col);
	  }
	
	
	// Places the specified player's piece at the specified row and column
	  void updateCell(int row, int col, int player) {
	    cells[row][col].setPlayer(player);
	  }
	
	
	
	private void setupButtons() {
		// Add this listener to each button
		ButtonListener btnListen = new ButtonListener();
		
		
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		for (int i = 0; i < 7; i++) {
			JButton tempButton = new JButton (i + "");
			tempButton.addActionListener(btnListen);
			
			buttonPanel.add(tempButton);
			buttonPanel.setBackground(Color.cyan);
		}
		
		frame.add(buttonPanel, BorderLayout.NORTH);
	}
	
	
	// Inner class that handles listening to buttons:
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			makeMove(Integer.parseInt(e.getActionCommand()));
			
		}
		
	}
	
	// ********************* BEGIN WIN CHECKING
	  void checkWin(int row, int col) {
	    if (inProgress) rowWin(row, col);
	    if (inProgress) colWin(row, col);
	    if (inProgress) nwseWin(row, col);
	    if (inProgress) swneWin(row, col);  

	    if (!inProgress) {
	      System.out.printf("PLAYER %d WINS!!%n", 2 - moves % 2);
	    }

	    if (inProgress && moves == getRows() * getCols()) {
	      System.out.println("TIE GAME");
	      inProgress = false;
	    }
	  }

	  // Check for a diagonal win from northwest to southeast
	  void nwseWin(int row, int col) {
	    if (southeastWall(row, col) - northwestWall(row, col) + 1 >= 4) {
	      int firstCol = northwestWall(row, col);
	      int firstRow = row - (col - firstCol);
	      for (int i = 0; i < 4; i++) {
	        cells[firstRow + i][firstCol + i].setWinning();
	      }
	      inProgress = false;
	    }
	  }

	  // Check for a diagonal win from southwest to northeast
	  void swneWin(int row, int col) {
	    if (northeastWall(row, col) - southwestWall(row, col) + 1 >= 4) {
	      int firstCol = southwestWall(row, col);
	      int firstRow = row + (col - firstCol);
	      for (int i = 0; i < 4; i++) {
	        cells[firstRow - i][firstCol + i].setWinning();
	      }
	      //display();
	      inProgress = false;
	    }
	  }

	  // Check for a win in the specified column
	  void colWin(int row, int col) {
	    if (southWall(row, col) - northWall(row, col) + 1 >= 4) {
	      int firstRow = northWall(row, col);
	      for (int i = 0; i < 4; i++) {
	        cells[firstRow + i][col].setWinning();
	      }
	      //display();
	      inProgress = false;
	    }
	  }

	  // Check for a win in the specified row
	  void rowWin(int row, int col) {
	    if (eastWall(row, col) - westWall(row, col) + 1 >= 4) {
	      int firstCol = westWall(row, col);
	      for (int i = 0; i < 4; i++) {
	        cells[row][firstCol + i].setWinning();
	      }
	    //display();
	      inProgress = false;
	    }
	  }


	  // probes left of the given location looking
	  // for colors that match the current cell.
	  // Stops when it finds a cell that doesn't match.
	  // Returns the leftmost column (which could be
	  // the given column if the next cell to the left
	  // is the wrong color).
	  int westWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (col > 0 && cells[row][col - 1].getPlayer() == pl) {
	      col--;
	    }

	    return col;
	  }

	  int eastWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (col <  getCols() - 1 && cells[row][col + 1].getPlayer() == pl) {
	      col++;
	    }
	    return col;
	  }

	  int southWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (row < getRows() - 1 && cells[row + 1][col].getPlayer() == pl) {
	      row++;
	    }
	    return row;
	  } 

	  // returns northmost wall as a row
	  int northWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (row > 0 && cells[row - 1][col].getPlayer() == pl) {
	      row--;
	    }
	    return row;
	  } 

	  // returns northwest most wall as a COLUMN
	  int northwestWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (row > 0 && col > 0 && cells[row - 1][col - 1].getPlayer() == pl) {
	      row--;
	      col--;
	    }
	    return col;
	  } 

	  // returns southeast most wall as a COLUMN
	  int southeastWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (row < getRows() - 1 && col < getCols() - 1 && cells[row + 1][col + 1].getPlayer() == pl) {
	      row++;
	      col++;
	    }
	    return col;
	  }

	  // returns northeast most wall as a COLUMN
	  int northeastWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (row > 0 && col < getCols() - 1 && cells[row - 1][col + 1].getPlayer() == pl) {
	      row--;
	      col++;
	    }
	    return col;
	  } 

	  // returns southwest most wall as a COLUMN
	  int southwestWall(int row, int col) {
	    int pl = cells[row][col].getPlayer();

	    while (row < getRows() - 1 && col > 0 && cells[row + 1][col - 1].getPlayer() == pl) {
	      row++;
	      col--;
	    }
	    return col;
	  }
	  // ********************** END WIN CHECKING
	
	  
	// Return the number of rows in the game
	  int getRows() {
	    return cells.length;
	  }

	  // Return the number of columns in the game
	  int getCols() {
	    return cells[0].length;
	  }
}
