package platformer.builder;

import java.awt.Rectangle;

import javax.management.RuntimeErrorException;

import platformer.datastructures.Position;
import platformer.services.*;
import platformer.services.delegates.Camera;
import platformer.services.delegates.Remover;

public class ServiceBuilder {
	public static final int TITLE_SCENE = 0;
	public static final int PLAY_SCENE = 1;

	ServiceBuilder(int settings) {
		switch (settings) {
		case TITLE_SCENE:
			cameras = new Camera[] { new Camera(world, display.width, display.height) };
			break;
		case PLAY_SCENE:
			cameras = new Camera[3];
			cameras[0] = new Camera(world, 0, 0, display.width / 2 - 4, display.height);
			cameras[1] = new Camera(world, display.width / 2 + 4, 0, display.width / 2, display.height);

			double minimapScale = 0.1;
			int minimapW = (int) (world.width * minimapScale);
			int minimapH = (int) (world.height * minimapScale);
			int minimapX = display.width / 2 - minimapW / 2;
			int minimapY = display.height - minimapH - 30;
			cameras[2] = new Camera(world, minimapX, minimapY, world.width, world.height, 0.1);
			break;
		default:
			throw new RuntimeException("Invalid build setting");
		}

		cameraDrawingService = new CameraDrawingService(remover, cameras);
	}

	// The size of the game world
	public final Rectangle world = new Rectangle(0, 0, 1200, 1200);

	// The size of the display
	public final Rectangle display = new Rectangle(0, 0, 800, 480);

	// The size of a grid
	public final int gridSize = 32;

	// The speed of the game
	public final int gameSpeed = 60;

	// The initial position of the tank
	public final Position tank0Position = new Position(100, 100);
	public final Position tank1Position = new Position(world.width - 100, world.height - 100);

	// *** Build delegates ***
	public final Camera[] cameras;
	public final Remover remover = new Remover();

	// *** Build services ***
	public final GridMapService gridMapService = new GridMapService(gridSize, world, remover, tank0Position,
			tank1Position);
	public final CameraDrawingService cameraDrawingService;
	public final ScreenDrawingService screenDrawingService = new ScreenDrawingService(remover);
	public final UpdateService updateService = new UpdateService(remover);
	public final ImageService imageService = new ImageService();
	public final AnimationService animationService = new AnimationService(imageService, gameSpeed, remover);
	public final Image360Service image360Service = new Image360Service(imageService);
	public final CollisionService collisionService = new CollisionService(remover);
	public final KeyboardService keyboardService = new KeyboardService();
	public final SceneManagerService sceneManagerService = new SceneManagerService();
	public final SoundService soundService = new SoundService();
}
