package org.zdenda.shapes.recognizer.core.lines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdenda.shapes.recognizer.core.AreaAroundPoint;
import org.zdenda.shapes.recognizer.core.Direction;
import org.zdenda.shapes.recognizer.core.Line;
import org.zdenda.shapes.recognizer.core.Pixel;

/**
 * Class containing methods to find lines in bitmap.
 * @author Zdenda
 *
 */
public class Lines {

	protected static Logger logger = LogManager.getLogger(Lines.class);
	
	/**
	 * Black points that were already visited.
	 * Integer values is counted as a hash code of the coordinates.
	 * 
	 */
	protected static Set<Integer> visitedPoints;
	
	/**
	 * This method will try to find every line in a bitmap. If no line is found, empty list is returned.
	 * If the bitmap is null or dimensions are invalid (one of dimensions is 0), null is returned.
	 * 
	 * @param bitmap Bitmap on which search will be performed.
	 * @return List with lines.
	 */
	public static List<Line> findLines(Pixel[][] bitmap) {
		//check proper dimensions of bitmap
		if(bitmap == null) {
			logger.warn("Bitmap is null.");
			return null;
		}
		
		if(bitmap.length == 0) {
			logger.warn("Bitmap length is 0.");
			return null;
		}
		
		if(bitmap[0].length == 0) {
			logger.warn("One dimensional bitamp.");
			return null;
		}
		
		visitedPoints = new HashSet<Integer>();
		List<Line> lines = new ArrayList<Line>();
		int w = bitmap[0].length;
		int h = bitmap.length;
		logger.debug("Finding lines in bitmap "+w+" x "+h);
		
		
		//this version works only with black & white (white as background) so the
		//first point is first black pixel.
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				Pixel p = bitmap[i][j];
				if(p == null) {
					logger.warn("Pixel [{},{}] is null.",j,i);
					continue;
				}
				
				int hash = getCodeForCoordinates(j, i, p);
				//first point must be black and not visited yet
				if(p.isBlack() && !visitedPoints.contains(hash)) {
					logger.debug("First point found at [{},{}].",j,i);
					Point firstPoint = new Point(j, i);
					visitedPoints.add(hash);
					
					Point secondPoint = findLineEnd(firstPoint, bitmap);
					if(secondPoint == null) {
						logger.warn("No second point found for point: {}.",firstPoint);
						secondPoint = firstPoint;
					}
					lines.add(new Line(firstPoint, secondPoint));
					
				}
			}
		}
		
		return lines;
	}
	
	/**
	 * This method will try to find a line in a bitmap. If line is found, then object representing this line is returned. 
	 * Otherwise null is returned.
	 * @param bitmap Bitmap on which search will be performed.
	 * @return Two points representing the line or null.
	 */
	public static Line findLine(Pixel[][] bitmap) {
		
		//check proper dimensions of bitmap
		if(bitmap == null) {
			logger.warn("Bitmap is null.");
			return null;
		}
		
		if(bitmap.length == 0) {
			logger.warn("Bitmap length is 0.");
			return null;
		}
		
		if(bitmap[0].length == 0) {
			logger.warn("One dimensional bitamp.");
			return null;
		}
		
		int w = bitmap[0].length;
		int h = bitmap.length;
		logger.debug("Finding line in bitmap "+w+" x "+h);
		
		visitedPoints = new HashSet<Integer>();
		Point first = null;
		
		//find the first point
		//this version works only with black & white (white as background) so the
		//first point is first black pixel.
		boolean stop = false;
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				Pixel p = bitmap[i][j];
				if(p == null) {
					logger.warn("Pixel [{},{}] is null.",j,i);
					continue;
				}
				
				int hash = getCodeForCoordinates(j, i, p);
				//first point must be black and not visited yet
				if(p.isBlack() && !visitedPoints.contains(hash)) {
					logger.debug("First point found at [{},{}].",j,i);
					first = new Point(j, i);
					visitedPoints.add(hash);
					stop = true;
					break;
				}
			}
			
			if(stop) {
				break;
			}
		}
		
		//if no first point of line is found, return empty array
		if(first == null) {
			logger.debug("No line found.");
			return null;
		}
		
		//get the end point of line
		Point endPoint = findLineEnd(first, bitmap);
		
		if(endPoint == null) {
			logger.warn("No end point found for the first point = "+first.toString());
			endPoint = new Point(first);
		}
		
		logger.debug("Line from: {} to: {} found.",first,endPoint);
		return new Line(first, endPoint);
	}
	
	/**
	 * This method will try to find the end of the line in bitmap.
	 * If no other point besides the {@code firstPoint} is found, then
	 * null is returned. {@code visitedPoints} must be initialized, before
	 * calling this method.
	 * 
	 * It is assumed that bitmap is checked for valid dimensions.
	 * 
	 * @param firstPoint Starting point of the line.
	 * @param bitmap Bitmap on which search will be performed.
	 * @return
	 */
	private static Point findLineEnd(Point firstPoint, Pixel[][] bitmap) {
		
		if(firstPoint == null) {
			logger.warn("First point is null.");
			return null;
		}
		
		logger.debug("Finding the end point from starting point: {}",firstPoint);
		
		//find the next point a determine the direction of the line
		Direction direction;
		Point nextPoint = findNextPoint(firstPoint, bitmap, Direction.NONE);
		if(nextPoint == null) {
			logger.warn("No other point besides the first point found.");
			return null;
		}
		direction = determineDirection(firstPoint, nextPoint);
		logger.debug("The scond point is: {}, direction: {}.", nextPoint,direction);
		
		//continue with finding points in the current direction
		Point lineEnd = nextPoint;
		while(nextPoint != null) {
			lineEnd = nextPoint; //last non-null point
			int hash = getCodeForCoordinates(nextPoint.x, nextPoint.y, bitmap[nextPoint.y][nextPoint.x]);
			visitedPoints.add(hash);

			nextPoint = findNextPoint(nextPoint, bitmap, direction);
			logger.trace("Next point is: {}.",nextPoint);
		}
		
		return lineEnd;
	}
	
	/**
	 * Tries to find the next line point in the 3x3 area around the current point.
	 * Actual area is determined by direction parameter.
	 * 
	 * If no point is found, null is returned. Bitmap is expected to have been checked
	 * for valid dimensions.
	 * 
	 * @param curPoint Current point.
	 * @param bitmap Bitmap.
	 * @param direction Direction of line. If NONE is used, whole 3x3 area will be searched for the next point. 
	 * @return Next point.
	 */
	public static Point findNextPoint(Point curPoint, Pixel[][] bitmap, Direction direction) {
		logger.trace("Finding next point from: {} in direction: {}",curPoint, direction);
		int w = bitmap[0].length;
		int h = bitmap.length;
		
		if(direction == Direction.NONE) {
			//go through all points around the curPoint
			AreaAroundPoint pointsAround = new AreaAroundPoint(curPoint);
			while(pointsAround.hasNext()) {
				Point next = pointsAround.next();
				if(next.x < 0 || next.x >= w || next.y < 0 || next.y >= h) {
					continue;
				}
				
				logger.trace("Checking point: {}.",next);
				if(bitmap[next.y][next.x].isBlack()) {
					logger.trace("Next point found: {}.",next);
					return next;
				}
			}
		} else {
			//iterate through points and try to find out next point
			AreaAroundPoint pointsAround = new AreaAroundPoint(curPoint, direction);
			while(pointsAround.hasNext()) {
				Point next = pointsAround.next();
				if(next.x < 0 || next.x >= w || next.y < 0 || next.y >= h) {
					continue;
				}
				
				logger.trace("Checking point: {}.",next);
				if(bitmap[next.y][next.x].isBlack()) {
					logger.trace("Next point found: {}", next);
					return next;
				}
			}
		}
		
		
		logger.warn("No next point found.");
		return null;
	}
	
	/**
	 * Determines the direction from the first point to the second point. Y coordinate is expected to get bigger
	 * from top to bottom.
	 * @param firstPoint First point. If null, NONE is returned.
	 * @param secondPoint Second point. If null, NONE is returned.
	 * @return Direction. Direction from the first point to the second point. If the points are same, NONE is returned.
	 */
	public static Direction determineDirection(Point firstPoint, Point secondPoint) {
		if(firstPoint == null) {
			logger.warn("First point is null.");
			return Direction.NONE;
		}
		
		if (secondPoint == null) {
			logger.warn("Second point is null.");
			return Direction.NONE;
		}
		
		int dx = secondPoint.x - firstPoint.x;
		int dy = secondPoint.y - firstPoint.y;
		
		//N,S
		if(dx == 0) {
			if(dy == 0) {
				logger.warn("Points are same.");
				return Direction.NONE;
			} else if (dy > 0) {
				return Direction.S;
			} else {
				return Direction.N;
			}
		}
		
		//NW,W,SW
		else if(dx < 0) {
			if(dy == 0) {
				return Direction.W;
			} else if(dy > 0) {
				return Direction.SW;
			} else {
				return Direction.NW;
			}
		}
		//NE,E,SE
		else {
			if(dy == 0) {
				return Direction.E;
			} else if(dy > 0) {
				return Direction.SE;
			} else {
				return Direction.NE;
			}
		}
	}
	
	/**
	 * Returns the hash code for pixel. This code also counts the coordinates of pixel.
	 * @param x X value.
	 * @param y Y value.
	 * @param p Pixel, null safe.
	 * @return Hash code.
	 */
	private static int getCodeForCoordinates(int x, int y, Pixel p) {
		int code = 7;
		code = code * 71 + x;
		code = code * 71 + y;
		code = code * 71 + (p == null ? 0 : p.hashCode());
		
		return code;
	}
}
