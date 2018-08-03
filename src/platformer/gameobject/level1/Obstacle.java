package platformer.gameobject.level1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.Player;
import platformer.gameobject.properties.ICollider;
import platformer.gameobject.properties.IDrawable;
import platformer.gameobject.properties.IUpdatable;

public class Obstacle extends GameObject implements 
	ICollider, IDrawable, IUpdatable {

	private final Player thePlayer;
	private Position position;
	
	private final BufferedImage image;
	
	public Obstacle(Services services, Player player) {
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		services.updateService.register(this);
		
		this.thePlayer = player;
		this.position = new Position(400, 475);
		
		this.image = services.imageService.getImage("obstacle.png");
	}
	 
	// The position of this object
	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public int getDepth() {
		// To make sure the obstacle is drawn above the ground
		return -1;
	}

	@Override
	public void onDraw(Graphics g) {
		Rectangle mask = (Rectangle) this.getCollisionMask(this.getPosition());
		g.drawImage(image, mask.x, mask.y, mask.width, mask.height, null);
	}

	@Override
	public Shape getCollisionMask(Position at) {
		// The collision mask is a rectangle
		return new Rectangle(at.x, at.y, image.getWidth(), image.getHeight());
	}

	// Kill the player on collision
	@Override
	public void onCollision(ICollider other) {
		// If the other collided object is a player
		if (other instanceof Player) {
			Player thePlayer = (Player) other;
			
			// make the player die
			thePlayer.hurt();
		}
	}

	// Update every frame. Called first
	@Override
	public void onUpdateBegin() {
		
	}

	// Update every frame. Called second
	@Override
	public void onUpdate() {
		Position playerPosition = this.thePlayer.getPosition();
		Position myPosition = this.getPosition();
		
		
		if (myPosition.x < playerPosition.x - 400) {
			// Jump ahead
			this.position = new Position(playerPosition.x + 400, 475);
		}
	}

	// Update every frame. Called third
	@Override
	public void onUpdateEnd() {
		
	}

}
