package boardgame;

public abstract  class Piece {
	
	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() { // Este método permite que as subclasses acessem o tabuleiro ao qual a peça está associada.
		return board;
	}

	public abstract boolean[][] possibleMoves(); /* esse método serve como um contrato que exige que todas as subclasses de Piece forneçam sua 
													própria lógica para determinar as jogadas possíveis daquela peça específica. */
	
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()]; 	// Verifica se a peça pode fazer um movimento válido para a posição especificada.
	}																		// Este método usa a matriz lógica retornada pelo método possibleMoves() para determinar se a peça pode se mover para a posição especificada.
																			// return True se o movimento é válido, caso contrário, False.
	
	public boolean isThereAnyPossibleMove() {
		boolean [][] mat = possibleMoves();
		for(int i = 0; i<mat.length; i++) {					// Verifica se há pelo menos um movimento possível para a peça no tabuleiro.
			for (int j = 0; j<mat.length; j++) {			// O método percorre a matriz lógica de possíveis movimentos retornada pelo método possibleMoves().
					if (mat[i][j]) {						// Se encontrar pelo menos um movimento possível, retorna true, indicando que a peça pode se mover.
						return true;						// Caso contrário, retorna false, indicando que a peça não tem movimentos disponíveis.
					}
			}	
		}
		return false;
	}
}
