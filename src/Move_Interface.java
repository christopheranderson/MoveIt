/*
 * Marcus Vinson
 * Problem Solving: Move It Game
 * 4/12/2012
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class Move_Interface implements ActionListener,Runnable  {
	
	public JFrame window;
	public JPanel game_panel;
	private JPanel board_panel;
	private JPanel top_label_panel;
	private JPanel left_label_panel;
	private JPanel menu_panel;
	private JPanel start_panel;
	private JPanel in_game_panel;
	private JPanel player_move_panel;
	private JPanel ai_move_panel;
	
	
	private JButton start_game;
	private JButton end_game;
	private JRadioButton blue_team_selector;
	private JRadioButton red_team_selector;
	private JCheckBox turn_selector;
	private JTextField player_piece;
	private JTextField player_location;
	private JTextField ai_piece;
	private JTextField ai_location;
	
	private JButton confirm;
	
	//Thread communication data variables
	private String text;
	private volatile boolean waiting = true;
	
	private Move_Board game_board;
	private JButton[][] game_board_cells;
	
	private String letters = "ABCDEFGHIJ";
	
	//In Game data
	private boolean PIECE_SELECTED = false;
	
	private int Moving_X;
	private int Moving_Y;
	
	private int TOP_PLAYER = 1;
	private int LEFT_PLAYER = 2;
	private int BLANK = 0;
	
	private Color Player_Color; //Default player color is Top color Green
	private Color O_COLOR = Color.red;
	private Color U_COLOR = Color.green;
	
	private int PLAYER = 0;
	private int OTHER_PLAYER = 0;
	
	private boolean OpponentTurn = false;
	
	
	
	public void start() {
		
		window = new JFrame( "Move It!!" );
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setPreferredSize( new Dimension( 950 , 725 ) );
		window.setResizable( false );
		window.setLayout( new BorderLayout() );
		
		//This value changes based on the selection of the User
		PLAYER = TOP_PLAYER;
		OTHER_PLAYER = LEFT_PLAYER;
		Player_Color = U_COLOR;
		
		
		
		
		//Set up the Game Board Pane
		board_panel = new JPanel();
		board_panel.setPreferredSize( new Dimension( 750 , 500 ) );
		board_panel.setLayout( new BorderLayout() );
		board_panel.setOpaque( true );
		
		
		//Set game_board to new one by default in case it is null
		if( game_board == null ) game_board = new Move_Board(this);
		game_board.setActionListener( this );

		
		//Set up the Menu Pane
		menu_panel = new JPanel( new FlowLayout() );
		menu_panel.setPreferredSize( new Dimension( 200 , 200 ));
		menu_panel.setBackground( Color.white );
		menu_panel.setBorder( BorderFactory.createTitledBorder( "Move It!!"));
		menu_panel.setOpaque( true );
		
		//Set up the Start Panel Components
		start_panel = new JPanel( new FlowLayout() );
		start_panel.setPreferredSize( new Dimension( 180 , 180 ) );
		start_panel.setBackground( Color.white );
		
		blue_team_selector = new JRadioButton( "Green", null, true );
		blue_team_selector.setBounds( 0, 0 , 20 , 50 );
		blue_team_selector.setBackground(Color.white);
		red_team_selector = new JRadioButton( "Red  ", null , false );
		red_team_selector.setBounds( 0, 0 , 20 , 50 );
		red_team_selector.setBackground( Color.white );
		
		turn_selector = new JCheckBox( "You go first." , true );
		turn_selector.setBackground( Color.white );
		start_game = new JButton( createImageIcon( "images/start.png" ) );
		start_game.setBorderPainted( false );
		start_game.setBackground( Color.white );
		
		
		
		//Set up the In game components.
		in_game_panel = new JPanel( new FlowLayout() );
		in_game_panel.setPreferredSize( new Dimension( 180 , 500 ));
		in_game_panel.setBackground( Color.white );
		
		player_move_panel = new JPanel( new GridLayout( 2 , 2 , 2 , 2 ));
		player_move_panel.setBorder( BorderFactory.createTitledBorder("Your move"));
		player_move_panel.setPreferredSize( new Dimension( 180 , 100 ));
		player_move_panel.setBackground( Color.white );
		
		ai_move_panel = new JPanel( new GridLayout( 2 , 2 , 2 , 2 ));
		ai_move_panel.setBorder( BorderFactory.createTitledBorder("AI move"));
		ai_move_panel.setPreferredSize( new Dimension( 180 , 100 ));
		ai_move_panel.setBackground( Color.white );
		
		JLabel piece = new JLabel("Piece:");
		piece.setBackground( Color.white );
		JLabel location = new JLabel( "To location: ");
		location.setBackground( Color.white );
		player_piece = new JTextField();
		player_piece.setEditable( false );
		player_location = new JTextField();
		player_location.setEditable( false );
		
		JLabel piece2 = new JLabel("Piece:");
		piece.setBackground( Color.white );
		JLabel location2 = new JLabel( "To location: ");
		location.setBackground( Color.white );
		ai_piece = new JTextField();
		ai_piece.setEditable( false );
		ai_location = new JTextField();
		ai_location.setEditable( false );
		
		player_move_panel.add( piece );
		player_move_panel.add( player_piece );
		player_move_panel.add( location );
		player_move_panel.add( player_location );
		
		ai_move_panel.add( piece2 );
		ai_move_panel.add( ai_piece );
		ai_move_panel.add( location2 );
		ai_move_panel.add( ai_location );
		
		
		end_game = new JButton( createImageIcon("images/end.png") );
		end_game.setBackground( Color.white );
		end_game.setBorderPainted(false);
		confirm = new JButton( createImageIcon("images/confirm.png") );
		confirm.setBackground( Color.white );
		confirm.setBorderPainted(false);
		window.getRootPane().setDefaultButton( confirm );
		
		//Set all the components to this listener.
		start_game.addActionListener(this);
		turn_selector.addActionListener( this );
		end_game.addActionListener( this );
		blue_team_selector.addActionListener(this);
		red_team_selector.addActionListener(this);
		confirm.addActionListener( this );
		
		//Add them all to the window
		start_panel.add( new JLabel( game_board.getTopIcon() ) );
		start_panel.add( new JLabel( game_board.getLeftIcon() ) );
		start_panel.add( blue_team_selector);
		start_panel.add( red_team_selector);
		start_panel.add( turn_selector );
		start_panel.add( start_game );
		
		//Make in game components
		in_game_panel.add( player_move_panel );
		in_game_panel.add( ai_move_panel );
		in_game_panel.add( confirm );
		in_game_panel.add( end_game );
		in_game_panel.setVisible( false );
		
		menu_panel.add( start_panel );
		menu_panel.add( in_game_panel );
		
		//Get the game board buttons container and labels
		game_panel = game_board.get_board_buttons();
		
		//Get all coordinate labels
		left_label_panel = new JPanel( new GridLayout( 10 , 1 , 2 ,2 ) );
		left_label_panel.setPreferredSize( new Dimension( 20 , 200 ) );
		for( int i = 0; i < 10; i++ ) {
			left_label_panel.add( new JLabel( "    " + letters.charAt(i)));
		}
		
		top_label_panel = new JPanel( new GridLayout( 1 , 11 , 2, 2));
		top_label_panel.setPreferredSize( new Dimension( 100 , 20 ));
		top_label_panel.add( new JLabel());
		for( int i = 1; i <= 10; i++ ) {
			top_label_panel.add( new JLabel( "" + i));
		}
		
		JPanel bottum_label_panel = new JPanel( new GridLayout( 1 , 11 , 2, 2 ));
		bottum_label_panel.setPreferredSize( new Dimension( 100 , 20 ));
		bottum_label_panel.add( new JLabel() );
		for( int i = 1; i <= 10; i++ ) {
			bottum_label_panel.add( new JLabel( "" + i ) );
		}
		
		JPanel right_label_panel = new JPanel( new GridLayout( 10 , 1 , 2 ,2 ) );
		right_label_panel.setPreferredSize( new Dimension( 20 , 200 ) );
		for( int i = 0; i < 10; i++ ) {
			right_label_panel.add( new JLabel( letters.charAt(i) + ""));
		}
		
		
		//Add all the components together and format there layouts
		JPanel holder = new JPanel( new FlowLayout() );
		holder.add( game_panel );
		board_panel.add( holder , BorderLayout.CENTER );
		board_panel.add( left_label_panel , BorderLayout.WEST );
		board_panel.add( top_label_panel , BorderLayout.NORTH );
		board_panel.add( bottum_label_panel , BorderLayout.SOUTH );
		board_panel.add( right_label_panel , BorderLayout.EAST );
		
		game_board_cells = game_board.get_cells();
		game_board.setBoardEnabled( false );
		
		window.add( board_panel , BorderLayout.CENTER );
		window.add( menu_panel , BorderLayout.EAST );
		window.pack();
		
		text = "";
		
	}
	
	
	public void run() {
		start_interface();
	}
	
	
	public void setGameBoard( Move_Board gameb ) {
		game_board = gameb;
	}
	
	
	//Creates the Image Icon by buffering in the image contained in Image.
	private ImageIcon createImageIcon( String path ) {
		BufferedImage icon;

		try
		{
			icon = ImageIO.read( new File( path ));
			return new ImageIcon( icon );
		}
		catch( IOException e ) { System.out.println("Image icon not found"); }
		
		return new ImageIcon( "None" );
	}
	
	
	//Used by Main to wait for input on this Interface
	public synchronized String get_input() throws InterruptedException{
		
		//System.out.println( "Data before: " + text );
		
		String input;
		
		if( text.length() == 0 )
			wait();
		
		input = text;
		
		text = "";
		
		notify();
		
		//System.out.println( "Data after: " + text );
		
		return input;
	}

	
	
	//Used by this Interface to send game data to main
	private synchronized void set_data( String input ) {
		
		text = input;
		
		//System.out.println( "Set data: " + text );
		
		notify();
	}
	
	public synchronized void notify_gui() {
		waiting = false;
		notify();
		System.out.println( "notified");
	}
	
	
	//Make this thread wait for 100 millis
	private synchronized void wait_gui() {
		System.out.println("Waiting");
		while( waiting ) {
			try {
				wait();
			} catch (InterruptedException e ) {};
		}
		System.out.println("Done waiting");
		waiting = true;
	}
	
	public void start_interface() {
		window.setVisible(true);
	}
	
	
	public void startOpponentTurn() {
		OpponentTurn = true;
	}
	
	public void setOpponentTurn( int pieceX , int pieceY , int LocX , int LocY ) {
		
			
			game_board_cells[pieceX][pieceY].setBackground( O_COLOR );
			game_board_cells[LocX][LocY].setBackground( O_COLOR );
			ai_piece.setText( "      " + letters.charAt( pieceX ) + "      " + (pieceY+1) );
			ai_location.setText( "      " + letters.charAt( LocX ) + "      " + (LocY+1) );
	}
	
	public void finishOpponentTurn( int player, int pieceX , int pieceY , int LocX , int LocY ) {
		
			game_board_cells[pieceX][pieceY].setBackground( Color.white );
			game_board_cells[LocX][LocY].setBackground( Color.white );
			game_board.setTilePiece( BLANK , pieceX, pieceY );
			game_board.setTilePiece(player, LocX, LocY);
	}
	
	
	private void gameWon( int player ) {
		
		if( window == null ) return;
		
		if( player == TOP_PLAYER )
			JOptionPane.showMessageDialog( null , 
									   	   "GREEN has won!!", 
									   	   "Game Over", 
									   	   JOptionPane.INFORMATION_MESSAGE, 
									   	   game_board.getTopIcon() );
		else
			JOptionPane.showMessageDialog( null , 
		   		   	   					   "RED has won!!", 
		   		   	   					   "Game Over", 
		   		   	   					   JOptionPane.INFORMATION_MESSAGE, 
		   		   	   					   game_board.getLeftIcon() );
		
		end_game();
	}
	
	
	
	//End the game and return the game board to its original state
	//Then reset the user' menu to the start game menu
	private void end_game() {
		if( PIECE_SELECTED ) {
			set_data("D");
			game_board_cells[Moving_X][Moving_Y].setBackground( Color.white );
			PIECE_SELECTED = false;
		}
		
		OpponentTurn = false;
		
		set_data("!");
		game_board.resetBoard();
		game_board.setBoardEnabled( false );
		
		in_game_panel.setVisible( false );
		start_panel.setVisible( true );
	}
	
	public static void main(String[] args ) {
		
		new Move_Interface();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		Responses response;
		String start_game_data;
		
		if( event.getSource() == confirm ) {
			if( OpponentTurn ) set_data( "C" );
			
			wait_gui();
			
			OpponentTurn = false;
			
			if( game_board.game_won( OTHER_PLAYER ) ) gameWon( OTHER_PLAYER );
		}
		
		else if( OpponentTurn ) {
			JOptionPane.showMessageDialog( null , "You must confirm the Opponent's turn.", "Opponent's Turn", JOptionPane.WARNING_MESSAGE);
		}
		
		else if( event.getSource() == start_game ) {
			
				set_data( "S" ); //Tell Main to Start a game.
				game_board.setBoardEnabled( true );
				
				
				//Get which color the user wants
				if( blue_team_selector.isSelected() ) {
					start_game_data = TOP_PLAYER + "";
				}
				else {
					start_game_data = LEFT_PLAYER + "";
				}
				
				
				//Get if the user wants to go first
				if( turn_selector.isSelected() )
					start_game_data += 1;
				else
					start_game_data += 2;
				
				
				set_data( start_game_data );
				
				start_panel.setVisible( false );
				in_game_panel.setVisible( true );
					
		}	
		else if( event.getSource() == end_game ){
				
				//If A game is ended while a piece is selected, deselect and exit.
				end_game();
		}
		else if( event.getSource() == red_team_selector ) {
			PLAYER = LEFT_PLAYER;
			OTHER_PLAYER = TOP_PLAYER;
			blue_team_selector.setSelected( false );
		}
		else if( event.getSource() == blue_team_selector ) {
			PLAYER = TOP_PLAYER;
			OTHER_PLAYER = LEFT_PLAYER;
			red_team_selector.setSelected( false );
		}
		
		else {
			for( int i = 0; i < 10; i++ ) {
				for( int j = 0; j < 10; j++ ) {
					
					//If a player clicks a cell on the board
					if( event.getSource() == game_board_cells[i][j] ) {
						
						//If the player has not already selected a piece
						if( !PIECE_SELECTED ) {
							
							//Make sure they are selecting their piece
							if( game_board.isPieceLocation( PLAYER , i , j ) ) {
								
								Moving_X = i;
								Moving_Y = j;
								player_piece.setText( "      " + letters.charAt(i) + "      " + (j+1) );
								player_location.setText("");
								game_board_cells[i][j].setBackground( Player_Color );
								set_data( i + "" + j );
								PIECE_SELECTED = true;
							}
						}
						//Check that the move is valid
						else {
							
							if( ( response = game_board.check_move( PLAYER , Moving_X, Moving_Y, i, j ) ) == Responses.VALID ) {
								game_board_cells[ Moving_X ][ Moving_Y ].setBackground( Color.white );
								game_board.setTilePiece( BLANK , Moving_X , Moving_Y );
								game_board.setTilePiece( PLAYER , i , j );
								player_location.setText( "      " + letters.charAt(i) + "      " + (j+1) );
								set_data( i + "" + j );
								PIECE_SELECTED = false;
								
								//Wait for main to notify that setting the piece has happened.
								wait_gui();
								
								//Check if the player's move is a winning move
								if( game_board.game_won( PLAYER) ) gameWon( PLAYER );
							}
							else{
								//Check if they are deselecting a piece
								if( Moving_X == i && Moving_Y == j ) {
									game_board_cells[ Moving_X ][ Moving_Y ].setBackground( Color.white );
									PIECE_SELECTED = false;
									set_data( "D" ); //Send message to deselect piece
								}
								else 
									JOptionPane.showMessageDialog( null , "Invalid Move:  " + response, "Invalid Move", JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				}
			}
			
		}
			
	}
}
