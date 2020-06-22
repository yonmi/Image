package utils;

import java.awt.image.BufferedImage;

/**
 * Stores advanced attributes of an {@link BufferedImage image}.
 *
 * <p>
 * They are:
 * <li> directory
 * <li> max
 * <li> min
 * <li> name
 * <li> path
 *
 */
public class Attribs {

	/**
	 * As one image can have several view, the current view is the active one to show.
	 * 
	 * <p>
	 * The size of the buffers must not change.
	 * 
	 * <p>
	 * Examples of views are:
	 * <li> the color image view
	 * <li> each grayscale image corresponding to each bands
	 * <li> icon view
	 */
	public BufferedImage activetView;

	/**
	 * An array of vector has to store the original size and values of the different views.
	 * 
	 * <p>
	 * <li> The first buffer is the original sized image view.
	 * <li> The remainder of the array are the different original grayscale images views of each band. 
	 */
	public BufferedImage[] buffer;

	/**
	 * Directory containing the image.
	 */
	public String directory;
	
	/**
	 * Indicates if the image has been equalized or not.
	 */
	public boolean equalized;

	/**
	 * Array of different icons representing each view.
	 *
	 * <p>
	 * <li> The first view corresponds to the icon of the color image.
	 * <li> The remainder of the array corresponds to the icons of the different grayscale images views of each band. 
	 */
	public BufferedImage[] iconView;

	/**
	 * Matrix used for pixel labeling. 
	 */
	public LabelMatrix labelMatrix;

	/**
	 * Storing each maximum grayscale value for each band. 
	 */
	public double[] max;

	/**
	 * Storing each minimum grayscale values for each band. 
	 */
	public double[] min;

	/**
	 * Indicates if the min and max values of image has been previously saved.
	 */
	public boolean minMaxPrepared;

	/**
	 * Name of the image with its extension.
	 */
	public String name;
	
	/**
	 * Path combining the directory and the name of the image.
	 */
	public String path;
	
	/**
	 * Indicates if the image has been registered or not.
	 */
	public boolean registered;

	/**
	 * Array of different updated views to show.
	 * 
	 * <p> 
	 * The size of the different views may be changed according to a zoom application.
	 * The color intensity may also vary if a special treatment (e.g. equalization) is done on the image. 
	 * <p>
	 * <li> The first view corresponds to the color image.
	 * <li> The remainder of the array corresponds to the different grayscale images views of each band. 
	 */
	public BufferedImage[] view;

	/**
	 * Creates an empty set of attributes.
	 * 
	 */
	public Attribs() {}
}
