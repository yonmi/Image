package examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import ui.ImFrame;
import utils.ImTool;
import utils.LabelMatrix;

/**
 * Example generating an image of regions from a label matrix an a LUT using {@link ImTool}.
 *
 */
public class GenerateRegions {

	public static void main(String[] args) {

		int width = 3;
		int height = 3;
		
		/* Example 1: each pixel is considered as a region */
		LabelMatrix labelMatrix1 = new LabelMatrix(width, height);		
		HashMap<Integer, Color> lut = new HashMap<Integer, Color>();

		BufferedImage pixelRegions = ImTool.generateRegions(labelMatrix1, lut);
		ImTool.show(pixelRegions, ImFrame.IMAGE_DEFAULT_SIZE, "Pixel regions");
		
		/* Example 2: manual definition of regions */
		LabelMatrix labelMatrix2 = new LabelMatrix();
		int[][] labels = {{0, 0, 1},{0, 1, 2},{0, 1, 2}};
		labelMatrix2.setLabels(labels);
		
		BufferedImage manualRegions = ImTool.generateRegions(labelMatrix2, lut);
		ImTool.show(manualRegions, ImFrame.IMAGE_DEFAULT_SIZE, "Manual regions");
	}

}
