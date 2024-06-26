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

package examples.d2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import ui.ImFrame;
import utils.ImTool;
import utils.d2.LabelMatrix;

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
