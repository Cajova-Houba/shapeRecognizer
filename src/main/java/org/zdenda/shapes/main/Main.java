package org.zdenda.shapes.main;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdenda.shapes.recognizer.core.BitmapConverter;
import org.zdenda.shapes.recognizer.core.Line;
import org.zdenda.shapes.recognizer.core.Pixel;
import org.zdenda.shapes.recognizer.core.lines.Lines;

/**
 * Main class of application.
 * @author Zdenda
 *
 */
public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	/**
	 * Name of the file is specificated through args.
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0) {
			logger.warn("No arguments.");
			System.out.println("No arguments. Use the file name as the first argument.");
			return;
		}
		
		//load file and verify it exists
		String fileName = args[0];
		File imgFile = new File(fileName);
		
		if(!imgFile.exists()) {
			logger.warn("File: {} doesn't exist.",imgFile.getPath());
			return;
		}
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(imgFile);
		} catch (IOException e) {
			System.out.println("Error reading the file: "+imgFile.getPath()+".");
			logger.error(e);
			return; 
		}
		
		
		//try to find a line
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		
		if(line == null) {
			System.out.println("No line found.");
		} else {
			System.out.println("Line found from: ["+line.getStart().x+","+line.getStart().y+"] to: ["+line.getStart().x+","+line.getStart().y+"]");
		}
	}

}
