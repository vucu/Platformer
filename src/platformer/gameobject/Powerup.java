package platformer.gameobject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.properties.*;

public class Powerup extends GameObject
	implements IDrawable, IUpdatable, ICollider {

	private final Services services;
	private final Player thePlayer;
	
	private final BufferedImage image; 
	
	private final double grav = 0.4;
	private double vsp = 0;
	
	
	private double x = 0;
	private double y = 0;
	
	private final PowerupEffect[] effects = 
			new PowerupEffect[] { new SpeedEffect(), new InvincibleEffect() };
	private PowerupEffect effect;
	private int effectNumber = 0;
	
	public Powerup(Services services, Player player) {
		services.cameraDrawingService.drawOnAllCameras(this);
		services.collisionService.register(this);
		services.updateService.register(this);
		
		this.services = services;
		this.thePlayer = player;
		
		this.jump();
		
		this.image = services.imageService.getImage(effect.getImageFileName());
	}
	
	// Make the powerup jump ahead
	private void jump() {
		x = thePlayer.x + services.display.getWidth() - 200;
		y = thePlayer.y - 200;
		
		// Move to next effect
		this.effectNumber = (this.effectNumber + 1) % this.effects.length;
		this.effect = this.effects[effectNumber];
	}
	
	@Override
	public Position getPosition() {
		return new Position((int) x, (int) y);
	}

	@Override
	public Shape getCollisionMask(Position at) {
		return new Rectangle(at.x, at.y, image.getWidth(), image.getHeight());
	}

	@Override
	public void onCollision(ICollider other) {
		// If collide with player, provide effect
		if (other instanceof Player) {
			Player thePlayer = (Player) other;
			
			effect.effect(thePlayer);
			
			this.jump();
		}
		
		// If collide with ground, bounce back
		if (other instanceof Ground) {
			this.vsp = - Math.abs(this.vsp);
			this.vsp -= 4;
		}
	}

	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		// If the player falls too far below, jump
		Rectangle world = this.services.world;
		int graceHeight = 30;
		if (this.y > world.y + world.height + graceHeight) {
			this.jump();
		}
		
		// If the powerup falls behind the player, jump
		int graceDistance = 100;
		if (this.x < this.thePlayer.getPosition().x - graceDistance) {
			this.jump();
		}
		
		// Affect by gravity
		this.vsp += this.grav;
		if (this.vsp>10) this.vsp = 10;
		this.y += this.vsp;
	}

	@Override
	public void onUpdateEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDepth() {
		return 5;
	}

	@Override
	public void onDraw(Graphics g) {
		Rectangle mask = (Rectangle) this.getCollisionMask(this.getPosition());
		g.drawImage(image, mask.x, mask.y, mask.width, mask.height, null);
	}
}
