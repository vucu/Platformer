package platformer.services;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import platformer.datastructures.*;
import platformer.gameobject.*;
import platformer.gameobject.properties.*;
import platformer.maincomponents.*;
import platformer.services.delegates.*;

public class CameraDrawingService {
	private final Remover remover;
	private final Camera[] cameras;

	private final Map<IDrawable, Integer> objectsRelativeToCamera;
	private final Map<IDrawable, Integer> objectsRelativeToWorld;

	private final List<IDrawable> sortedObjectsRelativeToCamera;
	private final List<IDrawable> sortedObjectsRelativeToWorld;

	private final Comparator<IDrawable> compareByDepth;

	public CameraDrawingService(Remover remover, Camera... cameras) {
		this.remover = remover;
		this.cameras = cameras;

		// Sorted the objects by their depth, so higher depth are drawn first
		this.compareByDepth = new Comparator<IDrawable>() {
			@Override
			public int compare(IDrawable a, IDrawable b) {
				return b.getDepth() - a.getDepth();
			}
		};

		objectsRelativeToCamera = new ConcurrentHashMap<>();
		objectsRelativeToWorld = new ConcurrentHashMap<>();

		sortedObjectsRelativeToCamera = new CopyOnWriteArrayList<>();
		sortedObjectsRelativeToWorld = new CopyOnWriteArrayList<>();
	}

	// *** Interface for GameObject ***
	// Register this object, so it will be drawn by this service
	private final int ALL_CAMERAS = -1;

	public void drawOnOneCamera(IDrawable object, int cameraNumber) {
		objectsRelativeToCamera.put(object, cameraNumber);
		sortedObjectsRelativeToCamera.add(object);
		sortedObjectsRelativeToCamera.sort(compareByDepth);
	}

	public void drawOnAllCameras(IDrawable object) {
		objectsRelativeToCamera.put(object, ALL_CAMERAS);
		sortedObjectsRelativeToCamera.add(object);
		sortedObjectsRelativeToCamera.sort(compareByDepth);
	}

	public void drawDirectlyToCamera(IDrawable object, int cameraNumber) {
		objectsRelativeToWorld.put(object, cameraNumber);
		sortedObjectsRelativeToWorld.add(object);
		sortedObjectsRelativeToWorld.sort(compareByDepth);
	}

	public void track(ITransform object, int cameraNumber) {
		cameras[cameraNumber].setTrackingObject(object);
	}

	// *** Interface for components ***
	public Camera[] getCameras() {
		return cameras;
	}

	// Draw one camera to the screen
	public void draw(Graphics g, int cameraNumber) {
		Graphics2D g2d = (Graphics2D) g;

		// Translate the graphic to offset for the camera
		g2d.translate(-cameras[cameraNumber].x, -cameras[cameraNumber].y);

		sortedObjectsRelativeToCamera.forEach(o -> {
			int n = objectsRelativeToCamera.get(o);
			if (n == ALL_CAMERAS || n == cameraNumber) {
				o.onDraw(g);
			}
		});

		// Return it back to original state
		g2d.translate(cameras[cameraNumber].x, cameras[cameraNumber].y);

		// Now draw objects that is directly to the camera
		sortedObjectsRelativeToWorld.forEach(o -> {
			int n = objectsRelativeToWorld.get(o);
			if (n == ALL_CAMERAS || n == cameraNumber) {
				o.onDraw(g);
			}
		});
	}

	// Update this camera
	public void update() {
		// Update all cameras
		for (Camera camera : cameras) {
			camera.update();
		}

		// Also check if a object is destroyed. If that is the case don't draw them
		// anymore
		remover.removeIfDestroyed(objectsRelativeToCamera.keySet());
		remover.removeIfDestroyed(sortedObjectsRelativeToCamera);
		remover.removeIfDestroyed(objectsRelativeToWorld.keySet());
		remover.removeIfDestroyed(sortedObjectsRelativeToWorld);
	}
}
