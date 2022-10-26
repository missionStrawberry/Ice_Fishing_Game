/* Entity.java
 * An entity is any object that appears in the game.
 * It is responsible for resolving collisions and movement.
 */

 public class LineEntity extends Entity {
	 private Game game;
	 private boolean used = false;
	 

	 public LineEntity(Game g, String r, int newX, int newY) {
		    super(r, newX, newY);  // calls the constructor in Entity
		    game = g;
		    if(r.contains("fish") || r.contains("Fish")|| r.contains("garbage")) used = true;
		  } // constructor
	 
	 public void move (long delta){
		// if a fish should be taken out of the water, remove the fish and put a new hook in
	    if (used == true && y <= 250) {
	    	game.fishCaught(x, fiName);
	    	game.removeEntity(this);
		   
		 }
		 // stop at the ice
		 if ((dy < 0) && (y < 250)) {
	      return;
	    } // if
		
	    // stop at bottom of screen
	    if ((dy > 0) && (y > 950)) {
	      return;
	    } // if
	    
	    
	    super.move(delta);
	 }
	 
	 public void collidedWith(Entity other) {
		// prevents double kills
	     if (used && !other.fiName.contains("shark")) {
	       return;
	     } // if
	     
	     // if it has hit a fish, move the fish with the line
	     if (other instanceof FishEntity) {
	       // remove affect entities from the Entity list
	       if (!other.fiName.contains("shark")) {
	    	   game.moveFish(x, other.fiName);
	    	   game.removeEntity(this);
		       game.removeEntity(other);
	       } else if (other.fiName.contains("shark")) {
	    	   game.looseLife(x);
	    	   game.removeEntity(this);
	    	   game.fishCaught(x, "");
	       } // else if
	       
	       
	       // catch the fish
	       used = true;
	       

	     } // if
	 } // collidedWith
	 
	 public void closeByed(Entity other) {
	        
	 } // closeByed
	 

 } // Entity class