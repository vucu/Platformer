package platformer.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JOptionPane;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.properties.ICollider;
import platformer.gameobject.properties.IDrawable;

public class Goal extends GameObject implements ICollider, IDrawable {
	int x;
	int y;
	int w;
	int h;
	
	public Goal(Services services) {
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		
		// Put it at the end of the world
		Rectangle world = services.world;
		x = world.width - 300;
		y = 0;
		w = 32;
		h = world.height;
	}
	
	@Override
	public Position getPosition() {
		return new Position(this.x, this.y);
	}

	@Override
	public Shape getCollisionMask(Position at) {
		return new Rectangle(at.x, at.y, this.w, this.h);
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
		g.setColor(Color.YELLOW);
		g.fillRect(mask.x, mask.y, mask.width, mask.height);
	}

}
