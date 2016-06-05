package org.zdenda.shapes.recognizer.core;

import java.awt.image.BufferedImage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class containing static methods to convert bitmap to 2D array of {@code Pixel} objects.
 * @author Zdenda
 *
 */
public class BitmapConverter {

	private static final Logger logger = LogManager.getLogger(BitmapConverter.class);
	
	/**
	 * Converts a buffered image object to 2D array of {@code Pixel} objects.
	 * The array is created as Pixel[height][width].
	 * 
	 * @param image
	 * @return
	 */
	public static Pixel[][] convertToPixArray(BufferedImage image) {
		
		//check that image is ok
		if(image == null) {
			logger.warn("Image is null");
			return new Pixel[0][0];
		}
		
		int w = image.getWidth();
		int h = image.getHeight();
		Pixel[][] res = new Pixel[h][w];
		logger.debug("Converting image {}x{} to Pixel array.",w,h);
		
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				res[i][j] = new Pixel(image.getRGB(j, i));
			}
		}
		
		return res;
	}
}
