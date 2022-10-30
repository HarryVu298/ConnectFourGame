package game;

import java.awt.Color;
import java.awt.*;
import javax.swing.*;


public class Cell extends JPanel {

	private static Color[] color = {
			Color.GRAY,
			Color.RED,
			Color.BLACK,
	};
	private int player;
	private boolean winning;
	
	public Cell() {
		this.player = 0;
		this.setBackground(Color.yellow);
		this.setPreferredSize(new Dimension(100, 100));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	
	// Sets this cell's player.
	  void setPlayer(int player) {
	    this.player = player;
	  }
	  
	  
	  int getPlayer() {
		  
		  return player;
	  }
	  
	  void setWinning() {
		    this.winning = true;
		 }
	
	
	// This is how we do graphics:
	// We want each panel to have a circle drawn on it.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color[player]);
		g.fillOval(3, 3, 95, 95);
	}
}
