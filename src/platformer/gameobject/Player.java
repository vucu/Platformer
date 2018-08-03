package platformer.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JOptionPane;

import com.sun.glass.events.KeyEvent;

import platformer.builder.Services;
import platformer.datastructures.Level;
import platformer.datastructures.Position;
import platformer.gameobject.properties.*;
import platformer.services.CollisionService;
import platformer.services.KeyboardService;

public class Player extends GameObject implements ICollider, IUpdatable, IDrawable {
	private final Services services; 
	private final Runnable win;
	private final Runnable lose;
	
	private final double grav = 0.2;
	private double hsp = 0;
	private double vsp = 0;
	private final double jumpspeed = 7;
	private final double movespeed = 4;
	
	double x;
	double y;
    
	public Player(Services services, Position initialPosition, Runnable win, Runnable lose) {
		services.updateService.register(this);
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		services.cameraDrawingService.track(this, 0);
		
		this.win = win;
		this.lose = lose;
		
		// Put player at the beginning of world
		Rectangle world = services.world;
		x = initialPosition.x;
		y = initialPosition.y;
		
		this.services = services;
	}
	
	@Override
	public void onUpdateBegin() {
		
	}

	@Override
	public void onUpdate() {
		// Decrease invincible duration
		this.invincibleDuration--;
		
		// If the player falls too far below, it will die
		Rectangle world = this.services.world;
		int graceHeight = 30;
		// It checks the player y. If player y is too large
		// meaning the player is well below the world
		// and it will die
		if (this.y > world.y + world.height + graceHeight) {
			this.die();
		}
		
		// React to input
		KeyboardService keyboard = this.services.keyboardService;
		CollisionService collision = this.services.collisionService;
		
		// Make player faster if he is speeding up
		double hfactor = 1;
		if (this.speedUpDuration > 0) {
			hfactor = 1.5;
			
			// Decrease the duration each frame
			this.speedUpDuration--;
		}
		
		double vfactor = 0;
		if (keyboard.check(KeyEvent.VK_SPACE)) {
			vfactor = 1;
		}
		
		// Horizontal speed
		hsp = movespeed * hfactor;
		
		// Vertical speed (accelerate by gravity, limit to 10)
		if (vsp<10) vsp+=grav;
		
		// Can jump if there is a solid object below player 
		if (collision.checkCollisionWith(this, 0, 1, Ground.class)) {
			vsp = vfactor * -this.jumpspeed;
		}
		
		// Horizontal collision
		if (collision.checkCollisionWith(this, (int) hsp, 0, Ground.class)) {
			while (!collision.checkCollisionWith(this, (int) Math.signum(hsp), 0, Ground.class)) {
				this.x += Math.signum(hsp);
			}
			hsp = 0;
		}
		this.x += hsp;
		
		// Vertical collision
		if (collision.checkCollisionWith(this, 0, (int) vsp, Ground.class)) {
			while (!collision.checkCollisionWith(this, 0, (int) Math.signum(vsp), Ground.class)) {
				this.y += Math.signum(vsp);
			}
			vsp = 0;
		}		
		
		this.y = this.y + vsp;
	}

	@Override
	public void onUpdateEnd() {
		
	}
	
	@Override
	public Position getPosition() {
		return new Position((int) x, (int) y);
	}

	@Override
	public Shape getCollisionMask(Position at) {
		return new Rectangle(at.x, at.y, 20, 20);
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
		g.setColor(Color.CYAN);
		g.fillRect(mask.x, mask.y, mask.width, mask.height);
	}
	
	public void hurt() {
		if (!this.isInvincible()) {
			this.die();
		}
	}
	
	public void die() {
		if (!this.isDestroyed()) {
			lose.run();
		}
		this.destroy();
	}
	
	// Call this method to make player win
	public void win() {
		if (!this.isDestroyed()) {
			win.run();
		}
		this.destroy();
	}
	
	// Make player faster
	private int speedUpDuration = 0;
	public void speedUp() {
		// Lasts 150 frames
		speedUpDuration = 150;
	}
	
	// Make player invincible
	private int invincibleDuration = 0;
	public void invincible() {
		invincibleDuration = 150;
	}
	
	private boolean isInvincible() {
		return invincibleDuration>0;
	}
}
