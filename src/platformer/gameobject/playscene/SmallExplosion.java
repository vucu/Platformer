package platformer.gameobject.playscene;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import platformer.builder.ServicePack;
import platformer.datastructures.*;
import platformer.gameobject.*;
import platformer.gameobject.properties.*;
import platformer.services.*;

public class SmallExplosion extends GameObject implements IDrawable, IDestructible, ITransform {
	private final AnimationService animationService;
	private final SoundService soundService = ServicePack.services().soundService;
	
	public SmallExplosion(AnimationService animationService, 
			CameraDrawingService camera, 
			Position position) {
		this.animationService = animationService;
		animationService.register(this, "small_explosion_%d.png", 6, 50);
		camera.drawOnAllCameras(this);

		this.setPosition(position);
		soundService.playSound("Explosion_small.wav");
	}

	@Override
	public void onDraw(Graphics g) {
		BufferedImage image = animationService.getCurrentImage(this);
		if (image == null) {
			this.destroy();
			return;
		}

		Position position = this.getPosition();
		g.drawImage(image, position.x - image.getWidth() / 2, position.y - image.getHeight() / 2, null);
	}

	@Override
	public int getDepth() {
		return -10;
	}

	private boolean destroyed = false;

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void destroy() {
		destroyed = true;
	}

	private Position position;

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void setPosition(Position position) {
		this.position = position;
	}
}
