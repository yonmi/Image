package examples.d3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import ui.ImFrame;
import utils.ImTool;
import utils.ImTool.CubeFace;
import utils.d3.LabelMatrix3D;
import utils.d3.RGBStruct;

public class GenerateRGBCube {

	public static void main(String[] args) {
		
		RGBStruct rgbCube = new RGBStruct();
		//rgbCube.linearPrint();
		
		int xLevels = rgbCube.getxLevels();
		int yLevels = rgbCube.getyLevels();
		int zLevels = rgbCube.getzLevels();
		
		LabelMatrix3D labelMatrix3D = new LabelMatrix3D(xLevels, yLevels, zLevels);	
		HashMap<Integer, Color> lut = rgbCube.getLUT();

		BufferedImage xyFace = ImTool.generateFaceofCube(labelMatrix3D, CubeFace.XY, lut);
		ImTool.show(xyFace, ImFrame.IMAGE_DEFAULT_SIZE, "RGB Cube face of x and y axis");
		
		BufferedImage xzFace = ImTool.generateFaceofCube(labelMatrix3D, CubeFace.XZ, lut);
		ImTool.show(xzFace, ImFrame.IMAGE_DEFAULT_SIZE, "RGB Cube face of x and z axis");

		BufferedImage yzFace = ImTool.generateFaceofCube(labelMatrix3D, CubeFace.YZ, lut);
		ImTool.show(yzFace, ImFrame.IMAGE_DEFAULT_SIZE, "RGB Cube face of y and z axis");
	}
}
