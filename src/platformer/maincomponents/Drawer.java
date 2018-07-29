package platformer.maincomponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import platformer.services.*;
import platformer.services.delegates.Camera;
import sun.awt.DisplayChangedListener;

public class Drawer extends JPanel {
	private class DrawingPanel extends JPanel {
		Camera[] cameras = cameraService.getCameras();

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

			// Display the camera drawing service
			for (int i = 0; i < cameras.length; i++) {
				int w = (int) (cameras[i].w * cameras[i].scale);
				int h = (int) (cameras[i].h * cameras[i].scale);

				g2d.setClip(cameras[i].screenX, cameras[i].screenY, w - 2, h - 2);

				AffineTransform transformation = new AffineTransform();
				transformation.translate(cameras[i].screenX, cameras[i].screenY);
				transformation.scale(cameras[i].scale, cameras[i].scale);
				g2d.transform(transformation);

				cameraService.draw(g, i);

				try {
					g2d.transform(transformation.createInverse());
				} catch (NoninvertibleTransformException e) {
					e.printStackTrace();
				}
			}

			// Display the screen drawing service
			g2d.setClip(display);
			screenDrawingService.draw(g);

			Toolkit.getDefaultToolkit().sync();
		}
	}

	// *** Key events ***
	private class MainKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent ke) {
			// Do nothing
		}

		@Override
		public void keyPressed(KeyEvent ke) {
			keyboardService.pressKey(ke.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent ke) {
			keyboardService.releaseKey(ke.getKeyCode());
		}
	}

	private final CameraDrawingService cameraService;
	private final KeyboardService keyboardService;
	private final ScreenDrawingService screenDrawingService;

	private final MainKeyListener keyListener;

	private Timer drawingTimer;
	private final Rectangle display;

	private final int fps = 30;

	public Drawer(CameraDrawingService cameraService, KeyboardService keyboardService, Rectangle display,
			ScreenDrawingService screenDrawingService) {
		this.keyboardService = keyboardService;
		this.cameraService = cameraService;
		this.screenDrawingService = screenDrawingService;
		this.display = display;

		// Create the screen panel
		this.setLayout(null);
		JPanel screenPanel = new DrawingPanel();
		screenPanel.setBackground(Color.black);
		screenPanel.setBounds(display);
		add(screenPanel);

		// Setup pane
		keyListener = new MainKeyListener();
		addKeyListener(keyListener);
		setSize(display.width, display.height);
		setBackground(Color.black);
		setFocusable(true);
		requestFocus();

		// Create timer
		drawingTimer = new Timer(1000 / fps, e -> {
			screenPanel.repaint();
		});

		drawingTimer.start();
	}

	public void dispose() {
		removeAll();
		drawingTimer.stop();
		removeKeyListener(keyListener);
	}
}
