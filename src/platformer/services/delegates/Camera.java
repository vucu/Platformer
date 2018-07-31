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

// The game world is too big, but the screen is small
// The camera specify the rectangle within the world
// to draw on the screen
public abstract class Camera {
	public final Rectangle world;
	public final int screenX;
	public final int screenY;
	public final int w;
	public final int h;
	public final double scale;

	// Curent state of the camera
	public int x = 0;
	public int y = 0;
	protected ITransform trackingObject = null;

	// These constructors set the variables
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

	// How the camera updates is up to the child class
	public abstract void update();
	

}
