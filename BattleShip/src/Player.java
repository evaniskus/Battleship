import java.util.*;
public class Player{
//FIELDS      
    private String name;
    private Ship[] fleet;
    private Board board;
    private Scanner kb;
//CONSTRUCTOR
    public Player(String name){
		this.name = name;
		this.fleet = this.createFleet();
		this.board = new Board(this);
		this.kb = new Scanner(System.in);
    }
//GETTERS AND SETTERS
	public String getName(){return this.name;}
    public void setName(String newName) {this.name = newName;}
    public Ship[] getFleet() {return this.fleet;}
	public Board getBoard() {return this.board;}
//METHODS
	/** createFleet()
	*Summary: creates a fleet array filled with 5 ship objects, each with different names and lengths
	*Parameters: (none)
	*Returns/Outputs: returns the new fleet array of ships for further use
	*Other Effects/Changes in object state: none
	*/
	public Ship[] createFleet(){
        Ship aircraftCarrier = new Ship("Aircraft Carrier", 5);
        Ship battleship = new Ship("Battleship", 4);
		Ship cruiser = new Ship("Cruiser", 3);
		Ship submarine = new Ship("Submarine", 3);
		Ship destroyer = new Ship("Destroyer", 2);
		return new Ship[] {aircraftCarrier, battleship, cruiser, submarine, destroyer};
    }
	/** placePlayerShips()
	*Summary: prompts the player for coordinates and uses those coordinates to place ships
	*Parameters: (none)
	*Returns/Outputs: places the boat and shows the ship initial on the board
	*Other Effects/Changes in object state: changes boolean in canPlaceShip() to true or false
	*/
    public void placePlayerShips(){
		String alphabet = "ABCDEFGHIJEFGHIJKLMNOPQRSTUVWXYZ";
        this.board.printBoard();
		for(Ship ship: this.fleet){//for each ship object inside fleet
            
			int[] startingCoor; 
            int[] endingCoor;
            String startCell;
            String endCell;
            
            do{
    			while(true){//keep running this until user inputs correct coordinates
    				System.out.println("===================================");
    				System.out.print("Enter the starting coordinate for your " + ship.getName() + " (Length: " + ship.getLength() + "(if your inputs are invalid, we will ask you to input it again!)): ");
    				startCell = this.kb.next().toUpperCase();
    				try{//set the starting coordinate of the board = coordinate input
    					startingCoor = Game.changeToCoor(startCell);
    					alphabet.indexOf(startCell.charAt(0));//1st index of input HAS to be a letter
    					Integer.parseInt(startCell.substring(1));//2nd and/or 3rd input has to be a number
    					break;
    				} catch (Exception e) {//catches exception
    					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    					System.out.println("Faulty input. Please try again.");
    					continue;
    				}
    			}
    			
    			while(true){
    				System.out.print("Enter the ending coordinate for your " + ship.getName() + ": ");
    				endCell = this.kb.next().toUpperCase();
    				try{
    					endingCoor = Game.changeToCoor(endCell);
    					alphabet.indexOf(endCell.charAt(0));
    					Integer.parseInt(endCell.substring(1));
    					break;
    				} catch (Exception e) {
    					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    					System.out.println("Faulty input. Please try again.");
    					continue;
    				} 
    			}
            }
            while(!this.canPlaceShip(ship, startingCoor, endingCoor));//checks to see if boat can be placed correctly)
			
			
            
			if(startingCoor[0] == endingCoor[0]){//if the ships are lined up horizontally
				for(int i = Math.min(startingCoor[1], endingCoor[1]); i <= Math.max(startingCoor[1],endingCoor[1]); i++){
					ship.setCoordinate(new int[] {startingCoor[0],i});
					this.board.setCell(new int[] {startingCoor[0],i}, ship.getName().charAt(0));
				}
			}
			if(startingCoor[1] == endingCoor[1]){//if the ships are lined up vertically
				for(int i = Math.min(startingCoor[0], endingCoor[0]); i <= Math.max(startingCoor[0],endingCoor[0]); i++){
					ship.setCoordinate(new int[] {i,startingCoor[1]});
					this.board.setCell(new int[] {i,startingCoor[1]}, ship.getName().charAt(0));
				}
			}
            this.board.printBoard();
        }
        System.out.println("You have placed all of your ships!");//alert only when user has succesfully placed all boats
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
	
	/** placeAIShips()
	*Summary: randomly places AI fleet onto the ai's board
	*Parameters: (none)
	*Returns/Outputs: none
	*Other Effects/Changes in object state: modifies AI's board state as well as places boats onto it
	*/	
    public void placeAIShips(){//randomly places the AI's ships
        for(Ship ship: this.getFleet()){
            int row = (int)(Math.random()*(this.board.getNumRows()-ship.getLength()+1));
            int col = (int)(Math.random()*(this.board.getNumCols()-ship.getLength()+1));
            boolean goesDown = Math.random() >= 0.5;//chooses randomly between placing down and placing right
            
            
            if(goesDown){//if ai's ship is vertical
                while(!this.canPlaceShip(ship, new int[] {row,col}, new int[] {row + ship.getLength() - 1,col})){//picks new coordinates if the ones chosen already cannot be used
                    row = (int)(Math.random()*(this.board.getNumRows()-ship.getLength()+1));
                    col = (int)(Math.random()*(this.board.getNumCols()-ship.getLength()+1));
                }
            }
            else{//if horizontal
                while(!this.canPlaceShip(ship, new int[] {row,col}, new int[] {row,col + ship.getLength() - 1})){//picks new coordinates if the ones chosen already cannot be used
                    row = (int)(Math.random()*(this.board.getNumRows()-ship.getLength()+1));
                    col = (int)(Math.random()*(this.board.getNumCols()-ship.getLength()+1));
                }
            }
			
			
            if(goesDown){
                for(int i = row; i <= row + ship.getLength()-1; i++){
                    ship.setCoordinate(new int[] {i,col});
                }
            }else{
                for(int i = col; i <= col + ship.getLength()-1; i++){
                    ship.setCoordinate(new int[] {row,i});
                }
            }
            
            //TEST CODE
                System.out.println(ship.getName() + "'s occupied spaces");
                for(int[] coord: ship.getCoordinates()){
                    System.out.print(coord[0]);
                    System.out.print(", ");
                    System.out.print((coord[1]) + "\n");
                }
        
        }
        System.out.println("The AI's ships have been placed!");//alert only when all of the AI's boats have been successfully placed
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
	/** canPlaceShip()
	*Summary: checks for all possible bugs in ship placement
	*Parameters: (Ship ship, int[] starting coordinates, int[] ending coordinates)
	*Returns/Outputs: true/false (if false, then replace the ship until returns true)
	*Other Effects/Changes in object state: none
	*/	
    public boolean canPlaceShip(Ship ship, int[] startingCoor, int[] endingCoor){//tells if the ship can be placed with the inputted start and end coordinates
		if(startingCoor[0] == endingCoor[0] && startingCoor[1] == endingCoor[1]){//if they input the exact same coordinates
			return false;//handles an exception
		}
		if(startingCoor[0] < 0 || startingCoor[0] > this.board.getNumRows()-1 || startingCoor[1] < 0 || startingCoor[1] > this.board.getNumCols()-1 || endingCoor[0] < 0 || endingCoor[0] > this.board.getNumRows()-1 || endingCoor[1] < 0 || endingCoor[1] > this.board.getNumCols()-1){//checking that the coordinates dont go out of bound
			 return false;
		}     
			
        if(startingCoor[0] == endingCoor[0]){//ships coordinates line up horizontally
             for(int i = Math.min(startingCoor[1], endingCoor[1]); i < Math.max(startingCoor[1], endingCoor[1]); i++){
                 for(Ship eachShip : fleet){//checks every ship in fleet
                      for(int[] coors : eachShip.getCoordinates()){//checks every spot that each ship is occupying
                           if(startingCoor[0] == coors[0] && i == coors[1]){//returns false if one of the coordinates between start and end matches one of the ship coordinates
                                 return false;
                            }
                       }
                  }
             } 
            return Math.abs(startingCoor[1] - endingCoor[1]) + 1 == ship.getLength();//checks if ship length is equal to space between the two
        }
        
        if(startingCoor[1] == endingCoor[1]){//ships coordinates line up vertically
             for(int i = Math.min(startingCoor[0], endingCoor[0]); i < Math.max(startingCoor[0], endingCoor[0]); i++){
               for(Ship eachShip : fleet){//checks every ship in fleet
                  for(int[] coors : eachShip.getCoordinates()){//checks every spot that each ship is occupying
                       if(startingCoor[1] == coors[1] && i == coors[0]){//returns false if one of the coordinates between start and end matches one of the ship coordinates
                             return false;
                       }
                  }
               }
             }
            return Math.abs(startingCoor[0] - endingCoor[0]) + 1 == ship.getLength();
        }
        return false;//returns false otherwise;
    }

	/** fleetDestroyed()
	*Summary: checks to see if every ship in fleet has been sunk
	*Parameters: (Ship ship, int[] starting coordinates, int[] ending coordinates)
	*Returns/Outputs: true/false
	*Other Effects/Changes in object state: none
	*/
    public boolean fleetDestroyed(){
        for(Ship ship : this.getFleet())//checks every ship in fleet
            if(!ship.isSunk())
                return false;//returns false if a ship has not been sunk
        return true;
    }
    
    
//MAIN METHOD
    public static void main(String[] args){
		Player evan = new Player("evan");
		evan.placeAIShips();
    }
}