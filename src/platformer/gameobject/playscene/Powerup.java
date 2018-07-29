package platformer.gameobject.playscene;

import java.awt.*;
import java.awt.image.BufferedImage;

import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.properties.*;
import platformer.services.*;

public class Powerup extends GameObject implements ITransform, IDrawable, ICollider, IDestructible {
	private final Rectangle world;
	private final int gridSize;

	private final int ROCKET = 0;
	private final int SHIELD = 1;
	private final int BOMB = 2;
	private final int[] effects = { ROCKET, SHIELD, BOMB };
	private int effect = getRandomEffect();;
	private BufferedImage[] images;

	public Powerup(CameraDrawingService camera, CollisionService collisionService, ImageService imageService,
			int gridSize, Rectangle world) {
		camera.drawOnAllCameras(this);
		collisionService.register(this);
		this.images = new BufferedImage[] { 
				imageService.getImage("powerup_rocket.png"), 
				imageService.getImage("powerup_shield.png"),
				imageService.getImage("powerup_bomb.png") };
		this.gridSize = gridSize;
		this.world = world;
		this.position = getRandomPosition(world, gridSize);
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
			Tank target = (Tank) other;

			switch (effect) {
			case ROCKET:
				target.armRocket();
				break;
			case SHIELD:
				target.armShield();
				break;
			case BOMB:
				target.armBomb();
				break;
			}

			// Jump to a random place on the map
			effect = getRandomEffect();
			this.setPosition(getRandomPosition(world, gridSize));
		}
		if (other instanceof Wall) {
			// Collide with a Wall, jump immediately to another position
			effect = getRandomEffect();
			this.setPosition(getRandomPosition(world, gridSize));
		}
	}

	private Position getRandomPosition(Rectangle world, int gridSize) {
		double randX = Math.random() * world.width;
		double randY = Math.random() * world.height;
		int x = (int) randX;
		int y = (int) randY;
		int snapX = (x / gridSize) * gridSize;
		int snapY = (x / gridSize) * gridSize;
		return new Position(snapX, snapY);
	}

	private int getRandomEffect() {
		return (int) (Math.random() * effects.length);
	}

	@Override
	public int getDepth() {
		return 10;
	}

	@Override
	public void onDraw(Graphics g) {
		BufferedImage image = images[effect];
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

	boolean destroyed = false;

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void destroy() {
		destroyed = true;
	}
}
