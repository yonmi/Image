package utils.d2;

import java.awt.image.BufferedImage;

/**
 * Values of each pixel of an image can be of a various types.
 * This interface allows to gather a generic value of a pixel a the coords x and y.
 * @param <T>
 */
public interface ValueGetter<T> {
	
	/**
	 * Get a generic value of a pixel of an image.
	 * @param x horizontal axis coordinate.
	 * @param y vertical axis coordinate.
	 * @param img image to consider.
	 * @return a generic value that will be casted later.
	 */
	public T getValueFrom(int x, int y, BufferedImage img);
}
