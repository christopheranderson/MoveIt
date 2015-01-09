
/*
 * Move It Game
 * 
 * by: Marcus Vinson
 * Problem solving
 * 4/12/12
 */



public class Move_Main {
	
	
	private Move_Board game;
	
	//Choices
	private static final int TOP = 1;
	private static final int LEFT = 2;
	private static final char START_NEW_GAME = 'S';
	private static final char EXIT = '!';
	private static final char DESELECT = 'D';
	
	
	private int User_Side;
	private int Other_Side;
	private int turn;
	private Move_Interface gui;
	private Game AI;
	
	public Move_Main() {
		
		game = new Move_Board(null);
		
		gui = new Move_Interface();
		gui.setGameBoard( game );
		
		gui.start();
		gui.run();
		
		String choice = "";
		
		System.out.println( "Welcome to Move It!" );
		System.out.println( "-------------------" );
		
				
		do
		{
			System.out.println( "\nMenu:\n");
			System.out.println( "   1. Start New Game = S");
			System.out.println( "   2. Exit = 0");
			System.out.print( "\nSelection: ");
			
			choice = get_player_input();
			
			System.out.print(choice + "\n");
			
			
			//Perform choice
			switch( choice.charAt(0) ) {
				case START_NEW_GAME:
					configure_game();
					start_game();
					AI = null;
					break;
				case EXIT:
					break;
				default:
					System.out.println("What?");
			}
			
		}while( choice.charAt(0) != EXIT );
		
		System.out.println("Goodbye.");
		
	}
	
	
	private void configure_game() {
		
		String player;
		
		player = get_player_input();
		
		User_Side = player.charAt(0) - '0';
		
		if( User_Side == TOP ) Other_Side = LEFT;
		else Other_Side = TOP;
		
		turn = player.charAt(1) - '0';
		
		System.out.println( "Player: " + User_Side + " Takes turn: " + turn );
		System.out.println("Game configured.");
	}
	
	
	private String get_player_input() {
		
		String input = "";
		
		try
		{
			input = gui.get_input();
		}
		catch(InterruptedException e ) {};
		
		return input;
	}
	
	
	
	private void start_game() {
		
		String  piece,location;
		Responses response;
		Move move;
		
		AI = new Game( );
		
		System.out.println("Start game:\n");
		
		while( true ) {
		
		switch( turn ) {
			case 1: //User's Turn
				
				piece = get_player_input();
				
				if( piece.charAt(0) == EXIT ) return;
				if( piece.charAt(0) == DESELECT ) continue;
				
				System.out.println("Move piece: " + piece );
				
				location = get_player_input();
				
				if( location.charAt(0) == EXIT ) return;
				if( location.charAt(0) == DESELECT ) continue;
				
				System.out.println("To location: " + location + "\n");
				
			
				//Move the Piece
				response = game.setPiece( User_Side , piece.charAt(0)-'0', 
						   			 piece.charAt(1)-'0', 
						   			 location.charAt(0)-'0', 
						   			 location.charAt(1)-'0' );
			
				gui.notify_gui();
				
				System.out.println( response );
				
				
				if( response == Responses.WINNER ) {
					game.printBoard();
					System.out.println( get_player_input() );  //Exit if this player has won.
					System.out.println("End Game");
					return;
				}
				
				move = new Move( piece.charAt(0)-'0', 
								 piece.charAt(1)-'0', 
								 location.charAt(0)-'0', 
								 location.charAt(1)-'0' );
				
				
				//The move class always returns a move with coordinates that orient
				//the user as top player and AI as left.  If the User is on the left
				//the move coordinates must be reversed to account for this orientation.
				if( User_Side == LEFT )
					move.reverse();
				
				
				System.out.println(  AI.douserMove( move ).printMove() );
				
				game.printBoard();
				
				turn = 2;  //Change to next turn
				
				System.out.println();
				break;
			
			case 2: //  Opponent's Turn
				
				gui.startOpponentTurn();
				
				move = AI.docompmove();
				
				if( Other_Side == TOP )
					move.reverse();
				
				piece = move.or + "" + move.oc;
				location = move.nr + "" + move.nc;
				
				//Set the Opponents piece
				response = game.setPiece( Other_Side , piece.charAt(0)-'0', 
			   			 piece.charAt(1)-'0', 
			   			 location.charAt(0)-'0', 
			   			 location.charAt(1)-'0' );
				
				
				
				gui.setOpponentTurn(    piece.charAt(0) - '0' ,
										piece.charAt(1) - '0' ,
										location.charAt(0) - '0' ,
										location.charAt(1) - '0');
				
				System.out.println( response );
				
				
				//This just makes Main wait until the user confirms opponents move
				get_player_input();
				
				gui.finishOpponentTurn( Other_Side , piece.charAt(0) - '0' ,
						piece.charAt(1) - '0' ,
						location.charAt(0) - '0' ,
						location.charAt(1) - '0');
				
				
				gui.notify_gui();
				//game.printBoard();
				
				
				
				if( response == Responses.WINNER ) {
					System.out.println( get_player_input() );
					System.out.println("End Game");
					return;
				}
				
				
				turn = 1; //Change to next turn
				
				System.out.println();
				break;
			
			default: break;//Do nothing
		}
		}
	}
	
	
	
	
	
	public static void main( String[] args ) {
		new Move_Main();
	}
}
