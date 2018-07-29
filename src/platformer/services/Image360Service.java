package platformer.services;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

import platformer.gameobject.properties.IDrawable;

public class Image360Service {
	private final ImageService imageService;
	
	// This service is for rotating objects, and they need 360 images for 360 degrees
	public Image360Service(ImageService imageService) {
		this.imageService = imageService;
	}
	
	Map<IDrawable,List<BufferedImage>> images = new HashMap<>();
	final int imageCount = 360;
	public void register(IDrawable object, String filenameFormat) {
		// Load the images from file
		List<BufferedImage> imagesFromFile = new ArrayList<>();
		for (int i = 0; i < imageCount; i++) {
			try {
				String fname = String.format(filenameFormat, i);
				BufferedImage image = imageService.getImage(fname);
				imagesFromFile.add(image);
			} catch (IllegalFormatException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		images.put(object, imagesFromFile);
	}
	
	public BufferedImage getImage(IDrawable object, int imageNumber) {
		return images.get(object).get(imageNumber);
	}
}
