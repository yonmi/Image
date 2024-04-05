/****************************************************************************
* Copyright AGAT-Team (2023)						       
* 									    
* Contributors:								
* B. Naegel								    
* K. Kurtz								    
* N. Passat								    
* J. Randrianasoa							    
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

package utils.d3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class LabelMatrix3D implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The number of the initial regions should be remembered as the cube could evolve.
	 */
	private int nbInitialRegions;
	
	/**
	 * The number of labels currently contained in the cube.
	 */
	private int nbRegions;

	/**
	 * The core of the cube.
	 */
	private int[][][] labels;
	
	/**
	 * Creates an empty cube of labels.
	 * 
	 * @see LabelMatrix3D#LabelMatrix(int, int, int) create a matrix of labels from the pixels.
	 */
	public LabelMatrix3D() { }
	
	/**
	 * Creates a cube of labels where the number of labels equals the number of voxels.
	 * 
	 * <pre>
	 * Number of labels = width * height * depth
	 * </pre>
	 * 
	 * @param width should be > 0
	 * @param height should be > 0
	 * @param depth should be > 0
	 * 
	 * @see LabelMatrix3D#LabelMatrix() instanciate an empty label of matrix
	 */
	public LabelMatrix3D(int width, int height, int depth) {
		
		this.labels = new int[width][height][depth];
		
		int label = 0;
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++) {
				for(int z = 0; z < this.getDepth(); z++) {
				
					this.labels[x][y][z] = label++;
				}
			}
		}
		
		this.nbRegions = label;
		this.nbInitialRegions = this.nbRegions;
	}

	/**
	 * Assign one label to all cells of the cube
	 * @param l initial label
	 */
	public void fill(int l) {
		
		for(int y = 0; y < this.getHeight(); ++y){
			for(int x=0; x < this.getWidth(); x++) {
			
				Arrays.fill(this.labels[x][y], l);
			}
		}
	}

	
	/**
	 * Assigns a label to all defined voxels.
	 * 
	 * @param voxels regrouped in a region; should not be null
	 * @param label associated to a region
	 * 
	 * @throws NullPointerException if voxel is null
	 */
	public void fill(ArrayList<Voxel> voxels, int label) {
	
		for(Voxel v : voxels) {
			
			this.setLabel(label, v.x, v.y, v.z);
		}
	}

	/**
	 * 
	 * @return the number of values for the z axis.
	 */
	public int getDepth() { 
	
		return this.labels[0][0].length; 
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
	 * @param z index of the depth
	 * @return the label at the specified coordinates
	 */
	public int getLabel(int x, int y, int z) {
		
		return this.labels[x][y][z]; 
	}

	/**
	 * 
	 * @return the core of the cube.
	 */
	public int[][][] getLabels() { 
		
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
	 * Shows the content of the x and y axis and z = 0.
	 */
	public void printXY() {
		
		System.out.println("Matrix of labels");
		
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < this.getWidth(); x++){
				
				System.out.print(labels[x][y][0] + " | ");
			}
			System.out.println("");
		}	
	}
	
	/**
	 * Shows the content of the x and z axis and y = 0.
	 */
	public void printXZ() {
		
		System.out.println("Matrix of labels");
		
		for(int x = 0; x < this.getWidth(); x++) {
			for(int z = 0; z < this.getDepth(); z++){
				
				System.out.print(labels[x][0][z] + " | ");
			}
			System.out.println("");
		}	
	}
	
	/**
	 * Shows the content of the y and z axis and x = 0.
	 */
	public void printYZ() {
		
		System.out.println("Matrix of labels");
		
		for(int y = 0; y < this.getHeight(); y++) {
			for(int z = 0; z < this.getDepth(); z++){
				
				System.out.print(labels[0][y][z] + " | ");
			}
			System.out.println("");
		}	
	}

	/**
	 * Assign a label to a cell located at x, y, z.
	 * 
	 * @param label associated to a region
	 * @param x index of the column; should be >= 0
	 * @param y index of the row; should be >= 0
	 * @param z index of the row; should be >= 0
	 */
	public void setLabel(int label, int x, int y, int z) { 
		
		this.labels[x][y][z] = label; 
	}

	/**
	 * Assign a predefined cube to the core.
	 * @param labels contains all regions configuration; should not be null
	 * 
	 * @throws NullPointerException if the parameter labels is null
	 */
	public void setLabels(int[][][] labels) {
		
		this.labels = labels;
		
		Stack<Integer> regions = new Stack<Integer>();
		
		for(int y = 0; y < this.getHeight(); ++y){
			for(int x = 0; x < this.getWidth(); x++) {
				for(int z = 0; z < this.getDepth(); z++) {
				
					if(!regions.contains(labels[x][y][z])) {
						
						regions.push(labels[x][y][z]);
					}
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
