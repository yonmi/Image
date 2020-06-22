package examples;

import java.awt.image.BufferedImage;

import ui.ImFrame;
import utils.ImTool;

/**
 * Example preparing and displaying the different views of an image using {@link ImTool}.
 * 
 * <p>
 * Views are:
 * <li> 0: the color image
 * <li> 1: the band 1
 * <li> 2: the band 2
 * <li> 3: the band 3
 * <li> ...
 * <li> minimized icon of the image
 *
 */
public class PrepareAndShowViews {

	public static void main(String[] args) {
		
		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		ImTool.initMinMaxValues(image);
		ImTool.prepareViewsFrom(image, ImTool.DEFAULT_RGB_ORDER);
		
		int viewId = 0;
		BufferedImage imageColorView = ImTool.getViewOf(image, viewId);
		ImTool.show(imageColorView, ImFrame.IMAGE_DEFAULT_SIZE, "Color image");
		
		BufferedImage imageColorIcon = ImTool.getIconViewOf(image, viewId);
		ImTool.show(imageColorIcon, ImFrame.IMAGE_REAL_SIZE, "Icon-color-"+ viewId);
		
		for(int i = 0; i < ImTool.getNbBandsOf(image); i++) {
			
			viewId = i + 1;
			
			BufferedImage view = ImTool.getViewOf(image, viewId);
			ImTool.show(view, ImFrame.IMAGE_DEFAULT_SIZE, "Band-"+ viewId);
			
			BufferedImage icon = ImTool.getIconViewOf(image, viewId);
			ImTool.show(icon, ImFrame.IMAGE_REAL_SIZE, "Icon-band-"+ viewId);
		}
		
	}
}
