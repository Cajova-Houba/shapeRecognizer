package org.zdenda.shapes.recognizer.core.lines;

import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdenda.shapes.recognizer.core.AreaAroundPoint;
import org.zdenda.shapes.recognizer.core.Direction;
import org.zdenda.shapes.recognizer.core.Pixel;

/**
 * Class containing methods to find lines in bitmap.
 * @author Zdenda
 *
 */
public class Lines {

	protected static Logger logger = LogManager.getLogger(Lines.class);
	
	/**
	 * This method will try to find a line in a bitmap. If line is found, then two points
	 * representing this line are returned. Otherwise empty array is returned.
	 * @param bitmap Bitmap on which search will be performed.
	 * @return Two points representing the line or empty array.
	 */
	public static Point[] findLine(Pixel[][] bitmap) {
		
		//check proper dimensions of bitmap
		if(bitmap == null) {
			logger.warn("Bitmap is null.");
			return new Point[0];
		}
		
		if(bitmap.length == 0) {
			logger.warn("Bitmap length is 0.");
			return new Point[0];
		}
		
		if(bitmap[0].length == 0) {
			logger.warn("One dimensional bitamp.");
			return new Point[0];
		}
		
		int w = bitmap[0].length;
		int h = bitmap.length;
		logger.debug("Finding line in bitmap "+w+" x "+h);
		
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
				
				if(p.isBlack()) {
					logger.debug("First point found at [{},{}].",j,i);
					first = new Point(j, i);
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
			return new Point[0];
		}
		
		//get the end point of line
		Point endPoint = findLineEnd(first, bitmap);
		
		if(endPoint == null) {
			logger.warn("No end point found for the first point = "+first.toString());
			endPoint = new Point(first);
		}
		
		logger.debug("Line from: {} to: {} found.",first,endPoint);
		return new Point[] {first, endPoint};
	}
	
	/**
	 * This method will try to find the end of the line in bitmap.
	 * If no other point besides the {@code firstPoint} is found, then
	 * null is returned.
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
	
}
