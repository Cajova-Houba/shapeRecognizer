package org.zdenda.shapes.recognizer.core.lines;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.zdenda.shapes.recognizer.core.BitmapConverter;
import org.zdenda.shapes.recognizer.core.Direction;
import org.zdenda.shapes.recognizer.core.Line;
import org.zdenda.shapes.recognizer.core.Pixel;

public class LinesTest {

	//test files
	//one line
	private final String path = "/testImg/lines/";
	private final String noLines = "noline.bmp";
	private final String horizontalLine = "horizontalLine.bmp";
	private final String verticalLine = "verticalLine.bmp";
	private final String SEline1 = "SEline1.bmp";
	private final String SEline2 = "SEline2.bmp";
	private final String SWline1 = "SWline1.bmp";
	private final String SWline2 = "SWline2.bmp";
	
	//more lines
	private final String horizontalLines = "horizontalLines.bmp";
	private final String verticalLines = "verticalLines.bmp";
	private final String SElines = "SElines.bmp";
	private final String SWlines = "SWlines.bmp";
	
	@Test
	public void testFindLineFail1() {
		Line line = Lines.findLine(null);
		assertEquals("Line should be null!", null, line);
	}
	
	@Test
	public void testFindLineFail2() {
		Pixel[][] bitmap = new Pixel[0][0];
		Line line = Lines.findLine(bitmap);
		assertEquals("Line should be null!", null, line);
	}
	
	@Test
	public void testFindLineFail3() {
		Pixel[][] bitmap = new Pixel[1][0];
		Line line = Lines.findLine(bitmap);
		assertEquals("Line should be null!", null, line);
	}
	
	@Test
	public void testFindLineNoLine() {
		BufferedImage image = openImage(getClass().getResource(path+noLines));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		assertEquals("No line should have been found!", null, line);
	}
	
	@Test
	public void testFindLineHorizontal() {
		BufferedImage image = openImage(getClass().getResource(path+horizontalLine));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		
		//check the points
		assertEquals("The line should start on [2,9]!",new Point(2, 9), line.getStart());
		assertEquals("The line should end on [17,9]!",new Point(17, 9), line.getEnd());
	}
	
	@Test
	public void testFindLineVertical() {
		BufferedImage image = openImage(getClass().getResource(path+verticalLine));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		
		//check the points
		assertEquals("The line should start on [9,2]!",new Point(9, 2), line.getStart());
		assertEquals("The line should end on [9,17]!",new Point(9, 17), line.getEnd());
	}
	
	@Test
	public void testFindLineSE1() {
		BufferedImage image = openImage(getClass().getResource(path+SEline1));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		
		//check the points
		assertEquals("The line should start on [2,2]!",new Point(2, 2), line.getStart());
		assertEquals("The line should end on [17,17]!",new Point(17, 17), line.getEnd());
	}
	
	@Test
	public void testFindLineSE2() {
		BufferedImage image = openImage(getClass().getResource(path+SEline2));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		
		//check the points
		assertEquals("The line should start on [3,2]!",new Point(3, 2), line.getStart());
		assertEquals("The line should end on [12,16]!",new Point(12, 16), line.getEnd());
	}
	
	@Test
	public void testFindLineSW1() {
		BufferedImage image = openImage(getClass().getResource(path+SWline1));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		
		//check the points
		assertEquals("The line should start on [17, 2]!",new Point(17, 2), line.getStart());
		assertEquals("The line should end on [2, 17]!",new Point(2, 17), line.getEnd());
	}
	
	@Test
	public void testFindLineSW2() {
		BufferedImage image = openImage(getClass().getResource(path+SWline2));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		Line line = Lines.findLine(bitmap);
		
		//check the points
		assertEquals("The line should start on [16, 2]!",new Point(16, 2), line.getStart());
		assertEquals("The line should end on [7, 16]!",new Point(7, 16), line.getEnd());
	}
	
	@Test
	public void testFindLineHorizontals(){
		BufferedImage image = openImage(getClass().getResource(path+horizontalLines));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		List<Line> lines = Lines.findLines(bitmap);
		
		assertEquals("Two lines should have been found!",2,lines.size());
		
		
		//check the points
		assertEquals("The first line should start on [3, 9]!",new Point(3, 9), lines.get(0).getStart());
		assertEquals("The first line should end on [26, 9]!",new Point(26, 9), lines.get(0).getEnd());
		
		assertEquals("The second line should start on [3, 9]!",new Point(3, 43), lines.get(1).getStart());
		assertEquals("The second line should end on [26, 9]!",new Point(26, 43), lines.get(1).getEnd());
	}
	
