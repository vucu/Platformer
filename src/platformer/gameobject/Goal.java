package platformer.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.properties.ICollider;
import platformer.gameobject.properties.IDrawable;

public class Goal extends GameObject implements ICollider, IDrawable {
	final int x;
	final int y;
	
	private final BufferedImage image;
	
	public Goal(Services services) {
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		
		// Put it at the end of the world
		Rectangle world = services.world;
		x = world.width - 300;
		y = 0;
		
		this.image = services.imageService.getImage("goal.png");
	}
	
	@Override
	public Position getPosition() {
		return new Position(this.x, this.y);
	}

	@Override
	public Shape getCollisionMask(Position at) {
		return new Rectangle(at.x, at.y, image.getWidth(), this.image.getHeight());
	}

	@Override
	public void onCollision(ICollider other) {
		if (this.isDestroyed()) return;
		
		// If collide with player, make the player win
		if (other instanceof Player) {
			Player thePlayer = (Player) other;
			thePlayer.win();
			this.destroy();
		}
	}

	@Override
	public int getDepth() {
		return -1;
	}

	@Override
	public void onDraw(Graphics g) {
		Rectangle mask = (Rectangle) this.getCollisionMask(this.getPosition());
		g.drawImage(image, mask.x, mask.y, mask.width, mask.height, null);
	}

}
