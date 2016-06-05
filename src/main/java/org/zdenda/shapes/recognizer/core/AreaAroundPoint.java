package org.zdenda.shapes.recognizer.core;

import java.awt.Point;
import java.util.Iterator;

/**
 * Class which will iterate over points around center point (3x3 area).
 * @author Zdenda
 *
 */
public class AreaAroundPoint implements Iterator<Point> {

	/**
	 * Center point.
	 */
	private Point center;
	
	/**
	 * Current direction.
	 */
	private Direction currentDirection;
	
	/**
	 * A direction in which iteration will be performed can be specified.
	 */
	private Direction direction;
	
	/**
	 * Constructor with center point. Points within 3x3 area around this point will be itarated over, starting from the EAST
	 * direction, clockwise. 
	 * @param center Center point. If null, exception is thrown.
	 */
	public AreaAroundPoint(Point center) {
		super();
		if(center == null) {
			throw new IllegalArgumentException("Center point can't be null!");
		}
		this.center = center;
		this.currentDirection = Direction.E;
		this.direction = Direction.NONE;
	}
	
	/**
	 * Constructor with center point and direction. Three points in the particular direction will be iterated over.
	 * Every direction has its own number. The three points will have those directions: direction, direction-1, direction+1.
	 * 
	 * @param center Center point.
	 * @param direction Direction from the center point.
	 */
	public AreaAroundPoint(Point center, Direction direction) {
		this(center);
		this.center = center;
		this.direction = direction;
		this.currentDirection = direction;
	}

	public boolean hasNext() {
		return currentDirection != Direction.NONE;
	}

	public Point next() {
		//no direction specified by user
		if(direction == Direction.NONE) {
			return wholeArea();
		}
		
		//user had specified direction
		//return points with according to direction in this order direction, direction-1, direction+1
		
		Point p = currentDirection.getPoint();
		Point res = new Point(center.x+p.x, center.y+p.y);
		//direction
		if(currentDirection == direction) {
			currentDirection = direction.previousDirection();
			return res;
		}
		
		//direction -1
		else if(currentDirection == direction.previousDirection()) {
			currentDirection = direction.nextDirection();
			return res;
		}
		
		//direction +1 - that's the last one
		else if(currentDirection == direction.nextDirection()) {
			currentDirection = Direction.NONE;
			return res;
		} 
		
		//this shouldn't happen
		else {
			return null;
		}
		
	}

	/**
	 * Iterates over the whole 3x3 area. Standard with no direction specified by user.
	 * @return
	 */
	private Point wholeArea() {
		switch (currentDirection) {
		case E:
		case SE:
		case S:
		case SW:
		case W:
		case NW:
			Point p = currentDirection.getPoint();
			currentDirection = currentDirection.nextDirection();
			return new Point(center.x+p.x,center.y+p.y);
		case N:
			currentDirection = Direction.NONE;
			return new Point(center.x,center.y-1);
			
		// shouldn't happen
		default:
			return null;
		}
	}
}
