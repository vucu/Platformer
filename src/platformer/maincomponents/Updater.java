package platformer.maincomponents;

import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;

import platformer.services.*;

public class Updater {
	private Timer gameLogicTimer;

	public Updater(AnimationService animationService, CameraDrawingService cameraDrawingService,
			CollisionService collisionService, int gameSpeed,
			ScreenDrawingService screenDrawingService,
			UpdateService updateService) {

		gameLogicTimer = new Timer(1000 / gameSpeed, e -> {
			animationService.update();
			cameraDrawingService.update();
			screenDrawingService.update();

			updateService.updateBegin();

			updateService.update();

			collisionService.update();

			updateService.updateEnd();
		});
		gameLogicTimer.start();
	}

	public void dispose() {
		gameLogicTimer.stop();
	}
}
