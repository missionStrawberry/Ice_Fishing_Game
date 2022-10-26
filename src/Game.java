 /* Game.java
 * Ice Fishing Main Program
 *  By: Anna Myllyniemi, Matt Sielecki, Jason Zhou
 *  Date: October 28, 2019
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.ArrayList;

public class Game extends Canvas {


      	private BufferStrategy strategy;   // take advantage of accelerated graphics
        private boolean waitingForKeyPress = true;  // true if game held up until
                                                    // start is pressed
        private boolean spacePressed = false; // true if p1 line going down
        private boolean downPressed = false; // true if p2 line going down
        private boolean startPressed = false; // true if user hits enter to start the game
        private boolean twoP = false; // true if p2 plays the game
        private boolean menu = true; // true if the menu should be displayed

        private boolean gameRunning = true; // true if the game is running
        private ArrayList<Entity> entities = new ArrayList<Entity>(); // list of entities
                                                      // in game
        private ArrayList<Entity> removeEntities = new ArrayList<Entity>(); // list of entities
                                                            // to remove this loop
        private Entity hook;  // p1 hook
        private Entity hook2; // p2 hook
        
        private int length = 1500; // length from left to right of the game window in px
        private int width = 1000; // width from top to bottom of the game window in px
        
        private int counter = 0; // counts how many frames are drawn from the start of each game.
        										// used to determine how often are drawn
        private int score = 0; // p1 score
        private int score2 = 0; // p2 score
       
        private int lives = 3; // p1 lives
        private int lives2 = 3; // p2 lives
        
        private long startT = 0; // time the game started in milliseconds
        private long currentT = 0; // current time in milliseconds
        
        private String message = ""; // message to display while waiting
                                     // for a key press


    	/*
    	 * Construct our game and set it running.
    	 */
    	public Game() {
    		// create a frame to contain game
    		JFrame container = new JFrame("Commodore 64 Ice Fishing");
    
    		// get hold the content of the frame
    		JPanel panel = (JPanel) container.getContentPane();
    
    		// set up the resolution of the game
    		panel.setPreferredSize(new Dimension(length,width));
    		panel.setLayout(null);
    
    		// set up canvas size (this) and add to frame
    		setBounds(0,0,length,width);
    		panel.add(this);
    
    		// Tell AWT not to bother repainting canvas since that will
            // be done using graphics acceleration
    		setIgnoreRepaint(true);
    
    		// make the window visible
    		container.pack();
    		container.setResizable(false);
    		container.setVisible(true);
    
    
            // if user closes window, shutdown game and jre
    		container.addWindowListener(new WindowAdapter() {
    			public void windowClosing(WindowEvent e) {
    				System.exit(0);
    			} // windowClosing
    		});
    
    		// add key listener to this canvas
    		addKeyListener(new KeyInputHandler());
    
    		// request focus so key events are handled by this canvas
    		requestFocus();

    		// create buffer strategy to take advantage of accelerated graphics
    		createBufferStrategy(2);
    		strategy = getBufferStrategy();
    
    		// initialize entities
    		initEntities();
    
    		// start the game
    		gameLoop();
        } // constructor
    
    
        /* initEntities
         * input: none
         * output: none
         * purpose: Initialize the starting state of the hook entities.
         *          Each entity will be added to the array of entities in the game.
    	 */
    	private void initEntities() {
    		hook = new LineEntity(this, "sprites/hook.png", 300, 250);
            entities.add(hook);
            hook2 = new LineEntity(this, "sprites/hook.png", 1200, 250);
            entities.add(hook2);
            
    	} // initEntities

         /* Remove an entity from the game.  It will no longer be
          * moved or drawn.
          */
         public void removeEntity(Entity entity) {
           removeEntities.add(entity);
         } // removeEntity

         /* Notification that a player has died.
          */
         public void notifyDeath() {
           if (twoP) {
        	   if (lives == 0) {
        		   message = "Player 1 lost all their lives. PLAYER 2 WINS!" ;
        	   } else if (lives2 == 0) {
        		   message = "Player 2 lost all their lives. PLAYER 1 WINS!" ;
        	   } // else if
           } else {
        	   if (lives == 0) {
        		   message = "You lost all your lives to the sharks. Try again?";
        	   } else {
        		   message = "You didn't catch enough fish. Try again?";
        	   } // else (inner)
           } // else (outer)
        	
           waitingForKeyPress = true;
         } // notifyDeath


         // Notification that the game has ended and display who wins
         public void notifyWin(){
        	 message = "Player 1 had a total of " + score + " points, and Player 2 had a total of " + score2 + " points. ";
           if (score > score2) {
        	   message += "PLAYER 1 WINS!";
           } else if (score < score2) {
        	   message += "PLAYER 2 WINS!";
           } else {
        	   message += "IT'S A TIE!";
           } // else
           waitingForKeyPress = true;
         } // notifyWin
         
         // Notification that the 1 player game has ended and displays the end score
         public void notifyWin1() {
        	 message += "Player 1 had a total of " + score + " points. CONGRADULATIONS YOU WON!";
        	 waitingForKeyPress = true;
         } // notifyWin1
         
         // replace the hook with the fish caught on the line
         public void moveFish(double x, String fileName) {
      	   String s = "";
		   if (fileName.contains("blue")) {
			   s = "sprites/ublueFish.png";
	   		} else if (fileName.contains("brown")) {
	   			s = "sprites/ubrownFish.png";
	   		} else if (fileName.contains("grey")) { 
	   			s = "sprites/ugreyFish.png";
	   		} else if (fileName.contains("garbage")) {
	   			s = "sprites/garbage1.png";
	   		} // else if (inner)
		   
		   if (x == 300) {
      		 hook = new LineEntity(this, s, 300, hook.getY());
      		 entities.add(hook);
             hook.setVerticalMovement(-100);
      	   } else if (x == 1200) {
    		 hook2 = new LineEntity(this, s, 1200, hook2.getY());
      		 entities.add(hook2);
             hook2.setVerticalMovement(-100);
           } // else if (inner)
           
         } // else if (outer)
         
         // remove the fish on the line when it has been reeled up
         public void fishCaught(double x, String fileName) {
        	if (x == 300) {
        		hook = new LineEntity(this, "sprites/hook.png", 300, 250);
        		entities.add(hook);
        		if (fileName.contains("blue")) {
        			score += 5;
        		} else if (fileName.contains("brown")) {
        			score += 2;
        		} else if (fileName.contains("grey")) { 
        			score++;
        		} else if (fileName.contains("garbage")) {
        			score -= 3;
        		} // else if (inner)
        	} else if (x == 1200) {
        		hook2 = new LineEntity(this, "sprites/hook.png", 1200, 250);
        		entities.add(hook2);
        		if (fileName.contains("blue")) {
        			score2 += 5;
        		} else if (fileName.contains("brown")) {
        			score2 += 2;
        		} else if (fileName.contains("grey")) { 
        			score2++;
        		} else if (fileName.contains("garbage")) {
        			score2 -= 3;
        		} // else if (inner)
        	} // else if (outer)
            
         } // fishCaught

		 // player looses a life if they hit a shark
         public void looseLife(double x) {
        	if (x == 300) {
         		lives --;
         	} else if (x == 1200) {
         		lives2 --;
         	} // else if
         } // looseLife
         
         // returns boolean waitingForKeyPress
         public boolean getWaitingForKeyPress() {
        	 return waitingForKeyPress;
         } // getWaitingForKeyPress
  

	/*
	 * gameLoop
         * input: none
         * output: none
         * purpose: Main game loop. Runs throughout game play.
         *          Responsible for the following activities:
	 *           - calculates speed of the game loop to update moves
	 *           - moves the game entities
	 *           - draws the screen contents (entities, text)
	 *           - updates game events
	 *           - checks input
	 */
	public void gameLoop() {
          long lastLoopTime = System.currentTimeMillis();

          // keep loop running until game ends
          while (gameRunning) {
        	 
            // calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // get graphics context for the accelerated surface and make it blue
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            
			// draw background
            g.setColor(new Color(0, 25, 102));
            g.fillRect(0,0, length, 1000);
            
            g.setColor(new Color(2, 39, 153));
            g.fillRect(0,0, length, 850);
            
            g.setColor(new Color(0, 51, 204));
            g.fillRect(0,0, length, 700);
            
            g.setColor(new Color(0, 63, 255));
            g.fillRect(0,0, length, 500);
            
            g.setColor(Color.white);
            g.fillRect(0, 0, length, 250);
            
            g.setColor(new Color(186, 230, 229));
            g.fillRect(0, 0, length, 242);
            
            // show score
            g.setColor(Color.black);
            g.fillRect(400, 100, 100, 100);
            g.fillRect(1000, 100, 100, 100);
            g.setColor(Color.white);
            g.drawString(Integer.toString(score), 444, 155);
            g.drawString(Integer.toString(score2), 1044, 155);
            
            // show time remaining
            g.setColor(Color.black);
            g.drawString(Integer.toString(60 - (int)(currentT - startT) / 1000), 750, 155);
            
            // show lives
            URL url = Game.class.getResource("sprites/heart.png");
            Image b = Toolkit.getDefaultToolkit().getImage(url);
            if (lives >= 1) {
            	g.drawImage(b, 400, 210, null);
            } // if
            if (lives >= 2) {
            	g.drawImage(b, 435, 210, null);
            } // if
            if (lives >= 3) {
            	g.drawImage(b, 470, 210, null);
            } // if
            
            if (lives2 >= 1) {
            	g.drawImage(b, 1000, 210, null);
            } // if
            if (lives2 >= 2) {
            	g.drawImage(b, 1035, 210, null);
            } // if
            if (lives2 >= 3) {
            	g.drawImage(b, 1070, 210, null);
            } // if
            
            // draws the two penguins
            URL p1 = Game.class.getResource("sprites/penguin.png");
            Image c = Toolkit.getDefaultToolkit().getImage(p1);
            g.drawImage(c, 1212, 165, null);
            	
            URL p2 = Game.class.getResource("sprites/-penguin.png");
            Image d = Toolkit.getDefaultToolkit().getImage(p2);
            g.drawImage(d, 248, 165, null);

            // draw the lines
            g.setColor(Color.white);
            g.fillRect(hook.getX() + 13, 190, 2, hook.getY() -184);
            g.fillRect(hook2.getX() + 13, 190, 2, hook2.getY() -184);

            // what to do while a game round is being played
            if (!waitingForKeyPress) {
              
              // spawn fish
              if (counter % 10 == 0) {
            	Entity fish = new FishEntity(this);
          	    entities.add(fish);
              } // if
              
              // spawn bubbles
              if (counter % 30 == 0) {
            	  Entity bubbles = new BubblesEntity(this, BubblesEntity.randomX());
          	      entities.add(bubbles);
              } // if
              
              // move fish and lines
              for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.move(delta);
              } // for
              
              // set the time the game round starts
              if (counter == 0) {
            	  startT = System.currentTimeMillis();
              } // if
              
              // set the current time and increase counter
              currentT = System.currentTimeMillis();
              counter++;
              
              // check to see if the game should end
              if (lives == 0 || lives2 == 0) {
            	  notifyDeath();
              }
              if ((startT + 60000) <= currentT) {
            	  if (twoP) {
            		  notifyWin();
            	  } else {
		        	  if (score >= 10) {
		        		  notifyWin1();
		        	  } else {
		        		  notifyDeath();
		        	  } // else
            	  } // else
              } // if 
              
            } // if

            // draw all entities
            for (int i = 0; i < entities.size(); i++) {
               Entity entity = (Entity) entities.get(i);
               entity.draw(g);
            } // for

            // brute force collisions, compare every entity
            // against every other entity.  If any collisions
            // are detected notify both entities that it has
            // occurred
           for (int i = 0; i < entities.size(); i++) {
             for (int j = i + 1; j < entities.size(); j++) {
                Entity me = (Entity)entities.get(i);
                Entity him = (Entity)entities.get(j);

                if (me.collidesWith(him)) {
                  me.collidedWith(him);
                  him.collidedWith(me);
                } // if
                if (me.closeBy(him)) {
                    me.closeByed(him);
                    him.closeByed(me);
                } // if
             } // inner for
           } // outer for

           // remove dead entities
           entities.removeAll(removeEntities);
           removeEntities.clear();

           // if waiting for enter to be pressed, draw message
           if (waitingForKeyPress) {
        	   
        	   // display all the instructions in the menu if a menu should be shown
        	   if (menu) {
	           	g.setColor(Color.blue);
	           	g.fillRect(0, 0, length, width);
	           	g.setColor(Color.white);
	           	g.drawString("Welcome to Commadore 64 Ice Fishing!", (length - g.getFontMetrics().stringWidth("Welcome to Commadore 64 Ice Fishing!"))/2, 100);
	           	g.drawString("Catch as many fish as you can by lowering the hook. The person that catched the most fish wins!", (length - g.getFontMetrics().stringWidth("Catch as many fish as you can by lowering the hook. The person that catched the most fish wins!"))/2, 200);
	           	g.drawString("Each player starts with 3 lives. If a player hits a shark, they loose a life. If all lives are lost for either player the game ends.", (length - g.getFontMetrics().stringWidth("Each player starts with 3 lives. If a player hits a shark, they loose a life. If all lives are lost for either player the game ends."))/2, 225);
	           	g.drawString("The game can be played with one player. To win a one player game you have at least 10 points at the end of the game.", (length - g.getFontMetrics().stringWidth("The game can be played with one player. To win a one player game you have at least 10 points at the end of the game."))/2, 250);
	           	g.drawString("Points:", (length - g.getFontMetrics().stringWidth("Points:"))/2, 300);
	           	g.drawString("Grey Fish: 1 point", (length - g.getFontMetrics().stringWidth("Grey Fish: 1 point"))/2, 325);
	           	g.drawString("Brown Fish: 2 points", (length - g.getFontMetrics().stringWidth("Brown Fish: 2 points"))/2, 350);
	           	g.drawString("Blue Fish: 5 points", (length - g.getFontMetrics().stringWidth("Blue Fish: 5 points"))/2, 375);
	           	g.drawString("Garbage: -3 points", (length - g.getFontMetrics().stringWidth("Garbage: -3 points"))/2, 400);
	           	g.drawString("Player 1 controls: Space Bar", 100, 600);
	           	g.drawString("Player 2 controls: Down Arrow", 1150, 600);
	         } // if
        	   
        	 if (!menu) {
        		 g.setColor(new Color(0, 51, 204));
                 g.fillRect(0, 250, length , 750);
        	 }
        	   
             g.setColor(Color.white);
             g.drawString(message, (length - g.getFontMetrics().stringWidth(message))/2, 500);
             g.drawString("Press Enter", (length - g.getFontMetrics().stringWidth("Press Enter"))/2, 600);
             
           }  // if

            // clear graphics and flip buffer
            g.dispose();
            strategy.show();

           

            // respond to user moving their line
            if (spacePressed) {
              hook.setVerticalMovement(100);
            } // if
            
            if (downPressed) {
              hook2.setVerticalMovement(100);
            } // if
            
            // pause
            try { Thread.sleep(50); } catch (Exception e) {}

          } // while

	} // gameLoop


        /* startGame
         * input: none
         * output: none
         * purpose: start a fresh game, clear old data
         */
         private void startGame() {
        	 
        	 
        	 // clear out any existing entities and initialize a new set
            entities.clear();
            
            initEntities();
            
            // reset variables
            spacePressed = false;
            downPressed = false;
            startPressed = false;
            twoP = false;
            counter = 0;
            score = 0;
            score2 = 0;
            startT = 0;
            currentT = 0;
            lives = 3;
            lives2 = 3;
            message = "";
            
         } // startGame


        /* inner class KeyInputHandler
         * handles keyboard input from the user
         */
	private class KeyInputHandler extends KeyAdapter {
                 
                 /* The following methods are required
                 * for any class that extends the abstract
                 * class KeyAdapter.  They handle keyPressed,
                 * keyReleased and keyTyped events.
                 */
		public void keyPressed(KeyEvent e) {

                  // if waiting for keypress to start game, 
				  // check to see if enter has been pressed, and otherwise do nothing
                  if (waitingForKeyPress) {
                	  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                		  startPressed = true;
                	  } // if
                    return;
                  } // if
                  
                  // respond to move down for both p1 and p2
                  if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    spacePressed = true;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                	downPressed = true;
                	twoP = true;
                  } // if

		} // keyPressed

		public void keyReleased(KeyEvent e) {
			
			  // if waiting for keypress to start game, 
			  // check to see if enter has been pressed, and otherwise do nothing
                  if (waitingForKeyPress) {
                	  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                		  startPressed = false;
                	  } // if
                    return;
                  } // if
                  
                  // respond to move left, right or fire
                  if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    spacePressed = false;
                    hook.setVerticalMovement(-100);
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                  	downPressed = false;
                  	hook2.setVerticalMovement(-100);
                  } // if
                  

		} // keyReleased

 	        public void keyTyped(KeyEvent e) {

               // if waiting for key press to start game
 	           if (waitingForKeyPress) {
 	        	   if (startPressed) {
 	        		   menu = false;
                       waitingForKeyPress = false;
                       startGame();
                     } else {
                     } // else
                   } // if waitingForKeyPress

                   // if escape is pressed, end game
                   if (e.getKeyChar() == 27) {
                     System.exit(0);
                   } // if escape pressed

		} // keyTyped

	} // class KeyInputHandler


	/**
	 * Main Program
	 */
	public static void main(String [] args) {
        // instantiate this object
		new Game();
	} // main
} // Game