package platformer.gameobject.level1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.properties.*;

public class Player extends GameObject implements ICollider, ITransform, IUpdatable, IDrawable {

	public Player(Services services) {
		services.updateService.register(this);
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
	}
	
	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Position getPosition() {
		return new Position(200,200);
	}

	@Override
	public Shape getCollisionMask() {
		Position position = this.getPosition();
		return new Rectangle(position.x, position.y, 20, 20);
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
		g.setColor(Color.CYAN);
		g.fillRect(mask.x, mask.y, mask.width, mask.height);
	}

}
