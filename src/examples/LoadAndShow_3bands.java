package examples;

import java.awt.image.BufferedImage;

import ui.ImFrame;
import utils.ImTool;

/**
 * Example loading and displaying an image using {@link ImTool}.
 * 
 * <p>
 * N.B. The maximum number of bands here are <= 3; Otherwise an IllegalArgument will be thrown by {@link ImTool#show(BufferedImage)}.
 * 
 */
public class LoadAndShow_3bands {

	public static void main(String[] args) {
		
		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		ImTool.show(image, ImFrame.IMAGE_DEFAULT_SIZE);
	}
}
