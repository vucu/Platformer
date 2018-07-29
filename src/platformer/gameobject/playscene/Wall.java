package platformer.gameobject.playscene;

import java.awt.*;
import java.awt.image.BufferedImage;

import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.properties.*;
import platformer.services.CameraDrawingService;
import platformer.services.CollisionService;
import platformer.services.GridMapService;
import platformer.services.ImageService;

public class Wall extends GameObject implements ITransform, IDrawable, ICollider, IDestructible {
	private final boolean breakable;
	private final BufferedImage image;

	public Wall(boolean breakable, CameraDrawingService camera, CollisionService collisionService,
			GridMapService gridMapService, ImageService imageService) {
		camera.drawOnAllCameras(this);
		collisionService.register(this);
		String fname = "";
		this.breakable = breakable;
		if (breakable) {
			fname = "breakable-wall.png";
			gridMapService.register(this, GridMapService.BREAKABLE_WALL);
		} else {
			fname = "unbreakable-wall.png";
			gridMapService.register(this, GridMapService.UNBREAKABLE_WALL);
		}
		image = imageService.getImage(fname);
	}

	@Override
	public Rectangle getCollisionMask() {
		int maskSize = 32;

		Position position = this.getPosition();
		return new Rectangle(position.x - maskSize / 2, position.y - maskSize / 2, maskSize, maskSize);
	}

	@Override
	public void onCollision(ICollider other) {
		if (other instanceof Tank) {
			((Tank) other).revertPosition();
		}
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public void onDraw(Graphics g) {
		g.drawImage(image, position.x - image.getWidth() / 2, position.y - image.getHeight() / 2, null);
	}

	private Position position;

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void setPosition(Position position) {
		this.position = position;
	}

	private boolean destroyed = false;

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void destroy() {
		destroyed = true;
	}

	// Allow other to interact with it
	public boolean isBreakable() {
		return breakable;
	}
}
