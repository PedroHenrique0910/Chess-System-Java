package application;

import boardgame.Board;

public class Program {

	public static void main(String[] args) {
		
		Board board = new Board (10, 20);
		
		System.out.printf(board.getColumns(), board.getRows());

	}

}
