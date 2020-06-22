package examples;

import java.awt.image.BufferedImage;

import ui.ImFrame;
import utils.ImTool;

/**
 * Example loading and displaying an image using {@link ImTool}.
 * 
 * <p>
 * N.B. The maximum number of bands here can be more than 3 bands; 
 * 
 * <p>
 * The min and max values of the pixels for each bands should be first initialized before preparing the views of the image. 
 * 
 */
public class LoadAndShow_Xbands {

	public static void main(String[] args) {
		
		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		ImTool.initMinMaxValues(image);
		ImTool.prepareViewsFrom(image, ImTool.DEFAULT_RGB_ORDER);
		ImTool.show(ImTool.getActiveViewOf(image), ImFrame.IMAGE_DEFAULT_SIZE);
	}
}
