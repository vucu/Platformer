package platformer.builder;

import java.awt.Rectangle;

import javax.management.RuntimeErrorException;

import platformer.datastructures.Level;
import platformer.datastructures.Position;
import platformer.services.*;
import platformer.services.delegates.Camera;
import platformer.services.delegates.Remover;
import platformer.services.delegates.SideScrollingCamera;

public class Services {

	Services(Level level) {
		this.level = level;
		
		switch (level) {
		case Level1:
			cameras = new Camera[] { new SideScrollingCamera(world, display.width, display.height) };
			break;
		case Level2:
			cameras = new Camera[] { new SideScrollingCamera(world, display.width, display.height) };
			break;
		default:
			throw new RuntimeException("Invalid build setting");
		}

		cameraDrawingService = new CameraDrawingService(remover, cameras);
	}

	// The level the player currently at
	public final Level level;
	
	// The size of the game world
	public final Rectangle world = new Rectangle(0, 0, 3000, 600);

	// The size of the display
	public final Rectangle display = new Rectangle(0, 0, 1000, 600);

	// The size of a grid
	public final int gridSize = 32;

	// The speed of the game
	public final int gameSpeed = 30;

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
