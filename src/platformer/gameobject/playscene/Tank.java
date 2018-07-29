package platformer.gameobject.playscene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

import platformer.builder.ServicePack;
import platformer.datastructures.Position;
import platformer.datastructures.Vector;
import platformer.gameobject.GameObject;
import platformer.gameobject.properties.*;
import platformer.services.*;

public class Tank extends GameObject implements IDrawable, ITransform, IUpdatable, ICollider, IDestructible {
	private final CameraDrawingService camera;
	private final CollisionService collisionService;
	private final UpdateService updateService;
	private final Image360Service image360Service;
	private final KeyboardService keyboardService;
	private final int player;
	private final Score enemyScore;
	private final Position initialPosition;

	// Implement secondary weapon
		private final int NONE = 0;
		private final int ROCKET = 1;
		private final int SHIELD = 2;
		private final int BOMB = 3;
		private int weapon = NONE;
	private final BufferedImage shieldImage;
	private final BufferedImage[] weaponImages;
	
	public Tank(CameraDrawingService camera, 
			CollisionService collisionService,
			ImageService imageService,
			Image360Service image360Service, 
			int player,
			KeyboardService keyboardService, 
			Score enemyScore,
			Position initialPosition, 
			UpdateService updateService) {
		this.camera = camera;
		this.collisionService = collisionService;
		this.updateService = updateService;
		this.shieldImage = imageService.getImage("shield_player"+player+".png");
		weaponImages = new BufferedImage[] {
				imageService.getImage("weapon_none.png"),
				imageService.getImage("weapon_rocket.png"),
				imageService.getImage("weapon_shield.png"),
				imageService.getImage("weapon_bomb.png"),
		};
		
		this.image360Service = image360Service;
		image360Service.register(this,"tank_player" + player + "_%d.png");
		this.enemyScore = enemyScore;

		updateService.register(this);
		collisionService.register(this);
		this.keyboardService = keyboardService;
		this.player = player;

		camera.track(this, player);
		camera.drawOnAllCameras(this);

		this.initialPosition = initialPosition;
		this.setPosition(initialPosition);
	}

	private Tank enemy;

	public void setEnemy(Tank enemy) {
		this.enemy = enemy;
	}

	@Override
	public Shape getCollisionMask() {
		// Note: Although the collision mask is a square, the tank can rotate
		// Thus it will be a rotated square
		final int maskSize = 48;

		// The corner of the squares
		Vector[] corners = new Vector[4];
		corners[0] = new Vector(45, maskSize / Math.sqrt(2));
		corners[1] = new Vector(135, maskSize / Math.sqrt(2));
		corners[2] = new Vector(225, maskSize / Math.sqrt(2));
		corners[3] = new Vector(315, maskSize / Math.sqrt(2));

		// Rotate all the corner in the current angle
		for (int i = 0; i < corners.length; i++) {
			corners[i] = corners[i].getNewVectorWithRotation(direction.angle);
		}

		// Now use those to calculate the position of the polygons
		Position origin = this.getPosition();
		int[] xPoints = new int[corners.length];
		int[] yPoints = new int[corners.length];
		for (int i = 0; i < corners.length; i++) {
			xPoints[i] = origin.x + (int) corners[i].getX();
			yPoints[i] = origin.y + (int) corners[i].getY();
		}

		Polygon mask = new Polygon(xPoints, yPoints, corners.length);
		return mask;
	}

	private Vector previousDirection;
	private double previousX;
	private double previousY;

	@Override
	public void onUpdateBegin() {
		previousX = x;
		previousY = y;
		previousDirection = direction;
	}

	// Input: Player 1: WASD, SPACE, CTRL, player 2: Arrows, ENTER, DELETE
	// Left/right will rotate the tank, Up/down will move the tank forward/backward
	private Vector direction = new Vector(0, 5);
	private double x;
	private double y;

	private int shootCooldown = 0;

	@Override
	public void onUpdate() {
		// Update the shoot cooldown
		if (shootCooldown > 0) {
			shootCooldown--;
		}

		// Update the shield duration
		if (shieldDuration > 0) {
			shieldDuration--;
		}

		if (player == 0) {
			if (keyboardService.check(KeyEvent.VK_W)) {
				moveForward();
			} else if (keyboardService.check(KeyEvent.VK_S)) {
				moveBackward();
			} else if (keyboardService.check(KeyEvent.VK_A)) {
				rotateLeft();
			} else if (keyboardService.check(KeyEvent.VK_D)) {
				rotateRight();
			} else if (keyboardService.check(KeyEvent.VK_SPACE)) {
				shootPrimary();
			} else if (keyboardService.check(KeyEvent.VK_CONTROL)) {
				shootSecondary();
			}
		} else if (player == 1) {
			if (keyboardService.check(KeyEvent.VK_UP)) {
				moveForward();
			} else if (keyboardService.check(KeyEvent.VK_DOWN)) {
				moveBackward();
			} else if (keyboardService.check(KeyEvent.VK_LEFT)) {
				rotateLeft();
			} else if (keyboardService.check(KeyEvent.VK_RIGHT)) {
				rotateRight();
			} else if (keyboardService.check(KeyEvent.VK_ENTER)) {
				shootPrimary();
			} else if (keyboardService.check(KeyEvent.VK_DELETE)) {
				shootSecondary();
			}
		}

		if (keyboardService.check(KeyEvent.VK_ESCAPE)) {
			// Debug
			debug();
		}
	}
	
