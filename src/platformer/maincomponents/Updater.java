package platformer.maincomponents;

import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;

import platformer.services.*;

public class Updater {
	private Timer gameLogicTimer;

	public Updater(AnimationService animationService, CameraDrawingService cameraDrawingService,
			CollisionService collisionService, GridMapService gridMapService, int gameSpeed,
			SceneManagerService sceneManagerService, ScreenDrawingService screenDrawingService,
			UpdateService updateService) {

		gameLogicTimer = new Timer(1000 / gameSpeed, e -> {
			animationService.update();
			cameraDrawingService.update();
			screenDrawingService.update();

			updateService.updateBegin();

			updateService.update();

			collisionService.update();

			gridMapService.update();

			updateService.updateEnd();

			sceneManagerService.update();
		});
		gameLogicTimer.start();
	}

	public void dispose() {
		gameLogicTimer.stop();
	}
}
