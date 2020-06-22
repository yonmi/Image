package examples;

import java.awt.image.BufferedImage;

import utils.ImTool;

/**
 * Example drawing some rectangles on an image using {@link ImTool}.
 */
public class DrawRectangleOnImage {

	public static void main(String[] args) {

		/* Load the image */
		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		
		/* Green rectangle parameters */
		int minX1 = 89;
		int maxX1 = 450;
		int minY1 = 50;
		int maxY1 = 420;
		int color1 = ImTool.RGB_GREEN;
		ImTool.drawRectangleOn(image, minX1, maxX1, minY1, maxY1, color1);
		
		/* Random color rectangle parameters */
		int minX2 = 261;
		int maxX2 = 379;
		int minY2 = 263;
		int maxY2 = 303;
		int color2 = ImTool.generateRandomColorRGBValue();
		ImTool.drawRectangleOn(image, minX2, maxX2, minY2, maxY2, color2);
		
		/* Showing the result */
		ImTool.show(image, 50, "Drawing rectangles");
	}
}
