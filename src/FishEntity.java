/* Entity.java
 * An entity is any object that appears in the game.
 * It is responsible for resolving collisions and movement.
 */
 

 
 public class FishEntity extends Entity {
	 
	 // the x axis movement speed
	 private static double moveSpeedX = 0;
	 
	 // the direction of the fish at the start
	 public static int directionStart = 0;
	 
	 // the direction of the fish for the object
	 public int direction = 0;
	 
	 // the sprite number for different sprite
	 public int spriteNum = 0;
	 private Game game;
	 
	 // the static direction of the fish but as a string
	 public static String directString = "";
	 
	 // the direction of the fish but as a string for getting the image from sprite
	 public String directStr = "";
	 
	 // a counter for sprite image changes
	 private static double counter = 0;

	 // the name of the fish that is used by the code
	 public String fName = "";
	 
	 // the name of the fish's first image
	 public static String fishNameStart = "";
	 
	 // an array that holds the images related to each fish
	 private String [] fishImages = new String [5];

	 public FishEntity(Game g) {
		    super("sprites/blueFish1.png",0,0);  // calls the constructor in Entity
		    fName = randomFish();
		    directStr = directString;
			fiName = "sprites/" + directStr + fName + "1.png";
		    changeSprite("sprites/" + directStr + fName + "1.png");
		    direction = directionStart;
		    fishImages = spriteMovement(fName);
			
		    
		    initializeFish();
		    game = g;
		    dx = moveSpeedX * direction;  // start off moving left  
		    spriteNum = 1;
		  } // constructor
	 
	 


	public void move (long delta){
		if (y <= 250) {
			y += 5;
		} // if
		
	
		// sets the moving pattern of the fish depending on the type
		 switch(fName) {
			 case "greyFish":
				 break;
			 case "brownFish":
				 y = (3 * (Math.sin((this.x/120)))+ y );
				 break;
			 case "blueFish":
				 y = (5 * (Math.sin((this.x/60)))+ y );
				break;
			 case "trash":
				 
			 case "shark":
			 
		 } // switch
		 	// changes the sprite image to make in look like its moving
			  if ((int)(counter % 10) ==0) {
				   spriteNum = (spriteNum >= 5) ? 0 : spriteNum;
				  changeSprite("sprites/" + fishImages[spriteNum] + ".png");
				  spriteNum++;
				  
	          } // if
			  
			  
			  counter = counter + delta/50.0;

		    // if the fish reaches the end of window the game removes the fish
		    if ((dx < 0) && (x < 10)) {
		    	 game.removeEntity(this);
		    } // if
		    
		    if ((dx > 0) && (x > 1400)) {
		    	 game.removeEntity(this);
		      } // if
		      
		      // proceed with normal move
		      super.move(delta);
	 } // move
	 
	 public void collidedWith(Entity other) {
	   } // collidedWith
	 
	 
	 // randomizes and initializes the fish
	 public static String randomFish(){
		 String fish = "";
		 
		 int rand = (int)(Math.random() * 40);
		 
		 // gives the fish a random type and speed
		 if (rand < 15) {
			 fish = "brownFish";
			 moveSpeedX = 75;
		} else if (rand >= 15 && rand < 25) {
			 fish = "blueFish";
			 moveSpeedX = 150;
			
		 } else if (rand >= 25 && rand < 35){
			 fish = "greyFish";
			 moveSpeedX = 100;
		 } else if (rand >=35 && rand < 37) {
			 fish = "shark";
			 moveSpeedX = 50;
		 } else {
			 fish = "garbage";
		     moveSpeedX = 50; 
		 } // else
		 
		 
		 
		 fishNameStart = fish;
		 
		 
		 // sets the direction of the fish
		 rand = (int) (Math.random() * 2);
		 if (rand == 0) { 
			 directionStart = -1;
			 directString = "-";
		 } else if(rand == 1) {
			 directionStart = 1;
			 directString = "";
		 } // else if
		 
		 return fishNameStart;
	 } // randomFish
 	
 	
	 
	 
 	public void initializeFish() {
 		
 		 // sets the x placement of the fish
		 int rand = (int)(Math.random() * 2);
		 
		 if (direction == 1) {
			 this.x= 10;
		 } else if(direction == -1) {
			 this.x = 1490;
		 } // else if
		
 		// sets the y value of the fish
		 rand = 0;
		 switch (this.fName) {
			 case "greyFish":
				 rand = (int)(Math.random() * 650) + 300;
				 break;
			 case "brownFish":
				 rand = (int)(Math.random() * 550) + 400;
				 break;
			 case "blueFish":
				 rand = (int)(Math.random() * 450) + 500;
				break;
			 case "garbage":
				rand = (int)(Math.random() * 500) + 300;
				break;
			 case "shark":
				rand = (int)(Math.random() * 550) + 400;
				break;
		 } // switch
		 this.y = rand;
 	} // initializeFish
 	
 	
 	// sets an array of sprites for each fish to simulate movement
 	public String[] spriteMovement(String x) {
 		
 		String [] fishI = new String [5];
 		
 		fishI[0] = directStr + x + "1";
 		fishI[1] = directStr + x + "2";
 		fishI[2] = directStr + x + "3";
 		fishI[3] = directStr + x + "4";
 		fishI[4] = directStr + x + "5";
 		
		return fishI;
 	} // spriteMovement
 	
 	
 	// moves the fish if a hook is detected
 	public void closeByed(Entity other) {
        if (other instanceof LineEntity && game.getWaitingForKeyPress() == false && !fiName.contains("garbage") && !fiName.contains("shark")) {
        	if (fiName.contains("brown") && y <= 300) {
			   this.y += 5;
		   } else {
			   this.y += 2;
		   } // else
        } // if
 	   } // collidedWith
 	
	} // Entity class