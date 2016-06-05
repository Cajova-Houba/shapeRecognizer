package org.zdenda.shapes.recognizer.core;

import java.awt.Point;

/**
 * Simple class representing line by two points.
 * @author Zdenda
 *
 */
public class Line {

	/**
	 * Starting point - first found.
	 */
	private Point start;
	
	/**
	 * End point.
	 */
	private Point end;

	
	public Line(Point start, Point end) {
		super();
		this.start = start;
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Line [start=" + start + ", end=" + end + "]";
	}

	
	
}
