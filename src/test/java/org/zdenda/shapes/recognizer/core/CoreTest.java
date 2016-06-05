package org.zdenda.shapes.recognizer.core;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

/**
 * Class for testing generic core utilities.
 * @author Zdenda
 *
 */
public class CoreTest {

	/**
	 * Verify that pixel's toInt method works correctly.
	 */
	@Test
	public void testPixelToInt() {
		int c1 = 0x00FFFFFF;
		int c2 = 0x00FF0000;
		int c3 = 0x0000FF00;
		int c4 = 0x000000FF;
		
		Pixel p1 = new Pixel(c1);
		Pixel p2 = new Pixel(c2);
		Pixel p3 = new Pixel(c3);
		Pixel p4 = new Pixel(c4);
		
		assertEquals("Wrong value for pixel "+p1+".",c1,p1.toInt());
		assertEquals("Wrong value for pixel "+p2+".",c2,p2.toInt());
		assertEquals("Wrong value for pixel "+p3+".",c3,p3.toInt());
		assertEquals("Wrong value for pixel "+p4+".",c4,p4.toInt());
	}
	
	/**
	 * Verify that converting works.
	 */
	@Test
	public void testConvertToPixelArray() {
		String testFileName = "/testImg/testConvert.bmp";
		int expWidth = 5;
		int expHeight = 6;
		
		//load image
		File imageFile = new File(getClass().getResource(testFileName).getFile());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//convert
		Pixel[][] array = BitmapConverter.convertToPixArray(bi);
		
		//check dimensions
		assertEquals("Wrong height!", expHeight, array.length);
		assertEquals("Wrong width!", expWidth, array[0].length);
		
		//check colors
		assertTrue("[0][1] should be balck!",array[0][1].isBlack());
		assertTrue("[1][4] shoubl be red!", array[1][4].getR() == 255 && array[1][4].getG() == 0 && array[1][4].getB() == 0);
		assertTrue("[3][2] shoubl be yellow!", array[3][2].getR() == 255 && array[3][2].getG() == 255 && array[3][2].getB() == 0);
		assertTrue("[4][0] shoubl be blue!", array[4][0].getR() == 0 && array[4][0].getG() == 0 && array[4][0].getB() == 255);
		assertTrue("[5][3] shoubl be green!", array[5][3].getR() == 0 && array[5][3].getG() == 255 && array[5][3].getB() == 0);
	}
}
