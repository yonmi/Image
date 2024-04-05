package examples.d2.cooccurrence;

import java.awt.Color;
import java.awt.image.BufferedImage;

import utils.ImTool;
import utils.d2.ValueGetter;
import utils.d2.cooccurrence.CooccurrenceMatrix;

/**
 * Example of code allowing to get a cooccurrence matrix (histogram) from an image.
 * The horizontal axis of the histogram corresponds to the couples of values contained by pixel neighbors.
 * The vertical axis counts the number of occurrences where couples of values are met.
 */
public class XpCooccurrenceMatrix {

	public static void main(String[] args) {
		
		/* Prepare an example image
		 * -------------------
		 * |  0  |  0  | 255 |
		 * -------------------
		 * |  0  | 255 |  0  |
		 * -------------------
		 * | 255 | 255 |  0  |
		 * -------------------
		 * 
		 * Value Set: <0, 255>
		 *  */
		BufferedImage img = XpCooccurrenceMatrix.generateImgExample();
		ImTool.show(img, 50, "Image test");
		
		/* 1. Prepare the cooccurrence matrix */
		int redBand = 0;
		int connexity = 4; // neighbors to consider
		
		/* 2. Prepare a value getter since a generic value can be considered */
		ValueGetter<Double> valueGetter = (x, y, image)->{
			
			// Precise here how to get the value from the image
			return ImTool.getPixelValue(x, y, redBand, image);
		}; 
		CooccurrenceMatrix<Double> cooccMat = new CooccurrenceMatrix<Double>(img, redBand, connexity, valueGetter);

		/* 3. The cooccurrence matrix should be
		 * 	     0  255
		 *     ---------
		 *  0  | 3 | 7 |
		 *     ---------
		 * 255 | 7 | 2 |
		 *     ---------
		 *  */
		cooccMat.print();
	}

	/**
	 * Just generate an image for the test.
	 * @return a specific generated image.
	 */
	private static BufferedImage generateImgExample() {

		int width = 3;
		int height = 3;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Color white = new Color(255, 255, 255);
		Color black = new Color(0, 0, 0);
		System.out.println("white r: "+ white.getRed() +", g: "
									  + white.getGreen() +", b: "
									  + white.getBlue());
		System.out.println("black r: "+ black.getRed() +", g: "
									  + black.getGreen() +", b: "
									  + black.getBlue());
		
		/* 1st column */
		img.setRGB(0, 0, black.getRGB());
		img.setRGB(0, 1, black.getRGB());
		img.setRGB(0, 2, white.getRGB());
		
		/* 2nd column */
		img.setRGB(1, 0, black.getRGB());
		img.setRGB(1, 1, white.getRGB());
		img.setRGB(1, 2, white.getRGB());
		
		/* 3rd column */
		img.setRGB(2, 0, white.getRGB());
		img.setRGB(2, 1, black.getRGB());
		img.setRGB(2, 2, black.getRGB());

		return img;
	}

}

