/*
 * Chris Anderson
 * Move it Game
 * Problem Solving
 * 4/13/2012
 */

import java.util.LinkedList;


public class Game {
	Board curr;
	int MAX_DEPTH;
	Move usersMove;
	Move compsMove;
	
	public Game(){
		curr = new Board();
		MAX_DEPTH = 6;
		usersMove = null;
		compsMove = null;
	}
	
	public Move docompmove(){
		Move mycompMove = compMove();
		curr = new Board(curr, mycompMove);
		this.compsMove = mycompMove;
		return mycompMove;
	}
	
	public Move douserMove(Move m){
		if(curr.B[m.or][m.oc] == 2){
			if(m.nr >= 0 && m.nr <= 9 && m.nc >=1 && m.nc <=8){
				curr = new Board(curr,m);
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
		this.usersMove = m;
		return m;
	}
	
	public int min(Board s, int a, int b, int depth){
		int score, best;
		LinkedList<Move> l;
		Move m;
		
		
		if(depth == this.MAX_DEPTH){
			return s.eval();
		}
		
		best = b;
		l = s.getUserMoves();
		while(!l.isEmpty()){
			m = l.remove();
			score = max(new Board(s,m),a,best,depth+1);
			if(score < best) best = score;
			
			if(score <= a) break;
		}
		
		
		
		return best;
	}
	
	public int max(Board s, int a, int b, int depth){
		int score, best;
		LinkedList<Move> l;
		Move m;
		
		if(depth == this.MAX_DEPTH){
			return s.eval();
		}
		
		best = a;
		l = s.getCompMoves();
		while(!l.isEmpty()){
			m = l.remove();
			score = min(new Board(s,m),best,b,depth+1);
			if(score > best) best = score;
			
			if(score >= b) break;
		}
		
		return best;
	}
	
	public Move compMove(){
		int score, best;
		LinkedList<Move> l;
		Move m, bestM = null;
		Board s = curr;
		
		MAX_DEPTH = 6;
		int count=0;
		for(int i = 1; i < 9; i++){
			if(curr.B[9][i] != 0) count++;
			if(curr.B[i][9] != 0) count++;
		}
		MAX_DEPTH += (count/3);
		
		best = Integer.MIN_VALUE;
		l = s.getCompMoves();
		while(!l.isEmpty()){
			m = l.remove();
			score = min(new Board(s,m), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
			if( score >= best){
				best = score;
				bestM = m;
			}
		}
		if(bestM == null){
			System.out.println("OH NO!");
		}
		return bestM;
	}
}
