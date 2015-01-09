/*
 * Marcus Vinson
 * Problem Solving: Move It Game
 * 4/12/2012
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Move_Board {
	

	private static final int DIMENSION = 10;
	private static final int CELL_DIM = 50;
	private static final int PIECES = 7;
	private static final int CORNER = -1;
	private static final int EMPTY = 0;
	private static final int TOP_PLAYER = 1;
	private static final int LEFT_PLAYER = 2;
	private static final int TOP_ZONE = 4;
	private static final int LEFT_ZONE = 8;
	private static final int PLAYER_BOUNDS = 0;
	
	
	//Pieces
	private ImageIcon top_icon = null;
	private ImageIcon left_icon = null;
	private ImageIcon blank_icon = null;
	
	private int game_board[][];
	private JButton button_board[][];
	private static JFrame window;
	private static JPanel container;
	
	private ActionListener listener = null;
	
	
	
	public Move_Board( ActionListener l ) {
		
		listener = l;
		game_board = new int[DIMENSION][DIMENSION];
		button_board = new JButton[DIMENSION][DIMENSION];
		
		container = new JPanel();
		container.setPreferredSize( new Dimension(CELL_DIM*DIMENSION+148, CELL_DIM*DIMENSION+150) );
		container.setBackground( Color.gray );
		container.setLayout( new GridLayout(DIMENSION,DIMENSION,2,2) );

		
		top_icon = createImageIcon( "images/green_pieces.png" );
		left_icon = createImageIcon( "images/red_pieces.png" );
		blank_icon = createImageIcon( "images/blank_pieces.png" );
		
		initialize_board();
		
		//get_board_buttons();
		//setPiece( TOP_PLAYER , "A4", "B4" );
		
		//printBoard();
		
	}
	
	//Creates the Image Icon by buffering in the image contained in Image.
	protected ImageIcon createImageIcon( String path ) {
		BufferedImage icon;

		try
		{
			icon = ImageIO.read( new File( path ));
			return new ImageIcon( icon );
		}
		catch( IOException e ) { System.out.println("Image icon not found"); }
		
		return new ImageIcon( "None" );
	}
	
	
	
	public ImageIcon getTopIcon() {
		return top_icon;
	}
	
	
	
	public ImageIcon getLeftIcon() {
		return left_icon;
	}
	
	
	public void resetBoard( ) {
		initialize_board();
		initialize_gui_board();
	}
	
	
	//Checks if the given player has one of its pieces at location x , y
	public boolean isPieceLocation( int player,  int x , int y ) {
		
		if( game_board[x][y] == player )
			return true;
		
		return false;
	}
	
	
	public Responses check_move( int player, int pieceX , int pieceY , int locationX , int locationY ) {
		
		//Make sure they are actually moving a piece
		if( game_board[pieceX][pieceY] == 0 )
			return Responses.NO_PEICE; 
		
		//Check that they are not moving more than one space.
		if( Math.abs( locationX - pieceX ) > 1 || Math.abs( locationY - pieceY ) > 1 )
			return Responses.INVALID;
		
		//Check that they are not moving diagonally
		if( locationX != pieceX && locationY != pieceY )
			return Responses.INVALID;
		
		
		if( player == TOP_PLAYER ){
			
			//Check they are not moving wrong piece
			if( game_board[pieceX][pieceY] == LEFT_PLAYER )
				return Responses.NOT_YOURS;
			
			//Check if player is trying to move out of bounds
			if( locationY == PLAYER_BOUNDS || locationY == DIMENSION-1) return Responses.OUT_BOUNDS;
			
			//Check if player is trying to move backwards
			if( locationX < pieceX ) return Responses.BACKWARDS;
		}
		else{
			//Check they are not moving wrong piece
			if( game_board[pieceX][pieceY] == TOP_PLAYER )
				return Responses.NOT_YOURS;
			
			if( locationX == PLAYER_BOUNDS || locationX == DIMENSION-1) return Responses.OUT_BOUNDS;
			
			if( locationY < pieceY ) return Responses.BACKWARDS;
		}
		
		//Check if they are moving into a corner
		if(  game_board[locationX][locationY] == CORNER )
			return Responses.OUT_BOUNDS;
		
		//Check if they are moving into another player
		if( game_board[locationX][locationY] != EMPTY && game_board[locationX][locationY] % 4 != 0 )
			return Responses.OCCUPIED;
		
		
		return Responses.VALID;
	}
	
	
	
	public Responses setPiece( int player , int pieceX , int pieceY , int locationX , int locationY ) {
		
		//Place move
		game_board[pieceX][pieceY] = EMPTY;
		game_board[locationX][locationY] = player;
		
		if( game_won(player) ) return Responses.WINNER;
		
		return Responses.VALID;
	}
	
	
	public void test_print( String t ) {
		System.out.println(t);
	}
	
	
	public void setActionListener( ActionListener l ) {
		listener = l;
	}
	
	
	
	public boolean game_won( int player ) {
		
		int count = 0, bound = DIMENSION-1;
		
		for( int i = 1; i < bound; i++ ) {
			if( player == TOP_PLAYER ) {
				if( game_board[bound][i] == player ) {
					count++;
					//System.out.println( "Count " + count + " player: " + player + " at " + i );
				}
			}
			else {
				if( game_board[i][bound] == player )
					count++;
			}
		}
		
		if( count == PIECES ) return true;
		
		return false;
	}
	
	
	
	public void printBoard() {
		
		for( int i = 0; i < DIMENSION; i++ ) {
			for( int j = 0; j < DIMENSION; j++ )
				System.out.printf("%2d",game_board[i][j]);
			System.out.println();
		}
	}
	
	
	private void initialize_board()
	{
		int i,j;
		
		//Set the corners
		for( i = 0; i < DIMENSION; i += DIMENSION-1){
			for( j = 0; j < DIMENSION; j += DIMENSION-1) {
				game_board[i][j] = CORNER;
			}
		}
		
		for( i = 1; i < DIMENSION; i++ ) {
			for( j = 1; j < DIMENSION; j++ ) {
				game_board[i][j] = 0;
			}
		}
		
		//Set Lefts end zone
		for( i = 1; i < DIMENSION-1; i++ ) {
			game_board[i][DIMENSION-1] = LEFT_ZONE;
		}
		
		//Set Tops end zone
		for( j = 1; j < DIMENSION-1; j++ ) {
			game_board[DIMENSION-1][j] = TOP_ZONE;
		}
		
		//Set Game Pieces
		for( i = 2; i < DIMENSION-1; i++ ) {
			game_board[i][0] = LEFT_PLAYER;
		}
		
		for( j = 2; j < DIMENSION-1; j++ ) {
			game_board[0][j] = TOP_PLAYER;
		}
	}
	
	
	private void initialize_gui_board()
	{
		int i,j;
		
		//Set the corners
		for( i = 0; i < DIMENSION; i += DIMENSION-1){
			for( j = 0; j < DIMENSION; j += DIMENSION-1) {
				button_board[i][j].setBackground(Color.LIGHT_GRAY);
				button_board[i][j].setIcon(null);
			}
		}
		
		for( i = 1; i < DIMENSION-1; i++ ) {
			for( j = 1; j < DIMENSION-1; j++ ) {
				button_board[i][j].setIcon( blank_icon );
			}
		}
		
		//Set Lefts end zone
		for( i = 1; i < DIMENSION-1; i++ ) {
			button_board[i][DIMENSION-1].setIcon( blank_icon );
		}
		
		//Set Tops end zone
		for( j = 1; j < DIMENSION-1; j++ ) {
			button_board[DIMENSION-1][j].setIcon( blank_icon );
		}
		
		//Set Game Pieces
		for( i = 2; i < DIMENSION-1; i++ ) {
			button_board[i][0].setIcon( left_icon );
		}
		
		for( j = 2; j < DIMENSION-1; j++ ) {
			button_board[0][j].setIcon( top_icon );
		}
	}
	
	
	private void fill_container() {
		
		for( int i = 0; i < DIMENSION; i++ ) {
			for( int j = 0; j < DIMENSION; j++ ) {
				container.add( button_board[i][j] );
			}
		}
	}
	
	
	public void setBoardEnabled( boolean value ) {
		
		for( int i = 0; i < DIMENSION; i++ ) {
			for( int j = 0; j < DIMENSION; j++ ) {
				button_board[i][j].setEnabled( value );
			}
		}
	}
	
	
	public void setTilePiece( int player , int x , int y ) {
		
		switch( player ) {
		
			case 0:
				button_board[x][y].setIcon( blank_icon );
				break;
			case 1:
				button_board[x][y].setIcon( top_icon );
				break;
			case 2:
				button_board[x][y].setIcon( left_icon );
				break;
			default: //nothing
				
		}
	}
	
	public JPanel get_board_buttons() {
		
		initialize_buttons();
		
		initialize_gui_board();
		
		fill_container();
		
		return container;
	}
	
	public JButton[][] get_cells() {
		return button_board;
	}
	
	private void initialize_buttons() {
		
		for( int i = 0; i < DIMENSION; i++ ) {
			for( int j = 0; j < DIMENSION; j++ ) {
				button_board[i][j] = new JButton( blank_icon );
				button_board[i][j].setBorderPainted(false);
				button_board[i][j].setBackground( Color.white );
				if( listener != null ) {
					button_board[i][j].addActionListener( listener );
				}
			}
		}
	}
	
	
	
	public static void main(String[] args ) {
		
		window = new JFrame( "Board" );
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setPreferredSize( new Dimension(CELL_DIM*DIMENSION+168, CELL_DIM*DIMENSION+150) );
		window.setResizable(true);
		
		new Move_Board( null );
		
		window.add( container );
		window.pack();
		window.setVisible(true);
		
	}
	
}
