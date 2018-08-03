package platformer.builder;

import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

import platformer.datastructures.Level;
import platformer.datastructures.Position;
import platformer.gameobject.Goal;
import platformer.gameobject.HealthBar;
import platformer.gameobject.Music;
import platformer.gameobject.Player;
import platformer.gameobject.Powerup;
import platformer.gameobject.level1.*;
import platformer.gameobject.level2.Background2;
import platformer.gameobject.level2.FlyingMonsterFactory;
import platformer.gameobject.level2.Ground2;
import platformer.gameobject.level3.*;
import platformer.maincomponents.*;

public class Builder {
	private static MainFrame frame;
	private static Drawer drawer;
	private static Updater updater;

	private static void buildMainComponents(Services services) {
		// Dispose old components if they exist
		if (drawer != null) {
			drawer.dispose();
		}

		if (updater != null) {
			updater.dispose();
		}

		// Create new main components
		drawer = new Drawer(services.cameraDrawingService, 
				services.keyboardService, 
				services.display, 
				services.screenDrawingService);
		updater = new Updater(services.animationService, 
				services.cameraDrawingService, 
				services.collisionService, 
				services.gameSpeed, 
				services.screenDrawingService, 
				services.updateService);

		// Update the main frame
		if (frame == null) {
			frame = new MainFrame(drawer, services.display);
		} else {
			frame.replaceMainPanel(drawer);
		}
	}
	
	private static int lives = 3;
	public static void buildLevel1() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Services services = new Services(Level.Level1);
				
				
				// Make music
				new Music(services);
				
				// Make the game objects
				Rectangle world = services.world;
				Position initialPlayerPosition = new Position(world.x + 100, world.y + world.height - 125);
				Player player = new Player(
						services, 
						initialPlayerPosition, 
						Builder::buildLevel2, 
						() -> { lives--; if (lives<=0) lives=3; buildLevel1(); });
				new Goal(services);
				new Background1(services);
				
				new Obstacle(services, player);
				
				new HealthBar(services, player, lives);
				
				// Span multiple grounds
				int x = 0;
				int w = 0;
				while (x < services.world.width) {
					// Make a random width
					w = (int) (Math.random() * 800);
					
					new Ground1(services, x, w);
					x = x + w;
					
					// Make a random gap
					int gap = (int) (Math.random() * 100);
					x = x + gap;
				}
				
				// Make a powerup
				new Powerup(services,player);
				
				// *** Build main components and start game ***
				buildMainComponents(services);
			}
		});
	}

	public static void buildLevel2() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Services services = new Services(Level.Level2);
				
				// Make the game objects
				new Background2(services);
				new Goal(services);
				
				Rectangle world = services.world;
				
				// Span multiple grounds
				Ground2 firstGround = null;
				int x = 0;
				int y = world.height/2;
				int w = 0;
				while (x < world.width) {
					// Make a random width
					w = 50 + (int) (Math.random() * 400);
					
					// Make a random elevation
					y = (int) (y + Math.random() * 200 - 100);
					
					// But make sure the elevation is not too high nor low
					if (y < world.y + 100) {
						y = world.y + 100;
					}
					if (y > world.y + world.height - 100) {
						y = world.y + world.height - 100;
					}
					
					if (firstGround==null) {
						firstGround = new Ground2(services, x, y, w);		
					}
					else {
						new Ground2(services, x, y, w);
					}
					
					x = x + w;
					
					// Make a random gap
					int gap = (int) (Math.random() * 200);
					x = x + gap;
				}
				
				// Place the player on the first ground
				Position p = firstGround.getPosition();
				Position initialPlayerPosition = new Position(p.x + 10, p.y - 125);
				Player player = new Player(
						services, 
						initialPlayerPosition, 
						Builder::buildLevel3, 
						() -> { lives--; if (lives<=0) {lives=3; buildLevel1(); } else buildLevel2(); });
				
				new HealthBar(services, player, lives);
				
				// Create the monster factory
				new FlyingMonsterFactory(services, player);
				
				// Make a powerup
				new Powerup(services,player);
				
				// *** Build main components and start game ***
				buildMainComponents(services);
			}
		});
	}
	
	public static void buildLevel3() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Services services = new Services(Level.Level3);
				
				// Make the game object
				new Background3(services);
				
				// Span multiple grounds
				Rectangle world = services.world;
				Position firstPosition = null;
				int x = 300;
				int y = world.height/2;
				int w = 0;
				while (x < world.width) {
					// Make a random width
					w = 150 + (int) (Math.random() * 25);
					
					// Make a random elevation
					y = (int) (y + Math.random() * 200 - 100);
					
					if (firstPosition == null) {
						firstPosition = new Position(x,y);
					}
					
					// But make sure the elevation is not too high nor low
					if (y < world.y + 200) {
						y = world.y + 200;
					}
					if (y > world.y + world.height - 200) {
						y = world.y + world.height - 200;
					}
					
					
					new PlusShapeGround(services, x, y, w);
					
					
					x = x + w;
					
					// Make a random gap
					int gap = (int) (Math.random() * 100);
					x = x + gap;
				}
				
				// Place the player near the first ground
				Position pos = firstPosition;
				Player player = new Player(
						services, 
						new Position(pos.x - 25, pos.y - 125), 
						() -> { JOptionPane.showMessageDialog(null, "You win the game"); System.exit(0); }, 
						() -> { lives--; if (lives<=0) {lives=3; buildLevel1(); } else buildLevel3(); });
				
				new HealthBar(services, player, lives);
				
				// Make boss
				new Boss(services, player);
				
				// Make a powerup
				new Powerup(services,player);
				
				// Make goal
				new Goal(services);
				
				// *** Build main components and start game ***
				buildMainComponents(services);
			}
		});
	}
}
