package platformer.maincomponents;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.function.*;

import javax.swing.*;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import platformer.datastructures.Position;
import platformer.services.*;

public class MainFrame extends JFrame {
	Drawer panel;

	public MainFrame(Drawer panel, Rectangle display) {
		this.panel = panel;
		add(panel);

		// Setup window
		setTitle("Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(display.width, display.height);
		setLocationRelativeTo(null);
		setVisible(true);

		panel.requestFocus();
	}

	public void replaceMainPanel(Drawer panel) {
		// Remove the old panel
		remove(this.panel);

		// Then add the new panel
		add(panel);
		revalidate();
		this.panel = panel;

		panel.requestFocus();
	}
	
	
}
