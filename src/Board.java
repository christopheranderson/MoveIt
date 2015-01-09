/*
 * Chris Anderson
 * Move it Game
 * Problem Solving
 * 4/13/2012
 */

import java.util.LinkedList;


public class Board {
	int[][] B;
	int[][] compval = {
			{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE},
			{0,110,202,286,362,430,490,542,586,750},
			{0,120,213,298,375,444,505,558,603,750},
			{0,130,224,310,388,458,520,574,620,750},
			{0,140,235,322,401,472,535,590,637,750},
			{0,150,246,334,414,486,550,606,654,750},
			{0,160,257,346,427,500,565,622,671,750},
			{0,170,268,358,440,514,580,638,688,750},
			{0,180,279,370,453,528,595,654,705,750},
			{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE}
	};
	int[][] userval = {
			{Integer.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0, 0, Integer.MIN_VALUE},
			{Integer.MIN_VALUE,110,120,130,140,150,160,170,180,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,202,213,224,235,246,257,268,279,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,286,298,310,322,334,346,358,370,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,362,375,388,401,414,427,440,453,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,430,444,458,472,486,500,514,528,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,490,505,520,535,550,565,580,595,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,542,558,574,590,606,622,638,654,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,586,603,620,637,654,671,688,705,Integer.MIN_VALUE},
			{Integer.MIN_VALUE,0,0,0,0,0,0,0,0,Integer.MIN_VALUE}
	};
	
	int[] compblk = {Integer.MIN_VALUE, 10, 10, 10, 10, 12, 11, 11, 11, Integer.MIN_VALUE};
	int[] userblk = {Integer.MIN_VALUE, 10, 10, 10, 10, 12, 11, 11, 11, Integer.MIN_VALUE};
	
	public Board(){
		B = new int[10][10];
		
		for(int i = 0 ; i < 10; i++){
			for(int j = 0; j < 10; j++){
				B[i][j] = 0;
			}
		}
		
		for(int i = 2; i < 9; i++){
			B[i][0] = 1; //computer
			B[0][i] = 2; //user
		}
	}
	
	//Intakes what it assumes is a valid board and a valid move
	public Board(Board in, Move m){
		B = new int[10][10];
		
		for(int i = 0 ; i < 10; i++){
			for(int j = 0; j < 10; j++){
				this.B[i][j] = in.B[i][j];
			}
		}
		
		this.B[m.nr][m.nc] = this.B[m.or][m.oc];
		this.B[m.or][m.oc] = 0;
	}
	
	public int eval(){
		int val = 0;
		
		//check wins
		if(userwin()) return Integer.MIN_VALUE;
		if(compwin()) return Integer.MAX_VALUE;
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(this.B[i][j] == 1){ //This is a computer
					val += compval[i][j];
//					//Check if it is blocked
//					for(int k = j+1; k < 10; k++){
//						if(this.B[i][k] != 0){
//							val -= userblk[k];
//							break;
//						}
//					}
				}
				else if(this.B[i][j] == 2){ //this is a user
					val -= userval[i][j];
//					//check if it is blocked
//					for(int k = i+1; k < 10; k++){
//						if(this.B[k][j] != 0){
//							val += compblk[k];
//							break;
//						}
//					}
				}
			}
		}
		return val;
	}
	
	public boolean userwin(){
		int count = 0;
		for(int i = 1; i < 9; i++){
			if(B[9][i] == 2){
				count++;
			}
		}
		if(count >= 7) return true;
		return false;
	}
	
	public boolean compwin(){
		int count = 0;
		for(int i = 1; i < 9; i++){
			if(B[i][9] == 2){
				count++;
			}
		}
		if(count >= 7) return true;
		return false;
	}
	
	public LinkedList<Move> getUserMoves(){
		LinkedList<Move> L = new LinkedList<Move>();
		for(int i = 0; i < 10; i++){
			//add moves
			for(int j = 1; j < 9; j++){
				if(this.B[i][j] == 2){
					//add forward
					if((i+1 < 10) && (B[i+1][j] == 0)) L.add(new Move(i,j,i+1,j));
					//add left
					if((j-1 > 0 ) && (B[i][j-1] == 0)) L.add(new Move(i,j,i,j-1));
					//add right
					if((j+1 < 9 ) && (B[i][j+1] == 0)) L.add(new Move(i,j,i,j+1));
				}
			}
		}
		return L;
	}
	
	public LinkedList<Move> getCompMoves(){
		LinkedList<Move> L = new LinkedList<Move>();
		for(int i = 1; i < 9; i++){
			//add moves
			for(int j = 0; j < 9; j++){
				if(this.B[i][j] == 1){
					//add forward
					if((j+1 < 10) && (B[i][j+1] == 0)) L.add(new Move(i,j,i,j+1));
					//add left
					//if((i-1 > 0 ) && (B[i-1][j] == 0)) L.add(new Move(i,j,i-1,j));
					//add right
					//if((i+1 < 9 ) && (B[i+1][j] == 0)) L.add(new Move(i,j,i+1,j));
				}
			}
		}
		
		if(L.isEmpty()){
			for(int i = 1; i < 9; i++){
				//add moves
				for(int j = 0; j < 9; j++){
					if(this.B[i][j] == 1){
						//add forward
						//if((j+1 < 10) && (B[i][j+1] == 0)) L.add(new Move(i,j,i,j+1));
						//add left
						if((i-1 > 0 ) && (B[i-1][j] == 0)) L.add(new Move(i,j,i-1,j));
						//add right
						if((i+1 < 9 ) && (B[i+1][j] == 0)) L.add(new Move(i,j,i+1,j));
					}
				}
			}
		}
		if(L.isEmpty()){
			for(int i = 1; i < 9; i++){
				//add moves
				for(int j = 9; j < 10; j++){
					if(this.B[i][j] == 1){
						//add forward
						//if((j+1 < 10) && (B[i][j+1] == 0)) L.add(new Move(i,j,i,j+1));
						//add left
						if((i-1 > 0 ) && (B[i-1][j] == 0)) L.add(new Move(i,j,i-1,j));
						//add right
						if((i+1 < 9 ) && (B[i+1][j] == 0)) L.add(new Move(i,j,i+1,j));
					}
				}
			}
		}
		return L;
	}
	
}
