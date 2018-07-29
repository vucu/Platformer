package platformer.builder;

import java.awt.Rectangle;

import javax.management.RuntimeErrorException;

import platformer.datastructures.Position;
import platformer.services.*;
import platformer.services.delegates.Camera;
import platformer.services.delegates.Remover;

public class Services {
	static final int LEVEL_1 = 1;

	Services(int settings) {
		switch (settings) {
		case LEVEL_1:
			cameras = new Camera[] { new Camera(world, display.width, display.height) };
			break;
		default:
			throw new RuntimeException("Invalid build setting");
		}

		cameraDrawingService = new CameraDrawingService(remover, cameras);
	}

	// The size of the game world
	public final Rectangle world = new Rectangle(0, 0, 3000, 800);

	// The size of the display
	public final Rectangle display = new Rectangle(0, 0, 1000, 600);

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
