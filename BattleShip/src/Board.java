public class Board{
//FIELDS
    private final char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','V','W','X','Y','Z'};
    private final int numRows = 10;
    private final int numCols = 10;
    private char[][] grid;//[row][column]
    private Player owner;
//CONSTRUCTOR
    public Board(Player owner){
        this.grid = new char[this.numRows][this.numCols];
        this.createGrid();
        this.owner = owner;
    }
//GETTERS AND SETTERS
    public char[][] getGrid() {return this.grid;}
    public int getNumRows() {return this.numRows;}
    public int getNumCols() {return this.numCols;}
    public void setCell(int[] coors, char newChar){this.grid[coors[0]][coors[1]] = newChar;}
//METHODS
	/** createGrid()
	*Summary: makes the board
	*Parameters: (Ship ship, int[] starting coordinates, int[] ending coordinates)
	*Returns/Outputs: prints board
	*Other Effects/Changes in object state: none
	*/
    private void createGrid(){
        for (int i = 0; i < this.getNumCols(); i++)
            for (int j = 0; j < this.getNumRows(); j++)
                 this.grid[i][j] = '~';
    }
    
	/** inBounds()
	*Summary: while the coordinate is inside the table
	*Parameters: (String cell)
	*Returns/Outputs: true/false (whether or not it's inside)
	*Other Effects/Changes in object state: none
	*/
    public boolean inBounds(String cell) {
        int[] coordinates = Game.changeToCoor(cell);
        return this.inBounds(coordinates[0], coordinates[1]);
    } 
    
    public boolean inBounds(int col, int row){
        return row >= 0 && row < this.numRows && col >= 0 && col < this.numCols;
    }

	/** printBoard()
	*Summary: prints out the tiles of the board and updates them
	*Parameters: (none)
	*Returns/Outputs: none
	*Other Effects/Changes in object state: prints out board
	*/
	public void printBoard(){//prints the board
        System.out.println("\n" + this.owner.getName() + "'s Board\n\n");
		// prints columns
		System.out.print("   "); //aligning the "1 2 3 4.."
		
		for(int i = 1; i <= this.numCols;i++)
		    System.out.print(i + " ");
		
		System.out.println("\n");//skips 2 lines

		// prints rows 
		for (int i = 0; i < this.grid.length; i++) {
			System.out.print(this.alphabet[i] + "  ");
			
			for (int j = 0; j < this.grid[i].length; j++) {
		        System.out.print(this.grid[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	public static void main(String[] args){
	  
	}
}