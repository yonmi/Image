/****************************************************************************
* Copyright AGAT-Team (2014)						       
* 									    
* Contributors:								
* J.F. Randrianasoa							    
* K. Kurtz								    
* E. Desjardin								    
* N. Passat								    
* 									    
* This software is a computer program whose purpose is to [describe	    
* functionalities and technical features of your software].		    
* 									    
* This software is governed by the CeCILL-B license under French law and    
* abiding by the rules of distribution of free software.  You can  use,     
* modify and/ or redistribute the software under the terms of the CeCILL-B  
* license as circulated by CEA, CNRS and INRIA at the following URL	    
* "http://www.cecill.info". 						    
* 									    
* As a counterpart to the access to the source code and  rights to copy,    
* modify and redistribute granted by the license, users are provided only   
* with a limited warranty  and the software's author,  the holder of the    
* economic rights,  and the successive licensors  have only  limited	    
* liability. 								    
* 									    
* In this respect, the user's attention is drawn to the risks associated    
* with loading,  using,  modifying and/or developing or reproducing the     
* software by the user in light of its specific status of free software,    
* that may mean  that it is complicated to manipulate,  and  that  also	   
* therefore means  that it is reserved for developers  and  experienced     
* professionals having in-depth computer knowledge. Users are therefore     
* encouraged to load and test the software's suitability as regards their   
* requirements in conditions enabling the security of their systems and/or  
* data to be ensured and,  more generally, to use and operate it in the     
* same conditions as regards security. 					    
*								            
* The fact that you are presently reading this means that you have had	    
* knowledge of the CeCILL-B license and that you accept its terms.          
* 									   		
* The full license is in the file LICENSE, distributed with this software.  
*****************************************************************************/

package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

import examples.d2.LoadAndShow_Xbands;
import ui.ImFrame;
import utils.d2.Attribs;
import utils.d2.LabelMatrix;
import utils.d3.Attribs3D;
import utils.d3.LabelMatrix3D;
import utils.d3.RGBStruct;
import utils.d3.Voxel;

/**
 * A tool simplifying the management of {@link BufferedImage}.
 * 
 * <p>
 * The core utilities are :
 * <li> {@link ImTool#read(String) loading an image using its path }
 * <li> {@link ImTool#show(BufferedImage) displaying an image in a frame } 
 * 
 */
public class ImTool {
	
	/**
	 * Attribs set to an image if it is not registered.
	 */
	private static final Attribs DEFAULT_ATTRIBS = new Attribs();
	
	/**
	 * Attribs set to a RGBCube if it is not registered.
	 */
	private static final Attribs3D DEFAULT_ATTRIBS_3D = new Attribs3D();
	
	/**
	 * The bands order could vary according to the type of the image.
	 * 
	 * <p>
	 * By default:
	 * <li> Red: 0
	 * <li> Green: 1
	 * <li> Blue: 2
	 */
	public static final int[] DEFAULT_RGB_ORDER = new int[] {0, 1, 2};

	/**
	 * Predefined Gray scale LUT.
	 */
	public static HashMap<Integer, Color> grayScaleLUT = null;

	/**
	 * Precised height of the icon view.
	 */
	public static final int ICON_HEIGHT = 60;

	/**
	 * Precised width of the icon view.
	 */
	public static final int ICON_WIDTH = 60;

	/**
	 * Contains some advanced attributes of the images.
	 */
	private static HashMap<BufferedImage, Attribs> register = new HashMap<BufferedImage, Attribs>();
	
	/**
	 * Contains some advanced attributes of the RGBCube.
	 */
	private static HashMap<RGBStruct, Attribs3D> register3D = new HashMap<RGBStruct, Attribs3D>();
	
	/**
	 * Green combined RGB value.
	 */
	public static final int RGB_GREEN = 0*65536+255*256+0;
	
	/**
	 * Axes determining a face of a cube
	 */
	public enum CubeFace {
		
		XY,
		XZ,
		YZ
	}

	/**
	 * Computes the value corresponding to the (percentage %) of maxValue.
	 * 
	 * <p>
	 * Example:
	 * <pre>
	 * 6% of 100 is 60
	 * <pre/>
	 * 
	 * @param maxValue corresponding to 100%
	 * @param percentage of the value to find
	 * @return the application of the percentage to the maxValue
	 */
	public static int applyPercentage(int maxValue, int percentage) {
		
		return (int) Math.ceil(maxValue * 0.01 * percentage);
	}

	/**
	 * Increases the global contrast of an image by rolling out its histogram.
	 * 
	 * <p>
	 * An uniform repartition of the histogram on all the gray levels is done.
	 * 
	 * @param image having the histogram to modify; should not be null
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static void equalize(BufferedImage image) {

		int nbBands = ImTool.getNbBandsOf(image);
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		ArrayList<TreeMap<Double, Integer>> histogram = new ArrayList<TreeMap<Double, Integer>>();
		double minValue[] = new double[nbBands];
		double maxValue[] = new double[nbBands];
		
		/* Check if the image is already prepared enough */
		Attribs attribs = ImTool.is(image);
		boolean isRegistered = attribs.registered;
		boolean isPrepared = isRegistered && attribs.minMaxPrepared;
		if(!isPrepared) {
			
			attribs.min = new double[nbBands];
			attribs.max = new double[nbBands];
		}		
		/* Prepare an histogram for each band */
		for(int b = 0; b < nbBands; b++) {
			
			histogram.add(new TreeMap<Double, Integer>());
			minValue[b] = Double.MAX_VALUE;
			maxValue[b] = Double.MIN_VALUE;
		}
		
		/* Fill the histograms */
		for(int b = 0; b < nbBands; ++b) {		
			for(int y = 0; y < imageHeight; ++y) {
				for(int x = 0; x < imageWidth; ++x) {
					
					double pixelValue = ImTool.getPixelValue(x, y, b, image);
					
					if(pixelValue < minValue[b]) {
						
						minValue[b] = pixelValue;
					}
					
					if(pixelValue > maxValue[b]) {
						
						maxValue[b] = pixelValue;
					}
					
					Integer count = 1;
					if(histogram.get(b).containsKey(pixelValue)) {
						
						count = (Integer) histogram.get(b).get(pixelValue) + 1;
					}
					
					histogram.get(b).put(pixelValue, count);
				}
			}
		}
		
