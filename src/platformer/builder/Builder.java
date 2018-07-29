package platformer.builder;

import java.awt.EventQueue;
import java.awt.Rectangle;

import platformer.datastructures.*;
import platformer.gameobject.playscene.*;
import platformer.gameobject.properties.*;
import platformer.gameobject.titlescene.*;
import platformer.maincomponents.*;
import platformer.services.*;
import platformer.services.delegates.Camera;

public class Builder {
	private static MainFrame frame;
	private static Drawer drawer;
	private static Updater updater;

	private static void buildMainComponents(ServiceBuilder sb) {
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

	public static void buildTitleScene() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServiceBuilder sb = new ServiceBuilder(ServiceBuilder.TITLE_SCENE);
				ServicePack.sb = sb;

				// *** Build the game objects ***
				new Loader(sb.soundService);
				new TitleController(sb.keyboardService, sb.sceneManagerService, sb.updateService);
				new TitleBackground(sb.imageService, sb.display, sb.screenDrawingService);

				// *** Build main components and start game ***
				buildMainComponents(sb);
			}
		});
	}

	public static void buildPlayScene() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServiceBuilder sb = new ServiceBuilder(ServiceBuilder.PLAY_SCENE);
				ServicePack.sb = sb;

				// *** Build the game objects ***
				new Background(sb.imageService, sb.display, sb.world, sb.cameraDrawingService);
				new MusicPlayer(sb.soundService, sb.updateService);
				
				// Create score
				Score score0 = new Score(sb.cameraDrawingService, 0);
				Score score1 = new Score(sb.cameraDrawingService, 1);

				// Create 2 tanks
				Tank tank0 = new Tank(sb.cameraDrawingService, sb.collisionService, sb.imageService, sb.image360Service, 0,
						sb.keyboardService, score1, sb.tank0Position, sb.updateService);
				Tank tank1 = new Tank(sb.cameraDrawingService, sb.collisionService, sb.imageService, sb.image360Service, 1,
						sb.keyboardService, score0, sb.tank1Position, sb.updateService);

				tank0.setEnemy(tank1);
				tank1.setEnemy(tank0);

				// Randomly generate walls and powerups
				for (int i = 0; i < sb.gridMapService.getRowCount(); i++) {
					for (int j = 0; j < sb.gridMapService.getColumnCount(); j++) {
						int state = sb.gridMapService.getState(i, j);
						boolean hasWall = (state == GridMapService.UNBREAKABLE_WALL
								|| state == GridMapService.BREAKABLE_WALL);
						boolean breakable = (state == GridMapService.BREAKABLE_WALL);
						if (hasWall) {
							ITransform wall = new Wall(breakable, sb.cameraDrawingService, sb.collisionService,
									sb.gridMapService, sb.imageService);
							int y = i * sb.gridSize + sb.gridSize / 2;
							int x = j * sb.gridSize + sb.gridSize / 2;
							wall.setPosition(new Position(x, y));
						}
					}
				}

				// Create a powerup
				new Powerup(sb.cameraDrawingService, sb.collisionService, sb.imageService, sb.gridSize, sb.world);

				// *** Build main components and start game ***
				buildMainComponents(sb);
			}
		});
	}
}
