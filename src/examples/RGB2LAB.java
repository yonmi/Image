package examples;

import java.awt.image.BufferedImage;

import ui.ImFrame;
import utils.ImTool;

public class RGB2LAB {
	
	public static void main(String[] args) {
		
		/* Simple exemple of the conversion for one pixel */
		int r = 255;
		int g = 5;
		int b = 100;
		
		double[] lab = ImTool.rgb2lab(r, g, b);
		
		System.out.println("r: "+ r +" -> l: "+ lab[0]);
		System.out.println("g: "+ g +" -> l: "+ lab[1]);
		System.out.println("b: "+ b +" -> l: "+ lab[2]);
		
		
		/* Conversion of the whole image */
		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		ImTool.show(image, ImFrame.IMAGE_DEFAULT_SIZE);
		
		BufferedImage imageLab = ImTool.rgb2lab(image);
		ImTool.show(imageLab, ImFrame.IMAGE_DEFAULT_SIZE);
	}
}
