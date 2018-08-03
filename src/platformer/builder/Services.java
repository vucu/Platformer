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
	}

	// The level the player currently at
	public final Level level;
	
	// The size of the game world
	public final Rectangle world = new Rectangle(0, 0, 3000, 600);

	// The size of the display
	public final Rectangle display = new Rectangle(0, 0, 1000, 600);

	// The speed of the game
	public final int gameSpeed = 30;

	// *** Build delegates ***
	public final Camera[] cameras = new Camera[] { new SideScrollingCamera(world, display.width, display.height) };;
	public final Remover remover = new Remover();

	// *** Build services ***
	public final CameraDrawingService cameraDrawingService = new CameraDrawingService(remover, cameras);;
	public final ScreenDrawingService screenDrawingService = new ScreenDrawingService(remover);
	public final UpdateService updateService = new UpdateService(remover);
	public final ImageService imageService = new ImageService();
	public final AnimationService animationService = new AnimationService(imageService, gameSpeed, remover);
	public final Image360Service image360Service = new Image360Service(imageService);
	public final CollisionService collisionService = new CollisionService(remover);
	public final KeyboardService keyboardService = new KeyboardService();
	public final SoundService soundService = new SoundService();
}
