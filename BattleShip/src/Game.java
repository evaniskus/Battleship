import java.util.*;
public class Game{
//FIELDS
    private Player ai;
    private Player human;
//CONSTRUCTOR
    public Game(){
        this.ai = new Player("AI");
        this.human = new Player("");
    }
//METHODS
	/** play()
	*Summary: runs the game by calling other methods
	*Parameters: none
	*Returns/Outputs: none
	*Other Effects/Changes in object state: none
	*/
    public void play(){//runs game
        this.startingMessage();
        
		this.human.placePlayerShips(); 
        this.ai.placeAIShips();
        
		while(true){//keeps going until checkForWin() exits the program
            
            this.ai.getBoard().printBoard();
            this.human.getBoard().printBoard();
            
            this.processPlayerMove();
            this.checkForWin(this.human);
            
            this.processAIMove();
            this.checkForWin(this.ai);
        }
    }
	
	/** startingMessage()
	*Summary: asks player for name and sets it to the player object
	*Parameters: none
	*Returns/Outputs: none
	*Other Effects/Changes in object state: changes player name
	*/
    public void startingMessage(){
        Scanner kb = new Scanner(System.in);
        System.out.print("Welcome to BattleShips! Please enter your name: ");
        String name = kb.nextLine();
        human.setName(name);
        kb.close();
    }
    
	/** changeToCoor()
	*Summary: changes firing code (a1) into cartesian coordinates [row][column]
	*Parameters: input coordinate as a String
	*Returns/Outputs: returns new coordinates as an int array
	*Other Effects/Changes in object state: changes coordinates
	*/
    public static int[] changeToCoor(String input){//changes a firing code like d10 into [3,9]

		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		input = input.toUpperCase();
	    
        int row = alphabet.indexOf(input.charAt(0));
        int col = Integer.parseInt(input.substring(1, input.length()));
    
        return new int[]{row, col-1};
    }
    
	/** checkForWin()
	*Summary: checks to see if the player has won the game, then exits the program
	*Parameters: the player object which is being checked
	*Returns/Outputs: none
	*Other Effects/Changes in object state: exits the game and displays game winner
	*/
    public void checkForWin(Player player){//checks if a player has won, prints out according message, and quits the program
        if(player.fleetDestroyed()){
            if(this.ai.fleetDestroyed())
                System.out.println("Congratulations, " + this.human.getName() + "!\nYou Have Won!\nThank You For Playing!");
            else
                System.out.println("Game Over!\nThe A.I. Has Won!\nThank You For Playing!");
            System.exit(0);
        }
    }
    
	/** processPlayerMove()
	*Summary: asks user to input a firing code, then processes it and checks to see if a)it's a legit firing coordinate and b) if hit or miss
	*Parameters: none
	*Returns/Outputs: none
	*Other Effects/Changes in object state: makes the player's move
	*/
    public void processPlayerMove(){
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Scanner kb = new Scanner(System.in);
        String code;
		
        while(true){//ask user to make a shot
			System.out.println("===================================");
            System.out.print("Please enter your firing code(remember, you can shoot at the same spot twice, and so can the AI!): ");
            
			try{//if the code is a legit firing coordinate
				code = kb.nextLine().toUpperCase();
				alphabet.indexOf(code.charAt(0));
				Integer.parseInt(code.substring(1));
				human.getBoard().inBounds(code);
				break;
			} catch (Exception e) {//catches exception and re-asks the user to input
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("Faulty input. Please try again.");
				continue;
			}
		}
        
        //if(code.equals("MISTER CARR")){//SUPER SECRET CHEAT CODE
        //    for(Ship ship: this.ai.getFleet()){
        //        for(int[] coor: ship.getCoordinates()){
        //            ship.sinkSpace(new int[] {coor[0], coor[1]});
        //        }
        //    }
        //    System.out.println("You unleashed an ancient power...");
        //    this.checkForWin(this.human);
        //}    
        
        int[] firingCode = Game.changeToCoor(code);
        //TEST
            //System.out.print("The firing code converted: " + firingCode[0] + ", " + firingCode[1]);
        String shipsSunk = "";
        
        boolean miss = true;
        
        for(Ship ship: this.ai.getFleet()){//for each ship object inside fleet array
            //TEST
            //    System.out.println(ship.isHit(firingCode));
            if(ship.isHit(firingCode)){//if hit
                ship.sinkSpace(firingCode);
                this.ai.getBoard().setCell(firingCode, 'X');
				System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");//for da aesthetics
				System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
				System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
				System.out.println(" ");
                System.out.println("You've hit the enemy's " + ship.getName());
                miss = false;
            }
            if(ship.isSunk())
                shipsSunk += (ship.getName() + ", ");
        }
        
        if(miss){//if miss
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
			System.out.println(" ");
            System.out.println("You missed!");
            this.ai.getBoard().setCell(firingCode, 'M');
        }
            
        System.out.println("Ships you've sunk so far: " + shipsSunk);
        
        for(Ship ship: this.ai.getFleet()){
            if(!ship.isSunk()){
                System.out.println("And more to come? :^)");
                break;            
            }    
        }
        
        kb.close();
    }
    
	/** processAIMove()
	*Summary: makes AI make a firing code, then processes it and checks to see if it hits or miss a player's ship
	*Parameters: none
	*Returns/Outputs: none
	*Other Effects/Changes in object state: makes the AI's move
	*/
    public void processAIMove(){
		char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','V','W','X','Y','Z'};

        int row = (int)(Math.random()*(this.human.getBoard().getNumRows()));
        int col = (int)(Math.random()*(this.human.getBoard().getNumCols()));
        int[] firingCode = new int[] {row, col};
        
        String firingCodeString = alphabet[row] + Integer.toString(col+1);
		System.out.println("===========================");
        System.out.println("The enemy fired on " + firingCodeString + "!");
        
        boolean miss = true;
        
        for(Ship ship: this.human.getFleet()){//for each ship object inside fleet array
            if(ship.isHit(firingCode)){//if hit
                ship.sinkSpace(firingCode);
                this.human.getBoard().setCell(firingCode, 'X');
                System.out.print("The enemy has hit your " + ship.getName());
                miss = false;
            }
        }
        
        if(miss){//if miss
            this.human.getBoard().setCell(firingCode, 'M');
            System.out.println("The enemy missed!");
        }    
    }
    
//MAIN METHOD
	public static void main(String[] args){
		System.out.println(Arrays.toString(Game.changeToCoor("a1")));
	}
}