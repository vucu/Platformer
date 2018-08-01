package platformer.gameobject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import platformer.builder.Services;
import platformer.gameobject.properties.IDrawable;

public abstract class Background extends GameObject implements IDrawable {
	private final Rectangle display;
	private final BufferedImage image;
	private final Rectangle world;

	public Background(Services services) {
		this.display = services.display;
		
		// Tell the drawing service to draw this object
		services.cameraDrawingService.drawOnAllCameras(this);
		
		String fname = this.getBackgroundFileName();
		this.image = services.imageService.getImage(fname);
		this.world = services.world;
	}
	
	// Let the children decide the file name
	protected abstract String getBackgroundFileName();
	
	// Higher depth meaning it's lower on the screen
	// If 2 objects are drawn in the same place,
	// the higher depth object is below, the lower depth
	// object is above
	@Override
	public int getDepth() {
		// Very high value so the background is always drawn on bottom
		return 1000;
	}

	// This function instruct how to draw this object
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
