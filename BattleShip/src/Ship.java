public class Ship{
//FIELDS     
    private String name;
    private int length;
    private int[][] coordinates;//[length][row, column, state(1/0 for alive/dead)]
//CONSTRUCTOR
    public Ship(String name, int length){
        this.name = name;
        this.length = length;
        this.coordinates = new int[length][3];
        for(int i = 0; i < coordinates.length; i++){
            this.coordinates[i][0] = -1;
            this.coordinates[i][1] = -1;
		        this.coordinates[i][2] = 1;
        }
    }
//GETTERS AND SETTERS
    public String getName(){return this.name;}
    public void setName(String newName){name = newName;}
    public int getLength(){return this.length;}
    public int[][] getCoordinates(){return this.coordinates;}
	public void setCoordinate(int[] input){
		for(int i = 0; i < this.coordinates.length; i++)
			if(this.coordinates[i][0] == -1){
				this.coordinates[i][0] = input[0];
				this.coordinates[i][1] = input[1];
				break;
			}
	}
//METHODS
	/** isSunk()
	*Summary: checks if the ship object is sunk or not
	*Parameters: (none)
	*Returns/Outputs: boolean of sunk or swim state
	*Other Effects/Changes in object state: none
	*/
    public boolean isSunk(){//checks if ship is sunk
        for(int[] coors : this.coordinates)
            if(coors[2] == 1)
				return false;//returns false if any tile is not sunk
        return true;//returns true if every tile in it is sunk
    }
    public void sinkSpace(int[] coor){
		for(int i = 0; i < this.coordinates.length; i++)
			if(coor[0] == this.coordinates[i][0] && coor[1] == this.coordinates[i][1])
				this.coordinates[i][2] = 0;
    }
    
	/** isHit()
	*Summary: checks if the ship has been hit or not
	*Parameters: (coordinate input array)
	*Returns/Outputs: boolean of hit or miss state
	*Other Effects/Changes in object state: none
	*/
    public boolean isHit(int[] input){
        for(int[] coors: this.coordinates)
            if(input[0] == coors[0] && input[1] == coors[1] && coors[2] == 1)
                return true;
        return false;
    }
//MAIN METHOD
	public static void main(String[] args){
		Ship ship = new Ship("Boaty", 1);
		ship.setCoordinate(new int[] {1,2});
		System.out.print(ship.isHit(new int[] {1,2}));
	}
}