		/* Compute the equalized histogram and apply it the image */
		for(int b = 0; b < nbBands; ++b) {
			
			HashMap<Double, Double> newValues = new HashMap<Double, Double>();
			TreeMap<Double, Integer> histoPerBand = histogram.get(b);
			
			/* Computation */
			for(Entry<Double, Integer> entry: histoPerBand.entrySet()) {
				
				Double pixelValue = entry.getKey();
				Integer pixelCount = entry.getValue();
				
				int sum = histoPerBand.headMap(pixelValue).values().stream().mapToInt(Integer::intValue).sum();
				sum += pixelCount;
				
				double newValue = (sum * maxValue[b]) / ImTool.getNbPixelsOf(image);
				newValues.put(pixelValue, newValue);
				
				if(isRegistered) {
					
					if(newValue < attribs.min[b]) {
						
						attribs.min[b] = newValue;
					}
					
					if(newValue > attribs.max[b]) {
						
						attribs.max[b] = newValue;
					}
				}				
			}
			
			/* Application */
			for(int y = 0; y < imageHeight; ++y) {
				for(int x = 0; x < imageWidth; ++x) {
					
					double pixelValue = ImTool.getPixelValue(x, y, b, image);
					ImTool.setPixelValue(x, y, b, image, newValues.get(pixelValue));
				}
			}
		}
		
