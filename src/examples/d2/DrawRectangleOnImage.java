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

import java.awt.image.BufferedImage;

import utils.ImTool;

/**
 * Example drawing some rectangles on an image using {@link ImTool}.
 */
public class DrawRectangleOnImage {

	public static void main(String[] args) {

		/* Load the image */
		String path = "xp//examples//lena.jpg";
		BufferedImage image = ImTool.read(path);
		
		/* Green rectangle parameters */
		int minX1 = 89;
		int maxX1 = 450;
		int minY1 = 50;
		int maxY1 = 420;
		int color1 = ImTool.RGB_GREEN;
		ImTool.drawRectangleOn(image, minX1, maxX1, minY1, maxY1, color1);
		
		/* Random color rectangle parameters */
		int minX2 = 261;
		int maxX2 = 379;
		int minY2 = 263;
		int maxY2 = 303;
		int color2 = ImTool.generateRandomColorRGBValue();
		ImTool.drawRectangleOn(image, minX2, maxX2, minY2, maxY2, color2);
		
		/* Showing the result */
		ImTool.show(image, 50, "Drawing rectangles");
	}
}
