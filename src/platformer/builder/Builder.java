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
				
				new Ground(services);
				new Player(services);
				new Goal(services);
				
				
				// *** Build main components and start game ***
				buildMainComponents(services);
			}
		});
	}

	
}
