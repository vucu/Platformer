package platformer.gameobject.playscene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import platformer.gameobject.GameObject;
import platformer.gameobject.properties.*;
import platformer.services.*;

public class Score extends GameObject implements IDrawable {
	final int player;

	public Score(CameraDrawingService camera, int player) {
		camera.drawDirectlyToCamera(this, player);
		this.player = player;
	}

	@Override
	public int getDepth() {
		return -100;
	}

	private final int fontSize = 60;
	private final Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);

	@Override
	public void onDraw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		int x = 25;
		int y = 25 + fontSize / 2;

		g2d.setFont(font);
		if (player == 0) {
			g2d.setColor(new Color(124, 24, 21));
		} else {
			g2d.setColor(new Color(38, 49, 163));
		}
		g2d.drawString("" + score, x, y);
	}

	private int score = 0;

	public void increase() {
		score++;
	}
}
