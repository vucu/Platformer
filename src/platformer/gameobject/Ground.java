package platformer.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.properties.ICollider;
import platformer.gameobject.properties.IDrawable;
import platformer.gameobject.properties.ITransform;

public abstract class Ground extends GameObject implements ICollider, ITransform, IDrawable {
	private final BufferedImage image;
	
	public Ground(Services services) {
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		
		this.image = services.imageService.getImage("ground.png");
	}
	
	
	// Make this abstract, so the children decides the position
	public abstract Position getPosition();
	public abstract int getWidth();
	public abstract int getHeight();
	
	@Override
	public Shape getCollisionMask(Position at) {
		return new Rectangle(at.x, at.y, this.getWidth(), this.getHeight());
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
		g.drawImage(image, mask.x, mask.y, mask.width, mask.height, null);
	}

}
