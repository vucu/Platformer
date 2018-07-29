package platformer.gameobject.playscene;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import platformer.builder.ServicePack;
import platformer.datastructures.Position;
import platformer.datastructures.Vector;
import platformer.gameobject.GameObject;
import platformer.gameobject.properties.*;
import platformer.services.*;

class BouncingBomb extends GameObject implements IDrawable, ITransform, IUpdatable, ICollider, IDestructible {
	int player;
	double xSpeed;
	double ySpeed;
	CollisionService collisionService;
	Image360Service imageService;

	public BouncingBomb(CameraDrawingService camera, CollisionService collisionService, UpdateService updateService,
			Image360Service imageService, int player, Position initialPosition, Vector initialDirection) {
		camera.drawOnAllCameras(this);
		collisionService.register(this);
		updateService.register(this);
		this.imageService = imageService;
		imageService.register(this, "bouncing_bomb_%d.png");
		this.player = player;
		this.setPosition(initialPosition);
		this.collisionService = collisionService;
		xSpeed = initialDirection.getX();
		ySpeed = initialDirection.getY();
	}

	int maskSize = 20;
	@Override
	public Rectangle getCollisionMask() {
		Position position = this.getPosition();
		return new Rectangle(position.x - maskSize / 2, position.y - maskSize / 2, maskSize, maskSize);
	}

	private int bouncingCooldown = 0;

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
			if (bouncingCooldown <= 0) {
				int d = maskSize * 3 / 4;

				// Check to determine the next direction
				int x = (int) this.x;
				int y = (int) this.y;
				boolean left = collisionService.checkCollisionAtPoint(new Point(x - d, y), Wall.class);
				boolean right = collisionService.checkCollisionAtPoint(new Point(x + d, y), Wall.class);
				boolean up = collisionService.checkCollisionAtPoint(new Point(x, y - d), Wall.class);
				boolean down = collisionService.checkCollisionAtPoint(new Point(x, y + d), Wall.class);

				xSpeed = Math.abs(xSpeed);

				if (up || down) {
					ySpeed = Math.abs(ySpeed);

					if (up)
						ySpeed = ySpeed;
					else if (down)
						ySpeed = -ySpeed;
				}

				if (left || right) {
					xSpeed = Math.abs(xSpeed);

					if (left)
						xSpeed = xSpeed;
					else if (right)
						xSpeed = -xSpeed;
				}

				bouncingCooldown = 3;
			}
		}
	}

	@Override
	public void onUpdateBegin() {
		if (bouncingCooldown > 0) {

			bouncingCooldown--;
		}
	}

	@Override
	public void onUpdate() {
		// Make it move
		x += xSpeed;
		y += ySpeed;
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
		Vector direction = new Vector(xSpeed, ySpeed, 1);
		BufferedImage image = imageService.getImage(this, direction.angle);
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