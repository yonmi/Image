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

package utils.d2;

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
				
				this.labels[x][y] = label++;			
			}
		}
		
		this.nbRegions = label;
		this.nbInitialRegions = this.nbRegions;
	}
	
	/**
	 * Create a label matrix for a simple set.
	 * @param nbValues number of values stored in the set.
	 */
	public LabelMatrix(int nbValues) {
		
		this.labels = new int[nbValues][1];
		int label = 0;
		for(int x = 0; x < nbValues; x++) {

			this.labels[x][0] = label++;
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
