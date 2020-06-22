package utils;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class LabelMatrix implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The number of the initial regions should be remembered as the matrix could evolve.
	 */
	private int nbInitialRegions;
	
	/**
	 * The number of labels currently contained in the matrix.
	 */
	private int nbRegions;

	/**
	 * The core of the matrix.
	 */
	private int[][] labels;
	
	/**
	 * Creates an empty label of matrix.
	 * 
	 * @see LabelMatrix#LabelMatrix(int, int) create a matrix of labels from the pixels.
	 */
	public LabelMatrix() { }
	
	/**
	 * Creates a matrix of labels where the number of labels equals the number of pixels.
	 * 
	 * <pre>
	 * Number of labels = width * height
	 * </pre>
	 * 
	 * @param width should be > 0
	 * @param height should be > 0
	 * 
	 * @see LabelMatrix#LabelMatrix() instanciate an empty label of matrix
	 */
	public LabelMatrix(int width, int height) {
		
		this.labels = new int[width][height];
		
		int label = 0;
		for(int y=0; y < this.getHeight(); y++) {
			for(int x=0; x < this.getWidth(); x++) {
				
				labels[x][y] = label++;			
			}
		}
		
		this.nbRegions = label;
		this.nbInitialRegions = this.nbRegions;
	}

	/**
	 * Assign one label to all cells of the matrix
	 * @param l initial label
	 */
	public void fill(int l) {
		
		for(int i = 0; i < this.labels.length; ++i){
			
			Arrays.fill(this.labels[i], -1);
		}
	}

	
	/**
	 * Assigns a label to all defined pixels.
	 * 
	 * @param pixels regrouped in a region; should not be null
	 * @param label associated to a region
	 * 
	 * @throws NullPointerException if pixels is null
	 */
	public void fill(ArrayList<Point> pixels, int label) {
	
		for(Point p : pixels) {
			
			this.setLabel(label, p.x, p.y);
		}
	}

	/**
	 * 
	 * @return the number of columns of the matrix.
	 */
	public int getHeight() { 
	
		return this.labels[0].length; 
	}
	
	/**
	 * 
	 * @param x index of the column
	 * @param y index of the row
	 * @return the label at the specified coordinates
	 */
	public int getLabel(int x, int y) {
		
		return this.labels[x][y]; 
	}

	/**
	 * 
	 * @return the core of the matrix.
	 */
	public int[][] getLabels() { 
		
		return labels; 
	}

	/**
	 * 
	 * @return The number of labels currently contained in the matrix.
	 */
	public int getNbRegions() { 
		
		return nbRegions; 
	}

	/**
	 * 
	 * @return the number of rows of the matrix
	 */
	public int getWidth() { 
		
		return this.labels.length; 
	}

	/**
	 * 
	 * @return remind the number of the initial regions.
	 */
	public int getNbInitialRegions() { 
		
		return nbInitialRegions; 
	}
	
	/**
	 * Shows the content of the matrix line by line.
	 */
	public void print() {
		
		System.out.println("Matrix of labels");
		
		for(int j = 0; j < labels[0].length; j++) {
			for(int i = 0; i < labels.length; i++){
				
				System.out.print(labels[i][j] + " | ");
			}
			System.out.println("");
		}	
	}

	/**
	 * Assign a label to a pixel located at x,y.
	 * 
	 * @param label associated to a region
	 * @param x index of the column; should be >= 0
	 * @param y index of the row; should be >= 0
	 */
	public void setLabel(int label, int x, int y) { 
		
		this.labels[x][y] = label; 
	}

	/**
	 * Assign a predefined matrix to the core.
	 * @param labels contains all regions configuration; should not be null
	 * 
	 * @throws NullPointerException if the parameter labels is null
	 */
	public void setLabels(int[][] labels) {
		
		this.labels = labels;
		
		Stack<Integer> regions = new Stack<Integer>();
		
		for(int j = 0; j < labels[0].length; j++) {
			for(int i = 0; i < labels.length; i++){
				
				if(!regions.contains(labels[i][j])) {
					
					regions.push(labels[i][j]);
				}
			}
		}
		
		this.nbRegions = regions.size();
		this.nbInitialRegions = this.nbRegions;
	}
	
	/**
	 * 
	 * @param numberOfRegions; should not be < 0
	 */
	public void setNbRegions(int numberOfRegions) {
		
		this.nbRegions = numberOfRegions;
	}
}
