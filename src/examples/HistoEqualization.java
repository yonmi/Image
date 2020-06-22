package examples;

import java.awt.image.BufferedImage;

import ui.ImFrame;
import utils.ImTool;

/**
 * Example of an image histogram equalization using {@link ImTool}.
 *
 */
public class HistoEqualization {

	public static void main(String[] args) {

		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		ImTool.show(image, ImFrame.IMAGE_DEFAULT_SIZE);
		
		ImTool.equalize(image);
		ImTool.show(image, ImFrame.IMAGE_DEFAULT_SIZE, "Equalized");
	}

}
