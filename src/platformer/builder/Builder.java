package platformer.builder;

import java.awt.EventQueue;
import java.awt.Rectangle;

import platformer.datastructures.*;
import platformer.gameobject.properties.*;
import platformer.maincomponents.*;
import platformer.services.*;
import platformer.services.delegates.Camera;

public class Builder {
	private static MainFrame frame;
	private static Drawer drawer;
	private static Updater updater;

	private static void buildMainComponents(Services sb) {
		// Dispose old components if they exist
		if (drawer != null) {
			drawer.dispose();
		}

		if (updater != null) {
			updater.dispose();
		}

		// Create new main components
		drawer = new Drawer(sb.cameraDrawingService, sb.keyboardService, sb.display, sb.screenDrawingService);
		updater = new Updater(sb.animationService, sb.cameraDrawingService, sb.collisionService, sb.gridMapService,
				sb.gameSpeed, sb.sceneManagerService, sb.screenDrawingService, sb.updateService);

		// Update the main frame
		if (frame == null) {
			frame = new MainFrame(drawer, sb.display);
		} else {
			frame.replaceMainPanel(drawer);
		}
	}

	public static void buildLevel1() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				// *** Build main components and start game ***
				// buildMainComponents(sb);
			}
		});
	}

	
}
