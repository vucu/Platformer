package platformer.gameobject.level1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.properties.ICollider;
import platformer.gameobject.properties.IDrawable;
import platformer.gameobject.properties.ITransform;

public class Ground extends GameObject implements ICollider, ITransform, IDrawable {
	private final Rectangle world;
	
	public Ground(Services services) {
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		
		this.world = services.world;
	}
	
	
	@Override
	public Position getPosition() {
		int x = 0;
		int y = world.height - 200;
		return new Position(x, y);
	}

	@Override
	public Shape getCollisionMask() {
		Position position = getPosition();
		return new Rectangle(position.x, position.y, world.width, 200);
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
		Rectangle mask = (Rectangle) this.getCollisionMask();
		g.setColor(Color.PINK);
		g.fillRect(mask.x, mask.y, mask.width, mask.height);
	}

}
