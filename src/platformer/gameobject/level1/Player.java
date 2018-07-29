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
	private final Services services; 
	
	private final double grav = 0.2;
	private double hsp = 0;
	private double vsp = 0;
	private final double jumpspeed = 7;
	private final double movespeed = 4;
    
	public Player(Services services) {
		services.updateService.register(this);
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		
		this.services = services;
	}
	
	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		this.y += 5;
	}

	@Override
	public void onUpdateEnd() {
		// TODO Auto-generated method stub it is a bit slow 
	}
	
	int x = 200;
	int y = 200;
	@Override
	public Position getPosition() {
		return new Position(x, y);
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
