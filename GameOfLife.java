import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

import java.lang.System;
import java.util.Random;

class GamePanel extends JPanel {

	private static final int ROWS = 50;
	private static final int COLUMNS = 50;
	
	private boolean[][] cells;
	
	public GamePanel(int width, int height) {
		cells = GamePanel.generateRandomBoard(GamePanel.ROWS, GamePanel.COLUMNS);
		
		ActionListener animate = new ActionListener() {
           public void actionPerformed(ActionEvent ae) { update(); }
       	};
       	Timer timer = new Timer(100, animate);
       	timer.start();
	}

	private void update() {
		repaint(); //Calls paintComponent
		calculateNextStep();
	}
	
	//Draws the board
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		
		int spacing = 4;
		int cellWidth = ((int)(this.getWidth() / GamePanel.ROWS)) - spacing;
		int cellHeight = ((int)(this.getHeight() / GamePanel.COLUMNS)) - spacing;
		
		for(int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				//if a cell is set to true, set the color to blue so it draws "filled"
				if(cells[row][col]) g.setColor(Color.blue);
				else g.setColor(Color.white);

				//draw the rectangle
				int originX = (int)((spacing + cellWidth) * row) + (spacing/2);
				int originY = (int)((spacing + cellHeight) * col) + (spacing/2);
				g.fillRect(originX, originY, cellWidth, cellHeight);
			}
		}
	}

	//Applies the rules of the game for all cells and assigns the new board
	private void calculateNextStep() {
		boolean[][] newCells = new boolean[GamePanel.ROWS][GamePanel.COLUMNS];

		for(int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				
				int cellNeighbors = numberOfNeighbors(row, col);

				if (cells[row][col]) { //If we're dealing with a living cell
					if (cellNeighbors < 2 || cellNeighbors > 3) newCells[row][col] = false;
					else newCells[row][col] = true;
				} else { //Dealing with a dead cell
					if (cellNeighbors == 3) newCells[row][col] = true;
					else newCells[row][col] = false;
				}
			}
		}

		cells = newCells;
	}

	//Returns the number of neighbors surrounding a given cell in the cells[][] array
	private int numberOfNeighbors(int cellRow, int cellCol) {
		int count = 0;
		int[] blocksToCheck = {-1, 0, 1};

		for (int row = 0; row < blocksToCheck.length; row++) {
			for (int col = 0; col < blocksToCheck.length; col++) {
				try {
					//Increment counter if there is a cell in any space other than the current cells space (1, 1)
					int xOffset = blocksToCheck[row];
					int yOffset = blocksToCheck[col];
					if (cells[cellRow + xOffset][cellCol + yOffset] && !(row == 1 && col == 1)) count++;
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		
		return count;
	}

	//Generates a random board with each cell having ~50% chance of bieng alive/dead
	private static boolean[][] generateRandomBoard(int width, int height) {
		boolean[][] board = new boolean[width][width];
		Random rand = new Random();
		
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				board[row][col] = (rand.nextInt(2) == 1);
			}
		}
		
		return board;
	}

}

//Create frame to show panel
class GameOfLife extends JFrame {
  public static void main(String[] args) {
	
	Dimension windowSize = new Dimension(800, 800);
	
    //Sets up the frame
    JFrame frame = new JFrame("Conway's Game Of Life");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(windowSize);
	frame.setLocationRelativeTo(null);
	
    GamePanel panel = new GamePanel(windowSize.width, windowSize.height);
    frame.add(panel);
    frame.setVisible(true);
  }
}