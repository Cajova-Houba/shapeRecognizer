package org.zdenda.shapes.recognizer.core;

import java.awt.Point;

/**
 * <p>
 * Possible directions in 2D space.
 * Every point has a number assigned, those numbers are from 1 to 8 (0 for NONE) and you can cycle them
 * using {@code nextDirection()} and {@code previousDirection()} methods. 
 * </p>
 * 
 * <p>
 * Every direction has also a point symbolizing that direction from [0,0] assigned.
 * Example: <br>
 * NORTH - [0,-1] <br>
 * SOUTH - [0,-] <br>
 * NONE - [0,0] <br>
 * </p>
 * 
 * @author Zdenda
 *
 */
public enum Direction {

	/**
	 * North.
	 */
	N(1, new Point(0,-1)),
	
	/**
	 * North-east.
	 */
	NE(2, new Point(1,-1)),
	
	/**
	 * East.
	 */
	E(3, new Point(1,0)),
	
	/**
	 * South-east.
	 */
	SE(4, new Point(1,1)),
	
	/**
	 * South.
	 */
	S(5, new Point(0,1)),
	
	/**
	 * South-west.
	 */
	SW(6, new Point(-1,1)),
	
	/**
	 * West.
	 */
	W(7, new Point(-1,0)),
	
	/**
	 * North-west.
	 */
	NW(8, new Point(-1,-1)),
	
	/**
	 * No direction.
	 */
	NONE(0, new Point(0,0));
	
	public static final int MAX_VAL = 8; 
	
	/**
	 * Integer value.
	 */
	private final int val;
	
	/**
	 * The point which would be in the direction from center.
	 * Example: 
	 * NORTH - [0,-1]
	 * SOUTH - [0,-]
	 * NONE - [0,0]
	 */
	private final Point point;

	public static Direction getDirection(int val) {
		switch(val) {
		case 0: return NONE;
		case 1: return N;
		case 2: return NE;
		case 3: return E;
		case 4: return SE;
		case 5: return S;
		case 6: return SW;
		case 7: return W;
		case 8: return NW;
		default: return NONE;
		}
	}
	
	private Direction(int val, Point point) {
		this.val = val;
		this.point = point;
	}
	
	public int getVal() {
		return val;
	}
	
	public Point getPoint() {
		return point;
	}

	/**
	 * Returns the next direction. Directions are ordered clockwise from N to NW.
	 * If the direciton is NONE, NONE is returned.
	 * @return
	 */
	public Direction nextDirection() {
		if(getVal() == 0) {
			return Direction.NONE;
		} 
		
		int next = getVal()+1;
		if(next > MAX_VAL) {
			next -= MAX_VAL;
		}
		
		return getDirection(next);
	}
	
	/**
	 * Returns the previsous direction. Directions are ordered clockwise from N to NW.
	 * If the direciton is NONE, NONE is returned.
	 * @return
	 */
	public Direction previousDirection() {
		if(getVal() == 0) {
			return Direction.NONE;
		}
		
		int prev = getVal() -1;
		if(prev < 1) {
			prev += MAX_VAL;
		}
		
		return getDirection(prev);
	}
	
}
