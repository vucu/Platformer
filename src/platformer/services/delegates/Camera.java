/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer.services.delegates;

import java.awt.Color;
import java.awt.Rectangle;

import platformer.datastructures.Position;
import platformer.gameobject.properties.ITransform;

public class Camera {
	public final Rectangle world;
	public final int screenX;
	public final int screenY;
	public final int w;
	public final int h;
	public final double scale;

	// Curent state of the camera
	public int x = 0;
	public int y = 0;
	private ITransform trackingObject = null;

	public Camera(Rectangle world, int w, int h) {
		this(world, 0, 0, w, h);
	}

	public Camera(Rectangle world, int screenX, int screenY, int w, int h) {
		this(world, screenX, screenY, w, h, 1);
	}

	public Camera(Rectangle world, int screenX, int screenY, int w, int h, double scale) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.w = w;
		this.h = h;
		this.scale = scale;
		this.world = world;
	}

	public void setTrackingObject(ITransform trackingObject) {
		this.trackingObject = trackingObject;
	}

	public void update() {
		if (trackingObject == null) {
			return;
		}
		int gap = 150;
		Position objectPosition = trackingObject.getPosition();
		if (objectPosition.x < x + gap) {
			x = objectPosition.x - gap - 2;
			if (x < world.x) {
				x = world.x;
			}
		}
		if (objectPosition.x > x + w - gap) {
			x = objectPosition.x - w + gap + 2;
			if (x + w > world.x + world.width) {
				x = world.x + world.width - w;
			}
		}
		if (objectPosition.y < y + gap) {
			y = objectPosition.y - gap - 2;
			if (y < world.y) {
				y = world.y;
			}
		}
		if (objectPosition.y > y + h - gap) {
			y = objectPosition.y - h + gap + 2;
			if (y + h > world.y + world.height) {
				y = world.y + world.height - h;
			}
		}
	}

}