		if(isRegistered) {
			
			attribs.equalized = true;
		}
	}

	/**
	 * 
	 * @param image to consider; should not be null
	 * @return a clone
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static BufferedImage clone(BufferedImage image) {
		
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * 
	 * @param icon considered as the source
	 * @return an image from an icon
	 */
	public static BufferedImage convertToBufferedImage(ImageIcon icon) {
		
		BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.createGraphics();
		icon.paintIcon(null, g, 0,0);
		g.dispose();
		return bufferedImage;
	}
	
	/**
	 * Generates a minimized version of the image while keeping the size ratio.
	 * 
	 * @param image to refer; should not be null
	 * @return a small visualization of the image
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static BufferedImage createIconFrom(BufferedImage image) {

		return ImTool.reSize(image, ImTool.ICON_WIDTH, ImTool.ICON_HEIGHT, 90);
	}

	/**
	 * Place a rectangle on the considered image.
	 * The rectangle is defined by two points, the upper-left and the lower-right ones. 
	 * 
	 * @param image to consider; should not be null
	 * @param minX column coordinate of the upper-left point; should be >= 0 
	 * @param maxX column coordinate of the lower-right point; should be >= 0
	 * @param minY row coordinate of the upper-left point; should be >= 0
	 * @param maxY row coordinate of the lower-right point; should be >= 0
	 * @param color of the border of the rectangle. It defines, the combined RGB value (e.g. {@link ImTool#RGB_GREEN} for a green color) 
	 * 
	 * @throws NullPointerException is image is null
	 * @throws IndexOutOfBoundsException if minX, maxX, minY and maxY do not satisfy the requirement (i.e. >= 0)
	 */
	public static void drawRectangleOn(BufferedImage image, int minX, int maxX, int minY, int maxY, int color) {
		
		/* Horizontal lines */
		for(int x = minX; x <= maxX; ++x){
			
			image.setRGB(x, minY, color);
			image.setRGB(x, maxY, color);
		}
		
		/* Vertical lines */
		for(int y = minY; y <= maxY; ++y){
			
			image.setRGB(minX, y, color);
			image.setRGB(maxX, y, color);
		}
	}

	/**
	 * Computes a new size of the image assuming that the new height must be (percentage %) of the height of the parent container.
	 * 
	 * @param image having width > 0 and height > 0; must not be null
	 * @param containerWidth should be > 0
	 * @param containerHeight should be > 0
	 * @param percentage to apply to the height of the parent container of the image
	 * @return new proportional dimension having a height representing the (percentage %) of the height of the container.
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static Dimension estiMateSize(BufferedImage image, int containerWidth, int containerHeight, int percentage) {
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		int newHeight = applyPercentage(containerHeight, percentage);
		int newWidth = (int) Math.ceil(newHeight * width / height);
		
		if(newWidth >= containerWidth) {
			
			int oldWidth = newWidth;
			newWidth = applyPercentage(containerWidth, percentage);
			newHeight = (int) Math.ceil(newWidth * newHeight / oldWidth);
		}
		
		return new Dimension(newWidth, newHeight);
	}

	/**
	 * Generates an image depicting the frontiers of the regions of an image.
	 * 
	 * @param flatZoneImage a flat zone corresponds here to a regions associated to one specific label.
	 * All pixels contained in the region have then an homogeneous color associated to the label.
	 * @param color of the edge frontier to draw
	 * @return
	 */
	public static BufferedImage flatZoneEdge(BufferedImage flatZoneImage, Color color) {
		
		int width = flatZoneImage.getWidth();
		int height = flatZoneImage.getHeight();
		BufferedImage edge = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	
		if(color == null) {
			
			color = new Color(255, 255, 0);
		}
		int iColor = color.getRGB();
		
		Color edgeColor = new Color(0, 0, 0, 0);
		int eColor=  edgeColor.getRGB();
		
		int xNeighbor;
		int yNeighbor;
		int label;
		int labelNeighbor;
		int labelEdge;
		
		// Horizontal sweep
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				
				xNeighbor = x + 1;
				yNeighbor = y;
				
				if(xNeighbor < width) {
					
					label = flatZoneImage.getRGB(x, y);
					labelNeighbor = flatZoneImage.getRGB(xNeighbor, yNeighbor);
					
					if(label != labelNeighbor) {
						
						edge.setRGB(x, y, iColor);
						
					}else {
						
						edge.setRGB(x, y, eColor);
					}
				}
			}
		}
		
		// Vertical sweep
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				
				xNeighbor = x;
				yNeighbor = y + 1;
				
				if(yNeighbor < height) {
					
					label = flatZoneImage.getRGB(x, y);
					labelNeighbor = flatZoneImage.getRGB(xNeighbor, yNeighbor);
					labelEdge = edge.getRGB(xNeighbor, yNeighbor);
					
					if(labelEdge != iColor) {
						
						if(label != labelNeighbor) {
	
							edge.setRGB(x, y, iColor);
	
						}
					}
				}
			}
		}
		
		return edge;
	}
	
	/**
	 * Generate an image according to the x and y axis matrix of labels where each label is associated to a color stored in a LUT (LookUp Table). 
	 * 
	 * <p>
	 * If a label is not associated with a color in the LUT, a random color is assigned to it.
	 * 
	 * @param labelMatrix3D contains all labels regrouping regions in the image; should not be null
	 * @param cubeFace determines the face of the cube to consider (select two axis eg: XY)
	 * @param lut map of RGB colors; created if null
	 * @return an RGB image depicting regions
	 * 
	 * @throws NullPointerException if labelMatrix is null
	 */
	public static BufferedImage generateFaceofCube(LabelMatrix3D labelMatrix3D, CubeFace cubeFace, HashMap<Integer, Color> lut) {
	
		int w;
		int h;
		
		switch(cubeFace) {
		
		case XY:
			w = labelMatrix3D.getWidth();
			h = labelMatrix3D.getHeight();
			break;	
		case XZ:
			w = labelMatrix3D.getWidth();
			h = labelMatrix3D.getDepth();
			break;	
		case YZ:
			w = labelMatrix3D.getHeight();
			h = labelMatrix3D.getDepth();
			break;			
		default:
			w = labelMatrix3D.getWidth();
			h = labelMatrix3D.getHeight();
		}
		
		if(lut == null) {
			
			lut = new HashMap<Integer, Color>();
		}
	
		BufferedImage imageRegions = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	
		Color color;
		for(int j = 0; j < h; j++) {
			for(int i = 0; i < w; i++) {
	
				int x;
				int y;
				int z;
				
				switch(cubeFace) {
				
				case XY:
					x = i;
					y = j;
					z = 0;
					break;	
				case XZ:
					x = i;
					y = 0;
					z = j;
					break;	
				case YZ:
					x = 0;
					y = i;
					z = j;
					break;			
				default:
					x = i;
					y = j;
					z = 0;
				}
				
				int label = labelMatrix3D.getLabel(x, y, z);
	
				if(!lut.containsKey(label)) {
	
					Random rand = new Random();
	
					float r = rand.nextFloat();
					float g = rand.nextFloat();
					float b = rand.nextFloat();
	
					color = new Color(r, g, b);
					lut.put(label, color);
	
				}else {
					
					color = lut.get(label);
				}
				
				imageRegions.setRGB(i, j, color.getRGB());
			}
		}
	
		return imageRegions;		
	}

	/**
	 * 
	 * @param image source having one or more bands; should not be null
	 * @param b index of a specific band; should be >= 0 and < number of bands of the image
	 * @return a grayscale image of the corresponding band
	 * 
	 * @throws NullPointerException if image is null
	 * @throws IndexOutOfBoundsException if b is not in [0, number of bands[
	 */
	public static BufferedImage generateGrayscaleImageFrom(BufferedImage image, int b) {

		BufferedImage imgBuffer = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for(int j = 0; j < image.getHeight(); ++j) {
			for(int i = 0; i < image.getWidth(); ++i) {

				int value = ImTool.getNormPixelValues(i, j, b, image);

				imgBuffer.getRaster().setSample(i, j, 0, value);
			}
		}

		if(ImTool.is(image).equalized) {
			
			ImTool.equalize(imgBuffer);
		}

		return imgBuffer;
	}

	/**
	 * 
	 * @return a random color integer RGB value
	 */
	public static int generateRandomColorRGBValue() {
		
		Random rand = new Random();
		int r = rand.nextInt();
		int g = rand.nextInt();
		int b = rand.nextInt();
		return ImTool.getRGBValueFrom(r, g, b);
	}

	/**
	 * Generate an image according to matrix of labels where each label is associated to a color stored in a LUT (LookUp Table). 
	 * 
	 * <p>
	 * If a label is not associated with a color in the LUT, a random color is assigned to it.
	 * 
	 * @param labelMatrix contains all labels regrouping regions in the image; should not be null
	 * @param lut map of RGB colors; created if null
	 * @return an RGB image depicting regions
	 * 
	 * @throws NullPointerException if labelMatrix is null
	 */
	public static BufferedImage generateRegions(LabelMatrix labelMatrix, HashMap<Integer, Color> lut) {
	
		int w = labelMatrix.getWidth();
		int h = labelMatrix.getHeight();
		
		if(lut == null) {
			
			lut = new HashMap<Integer, Color>();
		}
	
		BufferedImage imageRegions = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	
		Color color;
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
	
				int label = labelMatrix.getLabel(x, y);
	
				if(!lut.containsKey(label)) {
	
					Random rand = new Random();
	
					float r = rand.nextFloat();
					float g = rand.nextFloat();
					float b = rand.nextFloat();
	
					color = new Color(r, g, b);
					lut.put(label, color);
	
				}else {
					
					color = lut.get(label);
				}
				
				imageRegions.setRGB(x, y, color.getRGB());
			}
		}
	
		return imageRegions;		
	}
	
	/**
	 * 
	 * @param image of interest; should not be null
	 * @return the current active representation of the image for the visualization
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static BufferedImage getActiveViewOf(BufferedImage image) {
	
		return register.get(image).activetView;
	}

	/**
	 * 
	 * @param image to consider
	 * @return all {@link Attribs attributes} of the corresponding image
	 */
	public static Attribs getAttribsOf(BufferedImage image) {
		
		if(ImTool.is(image).registered) {
			
			return register.get(image);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param image of interest; should not be null
	 * @param viewId should be >=0 and <= number of prepared views
	 * @return the original visualization of the image having the initial size
	 * 
	 * <p>
	 * Possible values of viewId:
	 * <li> 0: the image
	 * <li> 1: band 1
	 * <li> 2: band 2
	 * <li> 3: band 4
	 * <li> ...
	 * 
	 * @throws NullPointerException if image is null
	 * @throws IndexOutOfBoundsException if viewId not in [0, number of prepared views[
	 */
	public static BufferedImage getBufferOf(BufferedImage image, int viewId) {

		return register.get(image).buffer[viewId];
	}

	/**
	 * Find the directory containing an image.
	 * 
	 * @param image contained in the directory; must not be null
	 * @return the directory found
	 * 
	 * @throws NullPointerException if image is null or not registered
	 */
	public static String getDirOf(BufferedImage image) {

		return register.get(image).directory;
	}

	public static HashMap<Integer, Color> getGrayScaleLUT() {
		
		if(grayScaleLUT == null) {
			
			grayScaleLUT = new HashMap<Integer, Color>();
			for(int i = 0; i <= 255; i++){
				
				grayScaleLUT.put(i, new Color(i, i, i));
			}
		}

		return grayScaleLUT;
	}

	/**
	 * 
	 * @param image of interest; should not be null
	 * @param viewId should be >=0 and <= number of prepared views
	 * @return the minimized view associated to the image
	 * 
	 * <p>
	 * Possible values of viewId:
	 * <li> 0: the image
	 * <li> 1: band 1
	 * <li> 2: band 2
	 * <li> 3: band 4
	 * <li> ...
	 * 
	 * @throws NullPointerException if image is null
	 * @throws IndexOutOfBoundsException if viewId not in [0, number of prepared views[
	 */
	public static BufferedImage getIconViewOf(BufferedImage image, int viewId) {

		return register.get(image).iconView[viewId];
	}

	/**
	 * 
	 * @param image associated to the matrix of labels to get.
	 * @return the matrix of labels; cannot be null as a new matrix of labels if created and filled automatically from the pixels if missing.
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static LabelMatrix getLabelMatrixOf(BufferedImage image) {

		if(!ImTool.is(image).registered) {
			
			Attribs attribs = new Attribs();
			register.put(image, attribs);
			attribs.registered = true;
		}
		
		LabelMatrix labelMatrix = register.get(image).labelMatrix;
		
		if(labelMatrix == null) {
			
			labelMatrix = new LabelMatrix(image.getWidth(), image.getHeight());
		}
	
		return labelMatrix;
	}
	
	/**
	 * 
	 * @param cube associated to the matrix 3D of labels to get.
	 * @return the matrix 3D of labels; cannot be null as a new matrix of labels if created and filled automatically from the voxels if missing.
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static LabelMatrix3D getLabelMatrix3DOf(RGBStruct cube) {

		if(!ImTool.is(cube).registered) {
			
			Attribs3D attribs3D = new Attribs3D();
			register3D.put(cube, attribs3D);
			attribs3D.registered = true;
		}
		
		LabelMatrix3D labelMatrix3D = register3D.get(cube).labelMatrix3D;
		
		if(labelMatrix3D == null) {
			
			labelMatrix3D = new LabelMatrix3D(cube.getxLevels(), cube.getyLevels(), cube.getzLevels());
		}
	
		return labelMatrix3D;
	}
	
	/**
	 * Find the name of an image in the register.
	 * 
	 * @param image owning the name; must not be null
	 * @return the name found
	 * 
	 * @throws NullPointerException if image is null or not registered
	 */
	public static String getNameOf(BufferedImage image) {

		return register.get(image).name;
	}

	/**
	 * The number of bands vary according to the type of the image.
	 * 
	 * <p>
	 * Examples:
	 * <pre>
	 * <li> Grayscale image: 1 band
	 * <li> Color image: 2 bands [R,G,B]
	 * <li> Depth image: 1 band
	 * <li> Multispectral image: multi-bands (i.e. > 3)
	 * </pre>
	 *
	 * @param image having a specific number of bands; should not be null
	 * @return the number of bands
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static int getNbBandsOf(BufferedImage image) {

		return image.getRaster().getNumBands();
	}

	/**
	 * 
	 * @param image of interest; should not be null
	 * @return the total number of pixels in the image
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static int getNbPixelsOf(BufferedImage image) {
		
		return image.getWidth() * image.getHeight();
	}

	/**
	 * The value of the pixel is converted in order to have a value in the display dynamic [0, 255]. 
	 * 
	 * @param x index of the column; should be >= 0 and < width of the image
	 * @param y index of the row; should be >= 0 and < height of the image
	 * @param b id of band; should be >= 0 and < number of bands
	 * @param image represented as one or more matrix; should not be null
	 * @param image
	 * @return Normalized value of the pixel for the determined band b.
	 * 
	 * @throws ArrayIndexOutOfBoundsException if the pixel (x,y) is not contained in the image and if b exceeds the number of bands
	 * @throws NullPointerException if image is null
	 */
	public static int getNormPixelValues(int x, int y, int b, BufferedImage image) {

		double pixelValue = ImTool.getPixelValue(x, y, b, image);

		Attribs attribs = register.get(image);
		double minValue = attribs.min[b];
		double maxValue = attribs.max[b];

		return (int) Math.round((255 * (pixelValue - minValue) / (maxValue - minValue)));
	}

	/**
	 * Find the path leading to the image.
	 * 
	 * @param image contained in the directory; must not be null
	 * @return the path found
	 * 
	 * @throws NullPointerException if image is null or not registered
	 */
	public static String getPathOf(BufferedImage image) {

		return register.get(image).path;
	}

	/**
	 * Find the value of a pixel at the position x,y on the band having the number b.
	 * 
	 * @param x index of the column; should be >= 0 and < width of the image
	 * @param y index of the row; should be >= 0 and < height of the image
	 * @param b number of band; should be >= 0 and < number of bands
	 * @param image represented as one or more matrix; should not be null
	 * @return a grayscale value of a pixel according to a specific band
	 * 
	 * @throws ArrayIndexOutOfBoundsException if the pixel (x,y) is not contained in the image and if b exceeds the number of bands
	 * @throws NullPointerException if image is null
	 * 
	 * @see ImTool#getPixelValues(int, int, BufferedImage) get a vector of values of a pixel
	 */
	public static double getPixelValue(int x, int y, int b, BufferedImage image) {
		
		return image.getRaster().getSampleDouble(x, y, b);
	}

	/**
	 * Find all the values associated to a pixel at the position x,y on all the bands.
	 * 
	 * @param x index of the column; should be >= 0 and < width of the image
	 * @param y index of the row; should be >= 0 and < height of the image
	 * @param image represented as one or more matrix; should not be null
	 * @return a vector of values of the pixel (i.e. for all bands) 
	 * 
	 * @throws ArrayIndexOutOfBoundsException if the pixel (x,y) is not contained in the image
	 * @throws NullPointerException if image is null
	 * 
	 * @see ImTool#getPixelValue(int, int, int, BufferedImage) get a value of a pixel on one band
	 */
	public static double[] getPixelValues(int x, int y, BufferedImage image) {

		int nbBands = image.getRaster().getNumBands();
		double[] pixelValues = new double[nbBands];
		for(int b=0; b < nbBands; b++) {
			
			pixelValues[b] = ImTool.getPixelValue(x, y, b, image);
		}

		return pixelValues;
	}
	
	/** 
	 * 
	 * @param r red value
	 * @param g green value
	 * @param b blue value
	 * @return An unique value of RGB from the individual bands r, g, b.
	 */
	public static int getRGBValueFrom(int r, int g, int b) {

		return (r & 0xFF) << 16 |
			   (g & 0xFF) << 8  |
			   (b & 0xFF) << 0;
	}

	/**
	 * 
	 * @param image of interest; should not be null
	 * @param viewId should be >=0 and <= number of prepared views
	 * @return the current view of the image according to a specific viewId
	 * 
	 * <p>
	 * Possible values of viewId:
	 * <li> 0: the image
	 * <li> 1: band 1
	 * <li> 2: band 2
	 * <li> 3: band 4
	 * <li> ...
	 * 
	 * @throws NullPointerException if image is null
	 * @throws IndexOutOfBoundsException if viewId not in [0, number of prepared views[
	 */
	public static BufferedImage getViewOf(BufferedImage image, int viewId) {
	
		return register.get(image).view[viewId];
	}

	/**
	 * Stores each minimum and maximum grayscale values of each band of an image.
	 * 
	 * <p>
	 * Vectors containing the min an max values of each band are storing in the {@link utils.d2.Attribs attribs} of the image. 
	 * 
	 * @param image having one or more bands; must not be null
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static void initMinMaxValues(BufferedImage image) {
	
		Attribs attribs = register.get(image);
		int numBands = ImTool.getNbBandsOf(image);
		
		attribs.min = new double[numBands];
		attribs.max = new double[numBands];
		
		Arrays.fill(attribs.min, Double.MAX_VALUE);
		Arrays.fill(attribs.max,  Double.MIN_VALUE);
		
		for(int b = 0; b < numBands; ++b ) {
			for(int j = 0; j < image.getHeight(); ++j) {
				for(int i = 0; i < image.getWidth(); ++i) {
	
					double pv = ImTool.getPixelValue(i, j, b, image);
					
					if(pv < attribs.min[b]) {
						
						attribs.min[b] = pv;
					}
					
					if(pv > attribs.max[b]) {
						
						attribs.max[b] = pv;
					}
				}
			}
		}	
		
		attribs.minMaxPrepared = true;
	}

	/**
	 * Essentially used for testing boolean image advanced attributes.
	 * 
	 * <p>
	 * Example:
	 * <pre>
	 * if(ImTool.is(image).equalized){
	 * ...
	 * }
	 * </pre>
	 * 
	 * @param image to find; should not be null
	 * @return all advanced attributes of the image; {@link ImTool#DEFAULT_ATTRIBS} is returned if the image is not registered
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static Attribs is(BufferedImage image) {
	
		Attribs attribs = register.get(image);
		
		if(attribs == null) {
			
			attribs = ImTool.DEFAULT_ATTRIBS;
		}
		
		return attribs;
	}
	
	public static Attribs3D is(RGBStruct cube) {
		
		Attribs3D attribs3D = register3D.get(cube);
		
		if(attribs3D == null) {
			
			attribs3D = ImTool.DEFAULT_ATTRIBS_3D;
		}
		
		return attribs3D;
	}

	/**
	 * Different views are useful for the representation of one image.
	 * 
	 * <p>
	 * N.B: the min and max values of the pixels for each band should be first {@link ImTool#initMinMaxValues(BufferedImage) prepared}.
	 * 
	 * <p> 
	 * Especially for the image having a number of band > 3, RGB views have to be generated for:
	 * <li> 0: the image
	 * <li> 1: band 1
	 * <li> 2: band 2
	 * <li> 3: band 3
	 * <li> ...
	 * 
	 * <p>
	 * Different kind of views:
	 * <li> activeView: the current view of the image that is actually shown
	 * <li> buffer[]: contains the original views keeping the same size as the real image
	 * <li> view[]: contains the current views of the image that can have different size than the real image
	 * <li> iconView[]: contains minimized representations of the image
	 * 
	 * @param image of interest; should not be null
	 * @param rgbOrder defines which band has to be Red, Green or Blue
	 * 
	 * @throws NullPointerException if image is null or if the min and max values are not yet prepared
	 */
	public static void prepareViewsFrom(BufferedImage image, int[] rgbOrder) {

		BufferedImage imageBuffer = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		for(int j = 0; j < image.getHeight(); ++j) {
			for(int i = 0; i < image.getWidth(); ++i) {

				int value = ImTool.getRGBValueFrom(ImTool.getNormPixelValues(i, j, rgbOrder[0], image),
  												   ImTool.getNormPixelValues(i, j, rgbOrder[1], image),
  												   ImTool.getNormPixelValues(i, j, rgbOrder[2], image));
				imageBuffer.setRGB(i, j, value);
			}
		}

		Attribs attribs = register.get(image);
		attribs.activetView = imageBuffer;

		int numberOfViews = ImTool.getNbBandsOf(image) + 1;
		attribs.buffer = new BufferedImage[numberOfViews];
		attribs.buffer[0] = attribs.activetView;

		attribs.view = new BufferedImage[numberOfViews];
		attribs.view[0] = attribs.activetView;

		attribs.iconView = new BufferedImage[numberOfViews];
		attribs.iconView[0] = ImTool.createIconFrom(attribs.buffer[0]);

		for(int bandNumber = 0; bandNumber < ImTool.getNbBandsOf(image); bandNumber++) {

			int viewId = bandNumber + 1;
			attribs.view[viewId] = ImTool.generateGrayscaleImageFrom(image, bandNumber);
			attribs.buffer[viewId] = attribs.view[viewId];
			attribs.iconView[viewId] = ImTool.createIconFrom(attribs.buffer[viewId]);
		}
	}

	/**
	 * Reads an image from its path.
	 * 
	 * <p>
	 * Advanced attributes of the image are stored in the register.
	 * 
	 * @param path leading to the image; must not be null
	 * @return the loaded image; is null if an exception is thrown
	 * 
	 * @throws NullPointerException if path is null
	 * @throws IIOException if file is not found or if it cannot be read
	 */
	public static BufferedImage read(String path) {

		File file = new File(path);
		BufferedImage image = null;
		try {
			
			image = ImageIO.read(file);

			if(image == null)
			{
				image = readBigImage(path); // Not working for now!
				//image = read(path, 0, 0, 100, 100);
			}
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		
		if(image != null) {
			
			String directory = file.getParentFile().getPath();
			String name = file.getName();
			register(image, directory, name, path);
		}
		
		return image;
	}
	
	/**
	 * Reads a region of an image from its path.
	 * 
	 * <p>
	 * Advanced attributes of the image are stored in the register.
	 * 
	 * @param path leading to the image; must not be null
	 * @param x as the position of the top left point
	 * @param y as the position of the top left point
	 * @param w as the width of the window
	 * @param h as the height of the window 
	 * 
	 * @return the loaded image; is null if an exception is thrown
	 * 
	 * @throws NullPointerException if path is null
	 * @throws IIOException if file is not found or if it cannot be read	 
	 */
	public static BufferedImage read(String path, int x, int y, int w, int h){
		
		File file = new File(path);
		BufferedImage image = null;
		try {
			
			Rectangle sourceRegion = new Rectangle(x, y, w, h);

			ImageInputStream stream = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

			if(readers.hasNext()) {

				ImageReader reader = readers.next();
				reader.setInput(stream);
				
				ImageReadParam param = reader.getDefaultReadParam();
				param.setSourceRegion(sourceRegion);

				image = reader.read(0, param);
			}
			
			stream.close();
			
		}catch(IOException e) {
			
			e.printStackTrace();
		}
		
		return image;
	}
	
	/**
	 * TODO - Reads a big image. Not currently working.
	 * 
	 * <p>
	 * Advanced attributes of the image are stored in the register.
	 * 
	 * @param path leading to the image; must not be null
	 * 
	 * @return the loaded image; is null if an exception is thrown
	 * 
	 * @throws NullPointerException if path is null
	 * @throws IIOException if file is not found or if it cannot be read	 
	 */
	public static BufferedImage readBigImage(String path){
		
		File file = new File(path);
		BufferedImage image = null;
		try {
			
			ImageInputStream stream = ImageIO.createImageInputStream(file);
			image = streamToBufferedImage(stream);			
			stream.close();
			
		}catch(IOException e) {
			
			e.printStackTrace();
		}
		
		return image;
	}

	/**
	 * 
	 * @param stream opened and filled by the content of an image.
	 * @return a #{@link BufferedImage} from a #{@link ImageInputStream} by using a buffer system.
	 */
	public static BufferedImage streamToBufferedImage(ImageInputStream stream) {
		
		BufferedImage image = null;
		
		try {
			
			// Get byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        byte[] buf = new byte[1024];
	        
	        try {
	        	
	        	int i = 1;
	            for (int readNum; (readNum = stream.read(buf)) != -1;) {
	            	
	                bos.write(buf, 0, readNum); 
	                System.out.println("read " + readNum + " bytes, i: "+ i++);
	            }
	            System.out.println("stream length: "+ stream.length());
	            
	        } catch (IOException ex) {
	        	
	            ex.printStackTrace();
	        }
	 
	        byte[] bytes = bos.toByteArray();
	        bos.close();
			
			// Convert the byte values into a BufferedImage
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			image = ImageIO.read(bis);
			bis.close();
			System.out.println(image);
			
			return image;
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return image;
	}

	/**
	 * Registers an image object that already has been opened (not by the ImTool)
	 * @param image register; should not be null
	 * @param dir containing the image
	 * @param name of the image
	 * @param path leading to the image
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static void register(BufferedImage image, String dir, String name, String path) {

		Attribs attribs = new Attribs();
		attribs.directory = dir;
		attribs.name = name;
		attribs.path = path;
		register.put(image, attribs);
		attribs.registered = true;
	}
	
	public static void register3D(RGBStruct cube, String dir, String name, String path) {

		Attribs3D attribs3D = new Attribs3D();
		attribs3D.directory = dir;
		attribs3D.name = name;
		attribs3D.path = path;
		register3D.put(cube, attribs3D);
		attribs3D.registered = true;
	}
	
	/**
	 * Redraw an image by scaling it according to the width and height.
	 * 
	 * @param image to resize; must not be null
	 * @param width new value; should be > 0
	 * @param height new value; should be > 0
	 * @return a resized image
	 * 
	 * @throws NullPointerException if image is null
	 * 
	 * @see ImTool#reSize(BufferedImage, int, int, int) resize an image according to its parent container and a percentage value
	 */
	public static BufferedImage reSize(BufferedImage image, int width, int height) {
		
		int type = image.getType();
		if(type == 0){
			
			type = BufferedImage.TYPE_BYTE_BINARY;
		}
		
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		
		return resizedImage;
	}

	/**
	 * Redraw an image by scaling it such that the new height is the (percentage %) of the height of the parent container.
	 * 
	 * <p>
	 * The proportion of the new size is ensured by {@link ImTool#estiMateSize(BufferedImage, int, int, int) a size estimation}.
	 * 
	 * @param image to resize; must not be null
	 * @param containerWidth; should be > 0
	 * @param containerHeight; should be > 0
	 * @param percentage to apply to the height of the parent container of the image
	 * @return a resized image having height corresponding to the (percent %) of the height of the container
	 * 
	 * @throws NullPointerException if image is null
	 * 
	 * @see ImTool#reSize(BufferedImage, int, int) resize an image by precising directly a new width and a new height
	 */
	public static BufferedImage reSize(BufferedImage image, int containerWidth, int containerHeight, int percentage) {
		
		Dimension size = estiMateSize(image, containerWidth, containerHeight, percentage);
		int newWidth = size.width;
		int newHeight = size.height;
		
		return reSize(image, newWidth, newHeight);
	}

	/**
	 * Converts an RGB color space of an image to an LAB color space.
	 * @param image input to convert
	 * @return
	 */
	public static BufferedImage rgb2lab(BufferedImage image) {

		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage labImage = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_565_RGB);
		ImTool.initMinMaxValues(image);
		
		for(int y = 0; y < h; ++y) {
			
			for(int x = 0; x < w; ++x) {
		
				int r = ImTool.getNormPixelValues(x, y, 0, image);
				int g = ImTool.getNormPixelValues(x, y, 1, image);
				int b = ImTool.getNormPixelValues(x, y, 2, image);
				double lab[] = ImTool.rgb2lab(r, g, b);
				labImage.getRaster().setPixel(x, y, lab); // TODO warning, to be tested
			}
		}
				
		return labImage;
	}

	/**
	 * Converts a trio of RGB values to LAB. 
	 * @param r red value
	 * @param g green value
	 * @param b b value
	 * @return an array of LAB values 
	 */
	public static double[] rgb2lab(int r, int g, int b){
		
		double[] lab = new double[3];
		
	    double varR = r/255.0;
	    double varG = g/255.0;
	    double varB = b/255.0;
	
	
	    if (varR > 0.04045 ) varR = Math.pow((( varR + 0.055 ) / 1.055 ), 2.4 );
	    else varR = varR / 12.92;
	    if (varG > 0.04045 ) varG = Math.pow((( varG + 0.055 ) / 1.055 ), 2.4);
	    else varG = varG / 12.92;
	    if (varB > 0.04045 ) varB = Math.pow((( varB + 0.055 ) / 1.055 ), 2.4);
	    else varB = varB / 12.92;
	
	    varR = varR * 100.;
	    varG = varG * 100.;
	    varB = varB * 100.;
	
	    //Observer. = 2°, Illuminant = D65
	    double X = varR * 0.4124 + varG * 0.3576 + varB * 0.1805;
	    double Y = varR * 0.2126 + varG * 0.7152 + varB * 0.0722;
	    double Z = varR * 0.0193 + varG * 0.1192 + varB * 0.9505;
	
	
	    double varX = X / 95.047 ;         //ref_X =  95.047   Observer= 2°, Illuminant= D65
	    double varY = Y / 100.000;          //ref_Y = 100.000
	    double varZ = Z / 108.883;          //ref_Z = 108.883
	
	    if (varX > 0.008856 ) varX = Math.pow(varX , ( 1./3.));
	    else varX = (7.787 * varX) + ( 16. / 116. );
	    if (varY > 0.008856) varY = Math.pow(varY , ( 1./3.));
	    else varY = ( 7.787 * varY ) + ( 16. / 116. );
	    if ( varZ > 0.008856 ) varZ = Math.pow(varZ , ( 1./3. ));
	    else varZ = ( 7.787 * varZ ) + ( 16. / 116. );
	
	    lab[0] = ( 116. * varY ) - 16.;
	    lab[1] = 500. * ( varX - varY );
	    lab[2] = 200. * ( varY - varZ );
	
	    return lab;
	}

	/**
	 * Stores a png image of the specified image on the disk.
	 * 
	 * @param image source; should not be null
	 * @param path
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static void save(BufferedImage image, String path) {
	
		try {
			
			ImageIO.write(image, "PNG", new File(path));
			System.out.println("Image saved: "+ path);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Defines the actual view to show.
	 * 
	 * @param image of interest; should not be null
	 * @param imageView actual representation of the image to consider; should not be null in order to avoid a NullPointerException later
	 * 
	 * @throws NullPointerException if image is null
	 */
	public static void setActiveViewOf(BufferedImage image, BufferedImage imageView) {

		register.get(image).activetView = imageView;
	}

	/**
	 * 
	 * @param x index of the column; should be >=0 and < width of the image
	 * @param y index of the row; should be >=0 and < height of the image
	 * @param b index of the band to consider; should be >=0 and < number of bands of the image
	 * @param image of interest; should not be null
	 * @param value to associate with the pixel according to the precised band
	 * 
	 * @throws NullPointerException if image is null
	 * @throws IndexOutOfBoundsException if x or y or b does not follows the rules above 
	 */
	public static void setPixelValue(int x, int y, int b, BufferedImage image, double value) {
		
		double[] bvalues = new double[ImTool.getNbBandsOf(image)];
		bvalues[b] = value;
		
		for(int i = 0; i < bvalues.length; ++i) {

			if(i != b) {
				
				bvalues[i] = ImTool.getPixelValue(x, y, i, image);
			}
		}
		
		image.getRaster().setPixel(x, y, bvalues);
	}

	/**
	 * Saves the view of the image according to the view Id that can be:
	 * 
	 * <li> 0: the image
	 * <li> 1: band 1
	 * <li> 2: band 2
	 * <li> 3: band 3
	 * <li> ...
	 * 
	 * @param image of interest; should not be null
	 * @param viewId should be >=0 and <= number of prepared views
	 * @param newImageView representation of the image corresponding to the specified viewId; should not be null in order to avoid a NullPointerException later
	 *
 	 * @throws NullPointerException if image is null
 	 * @throws IndexOutOfBoundsException if viewId is not included in [0, number of prepared views]
	 */
	public static void setViewOf(BufferedImage image, int viewId, BufferedImage newImageView) {

		register.get(image).view[viewId] = newImageView;
	}

	/**
	 * Displays an image visualization on the screen.
	 * 
	 * @param image to show; must not be null and should not have more than 3 bands
	 * @param percent defines how the image covers the screen
	 * 
	 * Example of values are:
	 * <li> {@link ImFrame#IMAGE_REAL_SIZE }
	 * <li> {@link ImFrame#IMAGE_DEFAULT_SIZE }
	 * 
	 * @throws NullPointerException if image is null
	 * @throws IllegalArgumentException if image has more than 3 bands
	 * 
	 * @see LoadAndShow_Xbands displaying an image having more than 3 bands
	 */
	public static void show(BufferedImage image, int percent) {
		
		new ImFrame(image, percent);
	}
	
	/**
	 * Displays an image on the screen while specifying the title of the frame.
	 * 
	 * @param image to show; must not be null
	 * @param percent defines how the image covers the screen
	 * 
	 * Example of values are:
	 * <li> {@link ImFrame#IMAGE_REAL_SIZE }
	 * <li> {@link ImFrame#IMAGE_DEFAULT_SIZE }
	 *  
	 * @param title of the frame; must not be null
	 * 
	 * @throws NullPointerException if image or title is null
	 */
	public static void show(BufferedImage image, int percent, String title) {
		
		new ImFrame(image, percent, title);
	}
	
	public static void show(ArrayList<BufferedImage> images, int percent, String title, int sliderMin, int sliderMax, int sliderInitValue) {
		
		new ImFrame(images, percent, title, sliderMin, sliderMax, sliderInitValue);
	}
	
	public static int[][] getConnectedNeibhorgs(int connectivity, int x, int y) {
		
		int coords[][];
				
		switch(connectivity) {
		
		case 8:
			/* 4 connectivities */
			coords = new int[8][2];
			
			/* 1st pixel */
			coords[0][0] = x - 1;
			coords[0][1] = y;
			
			/* 2nd pixel */
			coords[1][0] = x - 1;
			coords[1][1] = y + 1;
			
			/* 3rd pixel */
			coords[2][0] = x;
			coords[2][1] = y + 1;
			
			/* 4th pixel */
			coords[3][0] = x + 1;
			coords[3][1] = y + 1;

			/* 5th pixel */
			coords[4][0] = x + 1;
			coords[4][1] = y;
			
			/* 6th pixel */
			coords[5][0] = x + 1;
			coords[5][1] = y - 1;
			
			/* 7th pixel */
			coords[6][0] = x;
			coords[6][1] = y - 1;

			/* 8th pixel */
			coords[7][0] = x;
			coords[7][1] = y - 1;

			break;
		default:
			/* 4 connectivities */
			coords = new int[4][2];
			
			/* 1st pixel */
			coords[0][0] = x - 1;
			coords[0][1] = y;
			
			/* 2nd pixel */
			coords[1][0] = x;
			coords[1][1] = y + 1;
			
			/* 3rd pixel */
			coords[2][0] = x + 1;
			coords[2][1] = y;
			
			/* 4th pixel */
			coords[3][0] = x;
			coords[3][1] = y - 1;
		}
		
		return coords;
	}

	public static int[][] getConnectedNeibhorgs(int connectivity, Voxel voxel) {
		
		int coords[][];
		
		int xVoxel = voxel.x;
		int yVoxel = voxel.y;
		int zVoxel = voxel.z;
		
		switch(connectivity) {
		
		case 6:
			/* 6 connectivities */
			coords = new int[6][3];
			
			/* 1st voxel */
			coords[0][0] = xVoxel - 1;
			coords[0][1] = yVoxel;
			coords[0][2] = zVoxel;
			
			/* 2nd voxel */
			coords[1][0] = xVoxel;
			coords[1][1] = yVoxel + 1;
			coords[1][2] = zVoxel;
			
			/* 3rd voxel */
			coords[2][0] = xVoxel + 1;
			coords[2][1] = yVoxel;
			coords[2][2] = zVoxel;
			
			/* 4th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel - 1;
			coords[3][2] = zVoxel;
			
			/* 5th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel;
			coords[3][2] = zVoxel - 1;
			
			/* 6th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel;
			coords[3][2] = zVoxel + 1;
			
			break;
			
		case 14:
			/* 14 connectivities */
			coords = new int[14][3];
			
			/* 1st voxel */
			coords[0][0] = xVoxel - 1;
			coords[0][1] = yVoxel;
			coords[0][2] = zVoxel;
			
			/* 2nd voxel */
			coords[1][0] = xVoxel;
			coords[1][1] = yVoxel + 1;
			coords[1][2] = zVoxel;
			
			/* 3rd voxel */
			coords[2][0] = xVoxel + 1;
			coords[2][1] = yVoxel;
			coords[2][2] = zVoxel;
			
			/* 4th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel - 1;
			coords[3][2] = zVoxel;
			
			/* 5th voxel */
			coords[4][0] = xVoxel;
			coords[4][1] = yVoxel;
			coords[4][2] = zVoxel - 1;
			
			/* 6th voxel */
			coords[5][0] = xVoxel;
			coords[5][1] = yVoxel;
			coords[5][2] = zVoxel + 1;
			
			/* 7th voxel */
			coords[6][0] = xVoxel - 1;
			coords[6][1] = yVoxel - 1;
			coords[6][2] = zVoxel;
			
			/* 8th voxel */
			coords[7][0] = xVoxel - 1;
			coords[7][1] = yVoxel + 1;
			coords[7][2] = zVoxel;
			
			/* 9th voxel */
			coords[8][0] = xVoxel + 1;
			coords[8][1] = yVoxel + 1;
			coords[8][2] = zVoxel;
			
			/* 10th voxel */
			coords[9][0] = xVoxel + 1;
			coords[9][1] = yVoxel - 1;
			coords[9][2] = zVoxel;
			
			/* 11th voxel */
			coords[10][0] = xVoxel;
			coords[10][1] = yVoxel - 1;
			coords[10][2] = zVoxel - 1;

			/* 12th voxel */
			coords[11][0] = xVoxel;
			coords[11][1] = yVoxel + 1;
			coords[11][2] = zVoxel - 1;

			/* 13th voxel */
			coords[12][0] = xVoxel;
			coords[12][1] = yVoxel + 1;
			coords[12][2] = zVoxel + 1;

			/* 14th voxel */
			coords[13][0] = xVoxel;
			coords[13][1] = yVoxel - 1;
			coords[13][2] = zVoxel + 1;
			
			break;
			
		case 22:
			/* 22 connectivities */
			coords = new int[22][3];
			
			/* 1st voxel */
			coords[0][0] = xVoxel - 1;
			coords[0][1] = yVoxel;
			coords[0][2] = zVoxel;
			
			/* 2nd voxel */
			coords[1][0] = xVoxel;
			coords[1][1] = yVoxel + 1;
			coords[1][2] = zVoxel;
			
			/* 3rd voxel */
			coords[2][0] = xVoxel + 1;
			coords[2][1] = yVoxel;
			coords[2][2] = zVoxel;
			
			/* 4th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel - 1;
			coords[3][2] = zVoxel;
			
			/* 5th voxel */
			coords[4][0] = xVoxel;
			coords[4][1] = yVoxel;
			coords[4][2] = zVoxel - 1;
			
			/* 6th voxel */
			coords[5][0] = xVoxel;
			coords[5][1] = yVoxel;
			coords[5][2] = zVoxel + 1;
			
			/* 7th voxel */
			coords[6][0] = xVoxel - 1;
			coords[6][1] = yVoxel - 1;
			coords[6][2] = zVoxel;
			
			/* 8th voxel */
			coords[7][0] = xVoxel - 1;
			coords[7][1] = yVoxel + 1;
			coords[7][2] = zVoxel;
			
			/* 9th voxel */
			coords[8][0] = xVoxel + 1;
			coords[8][1] = yVoxel + 1;
			coords[8][2] = zVoxel;
			
			/* 10th voxel */
			coords[9][0] = xVoxel + 1;
			coords[9][1] = yVoxel - 1;
			coords[9][2] = zVoxel;
			
			/* 11th voxel */
			coords[10][0] = xVoxel;
			coords[10][1] = yVoxel - 1;
			coords[10][2] = zVoxel - 1;

			/* 12th voxel */
			coords[11][0] = xVoxel;
			coords[11][1] = yVoxel + 1;
			coords[11][2] = zVoxel - 1;

			/* 13th voxel */
			coords[12][0] = xVoxel;
			coords[12][1] = yVoxel + 1;
			coords[12][2] = zVoxel + 1;

			/* 14th voxel */
			coords[13][0] = xVoxel;
			coords[13][1] = yVoxel - 1;
			coords[13][2] = zVoxel + 1;
			
			/* 15th voxel */
			coords[14][0] = xVoxel - 1;
			coords[14][1] = yVoxel - 1;
			coords[14][2] = zVoxel + 1;
			
			/* 16th voxel */
			coords[15][0] = xVoxel - 1;
			coords[15][1] = yVoxel + 1;
			coords[15][2] = zVoxel + 1;

			/* 17th voxel */
			coords[16][0] = xVoxel + 1;
			coords[16][1] = yVoxel + 1;
			coords[16][2] = zVoxel + 1;

			/* 18th voxel */
			coords[17][0] = xVoxel + 1;
			coords[17][1] = yVoxel - 1;
			coords[17][2] = zVoxel + 1;

			/* 19th voxel */
			coords[18][0] = xVoxel - 1;
			coords[18][1] = yVoxel - 1;
			coords[18][2] = zVoxel - 1;

			/* 20th voxel */
			coords[19][0] = xVoxel - 1;
			coords[19][1] = yVoxel + 1;
			coords[19][2] = zVoxel - 1;
			
			/* 21th voxel */
			coords[20][0] = xVoxel + 1;
			coords[20][1] = yVoxel + 1;
			coords[20][2] = zVoxel - 1;
			
			/* 21th voxel */
			coords[20][0] = xVoxel + 1;
			coords[20][1] = yVoxel - 1;
			coords[20][2] = zVoxel - 1;

			break;
			
		default:
			/* 6 connectivities */
			coords = new int[6][3];
			
			/* 1st voxel */
			coords[0][0] = xVoxel - 1;
			coords[0][1] = yVoxel;
			coords[0][2] = zVoxel;
			
			/* 2nd voxel */
			coords[1][0] = xVoxel;
			coords[1][1] = yVoxel + 1;
			coords[1][2] = zVoxel;
			
			/* 3rd voxel */
			coords[2][0] = xVoxel + 1;
			coords[2][1] = yVoxel;
			coords[2][2] = zVoxel;
			
			/* 4th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel - 1;
			coords[3][2] = zVoxel;
			
			/* 5th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel;
			coords[3][2] = zVoxel - 1;
			
			/* 6th voxel */
			coords[3][0] = xVoxel;
			coords[3][1] = yVoxel;
			coords[3][2] = zVoxel + 1;
		}
		
		return coords;
	}
	
	public static boolean isInStudiedAread(int x, int y, BufferedImage img) {
		
		return (x >= 0 && y >=0 && x < img.getWidth() && y < img.getHeight());
	}
}
