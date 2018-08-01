package platformer.gameobject.level2;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.*;
import platformer.gameobject.properties.*;

// Monster can fly towards the player
public class FlyingMonster extends GameObject
	implements IDrawable, ICollider, IUpdatable {
	
	private final BufferedImage image;
	private final Player thePlayer;
	
	int x;
	int y;
	public FlyingMonster(Services services, Player player) {
		services.cameraDrawingService.drawOnAllCameras(this);
		services.collisionService.register(this);
		services.updateService.register(this);
		
		// The initial position will be off camera
		Rectangle display = services.display;
		Position playerPosition = player.getPosition();
		this.x = playerPosition.x + display.width + 100;
		
		// Initial y is random relative to player y
		this.y = playerPosition.y + (int) (Math.random() * 400 - 200);
		
		this.image = services.imageService.getImage("flying-monster.png");
		
		this.thePlayer = player;
	}

	@Override
	public Position getPosition() {
		return new Position(this.x, this.y);
	}

	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		// Fly towards the player
		Position playerPos = this.thePlayer.getPosition();
		
		this.x -= 5;
		if (this.y<playerPos.y) {
			this.y++;
		}
		else if (this.y>playerPos.y) {
			this.y--;
		}
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
			thePlayer.die();
		}
	}

	@Override
	public int getDepth() {
		// Draw it at a low depth, so it appears above
		// other objects
		return -5;
	}

	@Override
	public void onDraw(Graphics g) {
		Rectangle mask = (Rectangle) this.getCollisionMask(this.getPosition());
		g.drawImage(image, mask.x, mask.y, mask.width, mask.height, null);
	}
}
