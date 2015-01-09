/*
 * Chris Anderson
 * Move it Game
 * Problem Solving
 * 4/13/2012
 */


public class Move {
	int or, oc, nr, nc;
	public Move(int n1, int n2, int n3, int n4){
		or = n1; oc = n2; nr = n3; nc = n4;
	}
	
	public String printMove() {
		return or + " " + oc + " " + nr + " " + nc;
	}
	
	public Move reverse(){
		int t;
		t = or;
		or = oc;
		oc = t;
		
		t = nr;
		nr = nc;
		nc = t;
		
		return this;
	}
}
