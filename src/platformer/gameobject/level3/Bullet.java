package platformer.gameobject.level3;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.Player;
import platformer.gameobject.properties.*;

public class Bullet extends GameObject 
	implements IDrawable, ICollider, IUpdatable {
	private final BufferedImage image;
	
	double x;
	double y;
	final double xSpeed;
	final double ySpeed;
	public Bullet(Services services, Position initial) {
		services.cameraDrawingService.drawOnAllCameras(this);
		services.collisionService.register(this);
		services.updateService.register(this);
		
		this.x = initial.x;
		this.y = initial.y;
		this.xSpeed = -6 + (Math.random() * 2);
		this.ySpeed = -1 + (Math.random() * 2);
		
		this.image = services.imageService.getImage("bullet.png");
	}

	@Override
	public Position getPosition() {
		return new Position((int) this.x, (int) this.y);
	}

	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		// Update x and y based on speed
		this.x += this.xSpeed;
		this.y += this.ySpeed;
	}

	@Override
	public void onUpdateEnd() {
		
	}

	@Override
	public Shape getCollisionMask(Position at) {
		return new Rectangle(at.x, at.y, image.getWidth(), image.getHeight());
	}

	@Override
	public void onCollision(ICollider other) {
		// If the other collided object is a player
		if (other instanceof Player) {
			Player thePlayer = (Player) other;
			
			// make the player die
			thePlayer.hurt();
		}
	}

	@Override
	public int getDepth() {
		// Draw it at a low depth, so it appears above
		// other objects
		return -10;
	}

	@Override
	public void onDraw(Graphics g) {
		Rectangle mask = (Rectangle) this.getCollisionMask(this.getPosition());
		g.drawImage(image, mask.x, mask.y, mask.width, mask.height, null);
	}
}
