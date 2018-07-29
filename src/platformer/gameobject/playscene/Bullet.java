package platformer.gameobject.playscene;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import platformer.builder.ServicePack;
import platformer.datastructures.Position;
import platformer.datastructures.Vector;
import platformer.gameobject.GameObject;
import platformer.gameobject.properties.*;
import platformer.services.*;

public class Bullet extends GameObject implements IDrawable, ITransform, IUpdatable, ICollider, IDestructible {
	BufferedImage image;
	int player;
	Vector flyingDirection;

	public Bullet(CameraDrawingService camera, CollisionService collisionService, UpdateService updateService,
			ImageService imageService, int player, Position initialPosition, Vector flyingDirection) {
		camera.drawOnAllCameras(this);
		collisionService.register(this);
		updateService.register(this);
		this.image = imageService.getImage("bullet.png");
		this.player = player;
		this.setPosition(initialPosition);
		this.flyingDirection = flyingDirection.getNewVectorWithLength(8);
	}

	@Override
	public Rectangle getCollisionMask() {
		int maskSize = 26;

		Position position = this.getPosition();
		return new Rectangle(position.x - maskSize / 2, position.y - maskSize / 2, maskSize, maskSize);
	}

	@Override
	public void onCollision(ICollider other) {
		if (other instanceof Tank) {
			Tank target = (Tank) other;

			if (target.getPlayer() != this.player) {
				target.damage();
				this.destroy();
			}
		}
		if (other instanceof Wall) {
			Wall target = (Wall) other;
			if (target.isBreakable()) {
				target.destroy();
			}

			this.destroy();
		}
	}

	@Override
	public void onUpdateBegin() {
		// Do nothing
	}

	@Override
	public void onUpdate() {
		// Make it move
		x += flyingDirection.getX();
		y += flyingDirection.getY();
	}

	@Override
	public void onUpdateEnd() {
		// Do nothing
	}

	@Override
	public int getDepth() {
		return -10;
	}

	@Override
	public void onDraw(Graphics g) {
		Position position = getPosition();
		g.drawImage(image, position.x - image.getWidth() / 2, position.y - image.getHeight() / 2, null);
	}

	private double x;
	private double y;

	@Override
	public Position getPosition() {
		return new Position((int) x, (int) y);
	}

	@Override
	public void setPosition(Position position) {
		this.x = position.x;
		this.y = position.y;
	}

	private boolean destroyed = false;

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void destroy() {
		destroyed = true;

		// Create a small explosion
		new SmallExplosion(ServicePack.services().animationService, ServicePack.services().cameraDrawingService,
				this.getPosition());
	}

}
