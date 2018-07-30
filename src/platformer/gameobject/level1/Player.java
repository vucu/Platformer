package platformer.gameobject.level1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import com.sun.glass.events.KeyEvent;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.SolidObject;
import platformer.gameobject.properties.*;
import platformer.services.CollisionService;
import platformer.services.KeyboardService;

public class Player extends GameObject implements ICollider, IUpdatable, IDrawable {
	private final Services services; 
	
	private final double grav = 0.2;
	private double hsp = 0;
	private double vsp = 0;
	private final double jumpspeed = 7;
	private final double movespeed = 4;
	
	double x;
	double y;
    
	public Player(Services services) {
		services.updateService.register(this);
		services.collisionService.register(this);
		services.cameraDrawingService.drawOnAllCameras(this);
		services.cameraDrawingService.track(this, 0);
		
		// Put player at the beginning of world
		Rectangle world = services.world;
		x = world.x + 100;
		y = world.y + world.height - 100;
		
		this.services = services;
	}
	
	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		KeyboardService keyboard = this.services.keyboardService;
		CollisionService collision = this.services.collisionService;
		
		// React to input
		double hfactor = 1;
		double vfactor = 0;
		if (keyboard.check(KeyEvent.VK_SPACE)) {
			vfactor = 1;
		}
		
		// Horizontal speed
		hsp = movespeed * hfactor;
		
		// Vertical speed (accelerate by gravity, limit to 10)
		if (vsp<10) vsp+=grav;
		
		// Can jump if there is a solid object below player 
		if (collision.checkCollisionWith(this, 0, 1, SolidObject.class)) {
			vsp = vfactor * -this.jumpspeed;
		}
		
		// Horizontal collision
		if (collision.checkCollisionWith(this, (int) hsp, 0, SolidObject.class)) {
			while (!collision.checkCollisionWith(this, (int) Math.signum(hsp), 0, SolidObject.class)) {
				this.x += Math.signum(hsp);
			}
			hsp = 0;
		}
		this.x += hsp;
		
		// Vertical collision
		if (collision.checkCollisionWith(this, 0, (int) vsp, SolidObject.class)) {
			while (!collision.checkCollisionWith(this, 0, (int) Math.signum(vsp), SolidObject.class)) {
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

}