	@Test
	public void testFindLineVerticals(){
		BufferedImage image = openImage(getClass().getResource(path+verticalLines));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		List<Line> lines = Lines.findLines(bitmap);
		
		assertEquals("Two lines should have been found!",2,lines.size());
		
		
		//check the points
		assertEquals("The first line should start on [6, 3]!",new Point(6, 3), lines.get(0).getStart());
		assertEquals("The first line should end on [6, 46]!",new Point(6, 46), lines.get(0).getEnd());
		
		assertEquals("The second line should start on [22, 3]!",new Point(22, 3), lines.get(1).getStart());
		assertEquals("The second line should end on [22, 46]!",new Point(22, 46), lines.get(1).getEnd());
	}
	
	@Test
	public void testFindLineSEs(){
		BufferedImage image = openImage(getClass().getResource(path+SElines));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		List<Line> lines = Lines.findLines(bitmap);
		
		assertEquals("Two lines should have been found!",2,lines.size());
		
		
		//check the points
		assertEquals("The first line should start on [6, 3]!",new Point(6, 3), lines.get(0).getStart());
		assertEquals("The first line should end on [27, 28]!",new Point(27, 28), lines.get(0).getEnd());
		
		assertEquals("The second line should start on [3, 17]!",new Point(3, 17), lines.get(1).getStart());
		assertEquals("The second line should end on [22, 48]!",new Point(22, 48), lines.get(1).getEnd());
	}
	
	@Test
	public void testFindLineSWs(){
		BufferedImage image = openImage(getClass().getResource(path+SWlines));
		if(image == null) {
			fail();
		}
		
		Pixel[][] bitmap = BitmapConverter.convertToPixArray(image);
		List<Line> lines = Lines.findLines(bitmap);
		
		assertEquals("Two lines should have been found!",2,lines.size());
		
		
		//check the points
		assertEquals("The first line should start on [23, 3]!",new Point(23, 3), lines.get(0).getStart());
		assertEquals("The first line should end on [2, 28]!",new Point(2, 28), lines.get(0).getEnd());
		
		assertEquals("The second line should start on [26, 17]!",new Point(26, 17), lines.get(1).getStart());
		assertEquals("The second line should end on [7, 48]!",new Point(7, 48), lines.get(1).getEnd());
	}
	
	/**
	 * Test null points and same point.
	 */
	@Test
	public void testDetermineDirection1() {
		Point p1 = new Point(10,10);
		
		assertEquals("Direction should be NONE on null point!", Direction.NONE, Lines.determineDirection(null, null));
		assertEquals("Direction should be NONE on null point!", Direction.NONE, Lines.determineDirection(p1, null));
		assertEquals("Direction should be NONE on same point!", Direction.NONE, Lines.determineDirection(p1, p1));
	}
	
	/**
	 * Test N,E,S,W
	 */
	@Test
	public void testDetermineDirection2() {
		Point c = new Point(0,0);
		Point n = new Point(0,-1);
		Point e = new Point(1,0);
		Point s = new Point(0,1);
		Point w = new Point(-1,0);
		
		assertEquals("Direction should be NORTH!", Direction.N, Lines.determineDirection(c, n));
		assertEquals("Direction should be EAST!", Direction.E, Lines.determineDirection(c, e));
		assertEquals("Direction should be SOUTH!", Direction.S, Lines.determineDirection(c, s));
		assertEquals("Direction should be WEST!", Direction.W, Lines.determineDirection(c, w));
	}
	
	/**
	 * Test NE,SE,SW,NW
	 */
	@Test
	public void testDetermineDirection3() {
		Point c = new Point(0,0);
		Point ne = new Point(1,-1);
		Point se = new Point(1,1);
		Point sw = new Point(-1,1);
		Point nw = new Point(-1,-1);
		
		assertEquals("Direction should be NORTH-EAST!", Direction.NE, Lines.determineDirection(c, ne));
		assertEquals("Direction should be SOUTH-EAST!", Direction.SE, Lines.determineDirection(c, se));
		assertEquals("Direction should be SOUTH-WEST!", Direction.SW, Lines.determineDirection(c, sw));
		assertEquals("Direction should be NORTH-WEST!", Direction.NW, Lines.determineDirection(c, nw));
	}
	
	
	/**
	 * Opens image on specified path. If exception raises, returns null.
	 * @param path
	 * @return
	 */
	private BufferedImage openImage(URL path) {
		File noLine = new File(path.getFile());
		try {
			BufferedImage image = ImageIO.read(noLine);
			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
