package examples.d3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import ui.ImFrame;
import utils.ImTool;
import utils.ImTool.CubeFace;
import utils.d3.LabelMatrix3D;
import utils.d3.RGBStruct;

public class GenerateFlatRGB {

	public static void main(String[] args) {
		
		/* RGB on XY face */
		RGBStruct rgbCubeXY = new RGBStruct(255, 255, 1);
		rgbCubeXY.linearPrint();
		
		int xLevels = rgbCubeXY.getxLevels();
		int yLevels = rgbCubeXY.getyLevels();
		int zLevels = rgbCubeXY.getzLevels();
		
		LabelMatrix3D labelMatrix3DXY = new LabelMatrix3D(xLevels, yLevels, zLevels);	
		HashMap<Integer, Color> lut = rgbCubeXY.getLUT();

		BufferedImage xyFace = ImTool.generateFaceofCube(labelMatrix3DXY, CubeFace.XY, lut);
		ImTool.show(xyFace, ImFrame.IMAGE_DEFAULT_SIZE, "RGB Cube face of x and y axis");
		
		/* RGB on XY face */
		RGBStruct rgbCubeXZ = new RGBStruct(255, 1, 255);
		rgbCubeXZ.linearPrint();
		
		LabelMatrix3D labelMatrix3DXZ = new LabelMatrix3D(rgbCubeXZ.getxLevels(), rgbCubeXZ.getyLevels(), rgbCubeXZ.getzLevels());	
		HashMap<Integer, Color> lut2 = rgbCubeXZ.getLUT();
		
		BufferedImage xzFace = ImTool.generateFaceofCube(labelMatrix3DXZ, CubeFace.XZ, lut2);
		ImTool.show(xzFace, ImFrame.IMAGE_DEFAULT_SIZE, "RGB Cube face of x and z axis");

		/* RGB on YZ face */
		RGBStruct rgbCubeYZ = new RGBStruct(1, 255, 255);
		rgbCubeYZ.linearPrint();
		
		LabelMatrix3D labelMatrix3DYZ = new LabelMatrix3D(rgbCubeYZ.getxLevels(), rgbCubeYZ.getyLevels(), rgbCubeYZ.getzLevels());	
		HashMap<Integer, Color> lut3 = rgbCubeYZ.getLUT();
		
		BufferedImage yzFace = ImTool.generateFaceofCube(labelMatrix3DYZ, CubeFace.YZ, lut3);
		ImTool.show(yzFace, ImFrame.IMAGE_DEFAULT_SIZE, "RGB Cube face of y and z axis");
	}
}
