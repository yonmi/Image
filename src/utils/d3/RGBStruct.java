package utils.d3;

import java.awt.Color;
import java.util.HashMap;

public class RGBStruct {
	
	private int xLevels = 256;
	private int yLevels = 256;
	private int zLevels = 256;
	private Color[][][] structure;
	private HashMap<Integer, Color> lut = null;

	public RGBStruct() {
		
		this.init();
	}
	
	/**
	 * Create a 3D RGB structure.
	 * 
	 * @param levels Maximum number of values on each axis.
	 */
	public RGBStruct(int levels) {
		
		this.xLevels = levels;
		this.yLevels = levels;
		this.zLevels = levels;		
		this.init();		
	}
	
	public RGBStruct(int xLevels, int yLevels, int zLevels) {
		
		this.xLevels = xLevels;
		this.yLevels = yLevels;
		this.zLevels = zLevels;		
		this.init();		
	}
	
	private void init() {
		
		this.structure = new Color[this.xLevels][this.yLevels][this.zLevels];
		for(int y = 0; y < this.yLevels; y++) {
			for(int x = 0; x < this.xLevels; x++) {
				for(int z = 0; z < this.zLevels; z++) {
					
					this.structure[x][y][z] = new Color(x, y, z);
				}
			}
		}
	}
	
	public HashMap<Integer, Color> getLUT() {
		
		if(this.lut == null) {
			
			this.generateLUT();
		}
		
		return this.lut;
	}
	
	private void generateLUT() {
		
		this.lut = new HashMap<Integer, Color>();
		int label = 0;
		for(int y = 0; y < this.yLevels; y++) {
			for(int x = 0; x < this.xLevels; x++) {
				for(int z = 0; z < this.zLevels; z++) {
					
					this.lut.put(label++, this.structure[x][y][z]);
				}
			}
		}
	}

	public Color[][][] getStructure() {
		
		return this.structure;
	}
	
	public void linearPrint() {
		
		int nb = 1;
		for(int y = 0; y < this.yLevels; y++) {
			for(int x = 0; x < this.xLevels; x++) {
				for(int z = 0; z < this.zLevels; z++) {
					
					System.out.println("[RGBCube] x: "+ x +" y: "+ y +" z: "+ z +" Color: <"+ this.structure[x][y][z].toString() +"> | nb: "+ nb++);
				}
			}
		}		
	}

	public int[] getLevels() {

		int[] levels = new int[3];
		levels[0] = this.xLevels;
		levels[1] = this.yLevels;
		levels[2] = this.zLevels;
		
		return levels;
	}

	public double getGrayValueOfBand(int b, int x, int y, int z) {
		
		switch(b) {
		case 0:
			return this.structure[x][y][z].getRed();
		case 1:
			return this.structure[x][y][z].getGreen();
		case 2:
			return this.structure[x][y][z].getBlue();
		default:
			return this.structure[x][y][z].getRed();			
		}
	}
	
	public int getxLevels() {
		return xLevels;
	}

	public int getyLevels() {
		return yLevels;
	}

	public int getzLevels() {
		return zLevels;
	}
}