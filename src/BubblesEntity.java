
public class BubblesEntity extends Entity{
	
	private Game game;

	public BubblesEntity(Game g, int newX) {
		super("sprites/bubble.png",newX, 1000);
		dy = -50;
		game = g;
	}
	
	public void move (long delta){
		    // if we reach left side of screen and are moving left
		    // request logic update
		    if (this.y <= 300) {
		    	changeSprite("sprites/bubblepop.png");
		    }
		    	
		    if (this.y <= 250) {
		    	 game.removeEntity(this);
		    } // if
		    
		      // proceed with normal move
		      super.move(delta);
	 }
	
	public static int randomX(){
		 int rand = (int) (Math.random() * 1500);
		 return rand;
	}

	public void collidedWith(Entity other) {
	}

	public void closeByed(Entity other) {
		
	}

}
