package platformer.gameobject.level1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.SolidObject;
import platformer.gameobject.properties.ICollider;
import platformer.gameobject.properties.IDrawable;
import platformer.gameobject.properties.ITransform;

public class Ground extends SolidObject implements ICollider, ITransform, IDrawable {
	private final Rectangle world;
	private final int width;
	private final int x;
	
	public Ground(Services services, int x, int width) {
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		
		this.world = services.world;
		this.width = width;
		this.x = x;
	}
	
	
	@Override
	public Position getPosition() {
		int y = world.height - 50;
		return new Position(this.x, y);
	}

	@Override
	public Shape getCollisionMask(Position at) {
		return new Rectangle(at.x, at.y, this.width, 50);
	}

	@Override
	public void onCollision(ICollider other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public void onDraw(Graphics g) {
		Rectangle mask = (Rectangle) this.getCollisionMask(this.getPosition());
		g.setColor(Color.PINK);
		g.fillRect(mask.x, mask.y, mask.width, mask.height);
	}

}
