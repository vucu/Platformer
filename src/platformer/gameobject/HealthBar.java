package platformer.gameobject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import platformer.builder.Services;
import platformer.gameobject.properties.IDrawable;

public class HealthBar implements IDrawable {
	private final Services services;
	private final Player thePlayer;
	private final int lives;
	
	public HealthBar(Services services, 
			Player player,
			int lives) {
		this.services = services;
		this.services.screenDrawingService.register(this);
		
		this.thePlayer = player;
		this.lives = lives;
	}

	@Override
	public int getDepth() {
		return -100;
	}

	final Font font = new Font("Verdana", Font.BOLD, 20);
	@Override
	public void onDraw(Graphics g) {
		// Draw the health bar
		int x = 25;
		int y = 25;
		int w = 300;
		int h = 20;

		g.setColor(Color.red);
		g.fillRect(x, y, w, h);
		g.setColor(Color.green);
		g.fillRect(x, y, w * this.thePlayer.getHealth() / 100, h);
		
		// Draw lives
		g.setFont(font);
		g.setColor(Color.RED);
		g.drawString("Lives: " + lives, x, y + h*2);
	}
}
