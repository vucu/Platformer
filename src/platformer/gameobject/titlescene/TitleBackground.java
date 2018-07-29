package platformer.gameobject.titlescene;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import platformer.gameobject.GameObject;
import platformer.gameobject.properties.IDrawable;
import platformer.gameobject.properties.IUpdatable;
import platformer.services.ImageService;
import platformer.services.KeyboardService;
import platformer.services.SceneManagerService;
import platformer.services.ScreenDrawingService;

public class TitleBackground extends GameObject implements IDrawable {
	private final Rectangle display;
	private final BufferedImage image;

	public TitleBackground(ImageService imageService, Rectangle display, ScreenDrawingService drawingService) {
		this.display = display;
		drawingService.register(this);
		this.image = imageService.getImage("title.png");
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public void onDraw(Graphics g) {
		// Scale and center it to the screen
		double wRatio = (double) display.width / image.getWidth();
		double hRatio = (double) display.height / image.getHeight();

		int x;
		int y;
		int w;
		int h;
		if (wRatio > hRatio) {
			x = 0;
			w = display.width;

			h = (int) (image.getHeight() * wRatio);
			y = (display.height - h) / 2;
		} else {
			y = 0;
			h = display.height;

			w = (int) (image.getWidth() * hRatio);
			x = (display.width - w) / 2;
		}

		// Draw the title image to the screen
		g.drawImage(image, x, y, w, h, null);
	}
}
