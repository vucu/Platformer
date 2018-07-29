package platformer.gameobject.playscene;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import platformer.gameobject.GameObject;
import platformer.gameobject.properties.IDrawable;
import platformer.services.CameraDrawingService;
import platformer.services.ImageService;
import platformer.services.ScreenDrawingService;

public class Background extends GameObject implements IDrawable {
	private final Rectangle display;
	private final BufferedImage image;
	private final Rectangle world;

	public Background(ImageService imageService, Rectangle display, Rectangle world,
			CameraDrawingService drawingService) {
		this.display = display;
		drawingService.drawOnAllCameras(this);
		this.image = imageService.getImage("background.png");
		this.world = world;
	}

	@Override
	public int getDepth() {
		return 1000;
	}

	@Override
	public void onDraw(Graphics g) {
		// Draw the background repeatly across the game world
		int imageW = image.getWidth();
		int imageH = image.getHeight();

		// Calculate the number of backgrounds across width and height
		int rowCount = world.height / imageH + 1;
		int columnCount = world.width / imageW + 1;

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				g.drawImage(image, imageW * j, imageH * i, imageW, imageH, null);
			}
		}
	}
}
