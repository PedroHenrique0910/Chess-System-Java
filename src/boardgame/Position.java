package boardgame;

public class Position {

	private int row;
	private int column;
	public Position(int row, int column) {
		
		this.row = row;
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	
	public void setValues(int row, int column) {   //Atualiza os valores da posição (row, column).         
		 this.row = row;									
		 this.column = column;
	}
	
	@Override
	public String toString() {			// Cria uma representação em formato de string da posição, exibindo as coordenadas de linha e coluna 
		return row + ", " + column;		//separadas por uma vírgula e um espaço.
	}
	
	
}

// A classe Position é usada para representar e manipular posições em um tabuleiro.
// Ela define a estrutura para representar posições por meio de valores de linha e coluna.
// Outras classes, como peças de xadrez ou o tabuleiro em si, podem criar objetos Position
// para acessar e manipular posições de forma consistente, seguindo as regras definidas
// na classe Position. Isso melhora a organização do código e a legibilidade do programa,
// pois fornece uma estrutura predefinida para lidar com posições.