	static int counter = 0;
	private void debug() {
		counter = (counter + 1 ) % 3;
		weapon = counter;
		shootCooldown = 20;
	}

	private void rotateRight() {
		direction = direction.getNewVectorWithRotation(5);
	}

	private void rotateLeft() {
		direction = direction.getNewVectorWithRotation(-5);
	}

	private void moveForward() {
		x = x + direction.getX();
		y = y + direction.getY();
	}

	private void moveBackward() {
		x = x - direction.getX();
		y = y - direction.getY();
	}

	private void shootPrimary() {
		if (shootCooldown <= 0) {
			new Bullet(camera, collisionService, updateService, 
					ServicePack.services().imageService, player, getPosition(), direction);
			shootCooldown = 20;
		}
	}

	private void shootSecondary() {
		if (shootCooldown <= 0) {
			// If no weapon, don't shoot
			if (weapon==NONE)
				return;

			switch (weapon) {
			case ROCKET:
				new Rocket(camera, collisionService, updateService, image360Service, getPosition(), enemy);
				System.out.println("Deploy rocket");
				break;
			case BOMB:
				new BouncingBomb(camera, collisionService, updateService, image360Service, player, getPosition(),
						direction);
				System.out.println("Deploy bomb");
				break;
			case SHIELD:
				shield();
				System.out.println("Deploy shield");
				break;
			default:
				break;
			}

			weapon = NONE;
			shootCooldown = 30;
		}
	}

	@Override
	public void onUpdateEnd() {
		// Do nothing
	}

	@Override
	public int getDepth() {
		return -1;
	}

	@Override
	public void onDraw(Graphics g) {
		Position position = getPosition();

		// Draw the tank image
		BufferedImage image = image360Service.getImage(this, direction.angle);
		g.drawImage(image, position.x - image.getWidth() / 2, position.y - image.getHeight() / 2, null);

		// Draw the weapon
		BufferedImage weaponImage = weaponImages[weapon];
		g.drawImage(weaponImage, position.x - weaponImage.getWidth() / 2, position.y - weaponImage.getHeight() / 2 + 24, null);

		// Draw the shield
		if (isShielded()) {
			g.drawImage(shieldImage, position.x - shieldImage.getWidth() / 2, position.y - shieldImage.getHeight() / 2, null);
		}
		
		// Draw the health bar
		int x = position.x - 16;
		int y = position.y - 36;
		int w = 32;
		int h = 5;

		g.setColor(Color.red);
		g.fillRect(x, y, w, h);
		g.setColor(Color.green);
		g.fillRect(x, y, w * hp / 100, h);
	}

	@Override
	public Position getPosition() {
		return new Position((int) x, (int) y);
	}

	@Override
	public void setPosition(Position position) {
		this.x = position.x;
		this.y = position.y;
	}

	// Allow other objects to interact with the tank
	public int getPlayer() {
		return player;
	}

	public void revertPosition() {
		x = previousX;
		y = previousY;
		direction = previousDirection;
	}

	private int shieldDuration = 0;

	private void shield() {
		shieldDuration = 300;
	}

	private boolean isShielded() {
		return shieldDuration > 0;
	}

	private int hp = 100;

	public void damage() {
		if (shieldDuration > 0)
			return;

		hp -= 10;
		if (hp <= 0) {
			enemyScore.increase();

			new BigExplosion(ServicePack.services().animationService, ServicePack.services().cameraDrawingService,
					this.getPosition());

			this.setPosition(initialPosition);
			hp = 100;
		}
	}

	// From powerup
	public void heal() {
		hp += 50;
		if (hp > 100)
			hp = 100;
	}

	public int getHP() {
		return hp;
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
	
	public void armShield() {
		System.out.println("Add shield");
		weapon = SHIELD;
	}

	public void armBomb() {
		System.out.println("Add bomb");
		weapon = BOMB;
	}

	public void armRocket() {
		System.out.println("Add rocket");
		weapon = ROCKET;
	}

	@Override
	public void onCollision(ICollider other) {
		if (other instanceof Tank) {
			this.revertPosition();
		}
	}
}
