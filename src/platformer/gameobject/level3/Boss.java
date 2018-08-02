package platformer.gameobject.level3;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import platformer.builder.Services;
import platformer.datastructures.Position;
import platformer.gameobject.GameObject;
import platformer.gameobject.Player;
import platformer.gameobject.properties.*;

public class Boss extends GameObject 
	implements ITransform, IUpdatable, IDrawable {

	private final Services services;
	
	private final BufferedImage image;
	private final Player thePlayer;
	
	private final int maxCooldown = 75;
	private int cooldown = 0;
	
	int x;
	int y;
	public Boss(Services services, Player player) {
		services.cameraDrawingService.drawOnAllCameras(this);
		services.updateService.register(this);
		
		this.image = services.imageService.getImage("boss.png");
		
		this.thePlayer = player;
		
		this.services = services;
	}

	@Override
	public Position getPosition() {
		return new Position(this.x, this.y);		
	}

	@Override
	public int getDepth() {
		// Draw it at a low depth, so it appears above
		// other objects
		return -5;
	}

	@Override
	public void onDraw(Graphics g) {
		Position position = this.getPosition();
		g.drawImage(image, position.x, position.y, image.getWidth(), image.getHeight(), null);
	}

	@Override
	public void onUpdateBegin() {
		// TODO Auto-generated method stub
		
	}

	
	double parameter = 0;
	@Override
	public void onUpdate() {
		parameter += 0.05;
		double factor = Math.cos(parameter);
		
		// Keep same x distance from the player
		Rectangle display = this.services.display;
		this.x = thePlayer.getPosition().x + display.width - image.getWidth() * 2 + (int) (10 * factor);
		
		// Move up and down
		this.y = display.height/2 - image.getHeight()/2 + (int) ((display.height/3) * factor);
	
		// Make bullets
		if (cooldown<0) {
			new Bullet(this.services, this.getPosition());
			cooldown = maxCooldown;
		}
		else {
			cooldown--;
		}
	}

	@Override
	public void onUpdateEnd() {
		// TODO Auto-generated method stub
		
	}
	
}
