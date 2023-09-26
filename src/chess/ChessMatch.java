package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {       

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	
	public ChessMatch() {                    //Construtor da classe ChessMatch. Inicializa uma nova partida de xadrez com um tabuleiro de 8x8 casas e configura as peças no início da partida
	board = new Board (8, 8);
	turn = 1;
	currentPlayer = Color.WHITE;
	initialSetup();
	}
	
	public int getTurn() {		
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	
	public ChessPiece[][] getPieces() {																	// Este método retorna uma matriz de peças de xadrez (ChessPiece) representando o estado atual do tabuleiro de xadrez.
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];						// Ele percorre todas as posições do tabuleiro e realiza um downcasting (conversão de tipo) das peças genéricas (Piece)
		for (int i = 0; i<board.getRows(); i++) {														// para peças de xadrez (ChessPiece) e as armazena na matriz mat.
			for (int j = 0; j<board.getColumns(); j++ ) {												// Retorna a matriz de peças de xadrez.
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}		
		}
		return mat;
	}
	
	public boolean [][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();							// Converte a posição de origem de ChessPosition para Position, facilitando o uso das posições no código,
		validadeSourcePosition(position);											// valida a posição e obtém uma matriz de movimentos possíveis para a peça na posição de origem especificada.		
		return board.piece(position).possibleMoves();								
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {		
		Position source = sourcePosition.toPosition(); 														// Realiza uma jogada de xadrez a partir de uma posição de origem (sourcePosition) para uma posição de destino (targetPosition).
		Position target = targetPosition.toPosition();														// O método converte as posições de ChessPosition para Position, valida a posição de origem e destino, realiza a jogada,
		validadeSourcePosition(source);																		// captura qualquer peça adversária na posição de destino e retorna a peça capturada (ou null se não houver captura).
		validadeTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target); 
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
		nextTurn();
		}
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove (Position source, Position target) {															
		ChessPiece p =(ChessPiece) board.removePiece(source);	
		p.increaseMoveCount();																							// Este método executa uma jogada de xadrez no tabuleiro, movendo uma peça da posição de origem para a posição de destino.
		Piece capturedPiece = board.removePiece(target);																// Ele remove a peça da posição de origem, verifica se há uma peça na posição de destino (realizando uma possível captura),
		board.placePiece(p, target);																					// e coloca a peça na posição de destino.
		if (capturedPiece != null) {                                                                                    // O método retorna a peça capturada (ou null, se não houve captura).
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;																							
	}					
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		if (capturedPiece != null ) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
	}
	
	
						
	private void validadeSourcePosition(Position position) {								// Valida a posição de origem de um movimento de xadrez, garantindo que haja uma peça na posição especificada
		if (!board.thereIsAPiece(position)) {												// e que essa peça tenha movimentos possíveis disponíveis. Caso contrário, lança exceções apropriadas.
			throw new ChessException("There is no piece on source position");			
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
	}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}	
	}

	private void validadeTargetPosition (Position source, Position target) {				// Valida se a peça na posição de origem (source) pode se mover para a posição de destino (target).
		if (!board.piece(source).possibleMove(target)) {									// Para isso, ele acessa a peça na posição de origem utilizando o método 'board.piece(source)' e verifica se essa peça pode se mover para a posição de destino
			throw new ChessException("The chosen piece can't move to target position");		// usando o método 'possibleMove(target)' da peça. Se a peça não puder se mover para o destino, lança uma exceção indicando que o movimento é inválido.
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no " + color + "king on the board");
	}
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i<board.getRows(); i++) {
				for (int j = 0; j<board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position (i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
			
		}
		return true;
		
	}
	
	
	
	
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {					
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);													// Adiciona uma nova peça de xadrez ao tabuleiro na posição especificada por coluna e linha.
	}																					// Isso é feito convertendo a posição no formato de xadrez para a representação interna do tabuleiro (Position)
																						// e usando o método placePiece() do objeto board para colocar a peça na posição desejada.
	
	
	private void initialSetup() {															// Inicializa o tabuleiro de xadrez com as peças nas posições iniciais de uma partida.	 
		 placeNewPiece('a', 1, new Rook(board, Color.WHITE));
	        placeNewPiece('e', 1, new King(board, Color.WHITE));
	        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
	        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
	        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

	        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('e', 8, new King(board, Color.BLACK));
	        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
	        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
		}
	}



