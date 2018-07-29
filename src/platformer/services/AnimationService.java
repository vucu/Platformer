package platformer.services;

import java.awt.image.BufferedImage;
import java.util.*;

import platformer.datastructures.*;
import platformer.gameobject.*;
import platformer.gameobject.properties.*;
import platformer.maincomponents.*;
import platformer.services.delegates.*;

public class AnimationService {
	private final ImageService imageService;
	private final int gameSpeed;
	private final Remover remover;

	private Map<IDrawable, List<BufferedImage>> images = new HashMap<>();
	private Map<IDrawable, Integer> currentImageNumber = new HashMap<>();
	private Map<IDrawable, Double> timeBetweenImages = new HashMap<>();
	private Map<IDrawable, Double> cummulativeTime = new HashMap<>();
	private Map<IDrawable, Integer> loopsRemaining = new HashMap<>();

	public AnimationService(ImageService imageService, int gameSpeed, Remover remover) {
		this.imageService = imageService;
		this.gameSpeed = gameSpeed;
		this.remover = remover;
	}

	// Interface for game object
	public void register(IDrawable object, String filenameFormat, int numberOfImages) {
		this.register(object, filenameFormat, numberOfImages, numberOfImages);
	}

	public void register(IDrawable object, String filenameFormat, int numberOfImages, int animationPerSecond) {
		this.register(object, filenameFormat, numberOfImages, animationPerSecond, 1);
	}

	public void register(IDrawable object, String filenameFormat, int numberOfImages, int animationPerSecond,
			int loopCount) {
		// Load the images from file
		List<BufferedImage> imagesFromFile = new ArrayList<>();
		for (int i = 0; i < numberOfImages; i++) {
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

		currentImageNumber.put(object, 0);
		timeBetweenImages.put(object, 1.0 / animationPerSecond);
		cummulativeTime.put(object, 0.0);
		loopsRemaining.put(object, loopCount);
	}

	public BufferedImage getCurrentImage(IDrawable object) {
		int oLoopsRemaining = loopsRemaining.get(object);
		if (oLoopsRemaining <= 0)
			return null;

		int oImageNumber = currentImageNumber.get(object);
		List<BufferedImage> oImages = images.get(object);

		return oImages.get(oImageNumber);
	}

	public void update() {
		Set<IDrawable> objects = images.keySet();

		for (IDrawable object : objects) {
			int oLoopsRemaining = loopsRemaining.get(object);
			if (oLoopsRemaining <= 0)
				continue;

			double oCummulativeTime = cummulativeTime.get(object);

			// Add the time elapsed
			double timeElapsed = 1.0 / gameSpeed;
			oCummulativeTime += timeElapsed;

			// Update the current image number
			double oTimeBetweenImages = timeBetweenImages.get(object);
			if (oCummulativeTime > oTimeBetweenImages) {
				List<BufferedImage> oImages = images.get(object);
				int oCurrentImageNumber = currentImageNumber.get(object);

				// Increase the image number
				oCurrentImageNumber++;
				if (oCurrentImageNumber > oImages.size() - 1) {
					oLoopsRemaining--;
					loopsRemaining.put(object, oLoopsRemaining);

					oCurrentImageNumber = 0;
				}

				// Store back to data structures
				currentImageNumber.put(object, oCurrentImageNumber);

				// Subtract the time between images
				oCummulativeTime -= oTimeBetweenImages;
			}

			// Update the cummulative time
			cummulativeTime.put(object, oCummulativeTime);
		}

		// Also remove destroyed objects
		remover.removeIfDestroyed(images.keySet());
		remover.removeIfDestroyed(currentImageNumber.keySet());
		remover.removeIfDestroyed(timeBetweenImages.keySet());
		remover.removeIfDestroyed(cummulativeTime.keySet());
		remover.removeIfDestroyed(loopsRemaining.keySet());
	}
}
