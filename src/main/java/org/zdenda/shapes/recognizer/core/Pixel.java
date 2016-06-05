package org.zdenda.shapes.recognizer.core;

/**
 * This class represents one pixel.
 * @author Zdenda
 *
 */
public class Pixel {

	private int r;
	private int g;
	private int b;
	
	/**
	 * Constructor with all color components.
	 * @param r
	 * @param g
	 * @param b
	 */
	public Pixel(int r, int g, int b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Use the value returned by {@code getRgb()} method of {@code BufferedImage}.
	 * @param color
	 */
	public Pixel(int color) {
		//red is the last 8 bits
		r = (color >> 16) & 0x000000FF;
		
		//green is the middle 8 bits
		g = (color >> 8) & 0x000000FF;
		
		//blue is the first 8 bits
		b = color & 0x000000FF;
	}
	
	/**
	 * Returns true if the color represented by r,g,b fields is white.
	 * @return
	 */
	public boolean isWhite() {
		return (r == 255) && (g == 255) && (b == 255);
	}
	
	/**
	 * Returns true if the color represented by r,g,b fields is black.
	 * @return
	 */
	public boolean isBlack() {
		return (r == 0) && (g == 0) && (b == 0);
	}
	
	/**
	 * Returns color representation of this pixel suitable for
	 * {@code setRgb()} method of {@code BufferedImage}.
	 * @return
	 */
	public int toInt() {
		int rgb = 0;
		
		
		//masking shouldn't be necessary, but i will
		//still use it in case r,g,b are not valid (2 byte) values
		//last 8 bits is red color
		rgb += (r << 16) & 0x00FF0000;
		
		//middle 8 bits is green color
		rgb += (g << 8) & 0x0000FF00;
		
		//first 8 bits is blue color.
		rgb += b;
		
		return rgb;
	}
	
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "Pixel [r=" + r + ", g=" + g + ", b=" + b + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pixel other = (Pixel) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}
	
	
	
	
}
