package chess;

import boardgame.Position;

public class ChessPosition {

	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row < 1 || row > 8 ) {
			throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8");
		}
		this.column = column;
		this.row = row;
	}
	
	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	protected Position toPosition() {							// Converte a posição de xadrez (coluna e linha) em uma posição interna (linha e coluna da matriz).
		return new Position (8 - row, column - 'a');			// Isso é útil para mapear as coordenadas do tabuleiro de xadrez para a matriz do jogo.	
	}
	

	protected static ChessPosition fromPosition (Position position) {								// Converte uma posição interna (linha e coluna da matriz) em uma posição de xadrez (coluna e linha).
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());		// Essa conversão é útil para exibir posições de forma legível no formato de xadrez.			
	}
	
	@Override
	public String toString () {
		return "" + column + row;
	}
	
}
