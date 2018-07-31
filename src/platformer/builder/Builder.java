package platformer.builder;

import java.awt.EventQueue;

import platformer.gameobject.level1.*;
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
				services.gridMapService,
				services.gameSpeed, 
				services.sceneManagerService, 
				services.screenDrawingService, 
				services.updateService);

		// Update the main frame
		if (frame == null) {
			frame = new MainFrame(drawer, services.display);
		} else {
			frame.replaceMainPanel(drawer);
		}
	}

	public static void buildLevel1() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Services services = new Services(Services.LEVEL_1);
				
				
				// Make the game objects
				Player player = new Player(services);
				new Goal(services);
				new Background(services);
				
				new Obstacle(services, player);
				
				// Span multiple grounds
				int x = 0;
				int w = 0;
				while (x < services.world.width) {
					// Make a random width
					w = (int) (Math.random() * 800);
					
					new Ground(services, x, w);
					x = x + w;
					
					// Make a random gap
					int gap = (int) (Math.random() * 100);
					x = x + gap;
				}
				
				// *** Build main components and start game ***
				buildMainComponents(services);
			}
		});
	}

	
}
