package examples;

import java.awt.image.BufferedImage;

import utils.ImTool;

/**
 * Example saving an image using {@link ImTool}.
 * 
 * <p> 
 * Saves an image as a .png format.
 *
 */
public class SaveImage {

	public static void main(String[] args) {

		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		ImTool.save(image, "xp//examples//lena-saved.png");
	}
}
