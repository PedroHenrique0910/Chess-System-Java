package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {

	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	protected boolean isThereOpponentPiece(Position position) {				// Verifica se há uma peça adversária na posição especificada. Para isso, obtém a peça na posição
		ChessPiece p = (ChessPiece)getBoard().piece(position);				// através do método getBoard().piece(position), verifica se a peça não é nula (ou seja, há uma peça na posição)
		return p != null && p.getColor() != color;                          // e compara a cor dessa peça com a cor da peça atual. Se a cor for diferente, significa que é uma peça adversária.																	
	}
	
	
}
