package boardgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) {     							// Retorna a peça em uma posição específica do tabuleiro ou lança uma exceção se a posição for inválida.	
		if (!positionExists(row, column)) {									// @return A peça na posição especificada.	
			throw new BoardException("Position not on the board");
		}
		return pieces[row] [column];
	}
	public Piece piece(Position position) { 								// Retorna a peça localizada na posição especificada no tabuleiro (porém através de um objeto position)
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) { 								// Coloca a peça na matriz do tabuleiro na posição especificada e atualiza a posição da peça.
		if (thereIsAPiece(position)) {														// Lança uma exceção se já houver uma peça na posição especificada.
			throw new BoardException("There is already a piece on position " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	public Piece removePiece (Position position) {                                          // Remove uma peça do tabuleiro na posição especificada e a retorna.
		if (!positionExists(position)) {													// A peça removida tem sua posição definida como null no tabuleiro.
			throw new BoardException("Position not on the board");							// A peça removida é armazenada temporariamente na variável aux.
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
	private boolean positionExists(int row, int column) {									 // Verifica se uma posição (representada por uma linha e uma coluna) está dentro dos limites válidos do tabuleiro.	
		return	row >= 0 && row < rows && column >=0 && column < columns;					// Return true se a posição estiver dentro dos limites do tabuleiro, caso contrário, false.
	}
		
	public boolean positionExists(Position position) { 										// Este método adapta a lógica do método positionExists para aceitar um objeto Position.
		return positionExists(position.getRow(), position.getColumn());						// Este método é uma sobrecarga do método anterior	
	}
	
	public boolean thereIsAPiece(Position position) {										// Verifica se existe uma peça (objeto Piece) em uma posição específica do tabuleiro.
		if (!positionExists(position)) {													// Retorna true se houver uma peça na posição especificada ou false se a posição estiver vazia (sem peça).
			throw new BoardException("Position not on the board");
		}
		return piece(position) != null;
	}
}
