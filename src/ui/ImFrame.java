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

package ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import utils.ImTool;

/**
 * Simple {@link JFrame frame} displaying an image.
 *
 */
public class ImFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Indicates if the image has to be displayed covering a precised percent of the screen height.
	 */
	public static final int IMAGE_DEFAULT_SIZE = 60;

	/**
	 * Indicates if the image has to be displayed with a medium size.
	 */
	public static final int IMAGE_STD_SIZE = 20;

	/**
	 * Indicates if the image has to be displayed with the real size. 
	 */
	public static final int IMAGE_REAL_SIZE = 0;

	/**
	 * Creates a frame and prints an image on the screen.
	 * 
	 * <p>
	 * The default title to use is the name of the image.
	 * 
	 * @param image to show; must not be null
	 * @param percent defines how the image covers the screen
	 * 
	 * Example of values are:
	 * <li> {@link ImFrame#IMAGE_REAL_SIZE }
	 * <li> {@link ImFrame#IMAGE_DEFAULT_SIZE }
	 * 
	 * @exception NullPointerException if image is null
	 * 
	 * @see ImFrame#ImFrame(BufferedImage, String) create an image frame with a specific title
	 */
	public ImFrame(BufferedImage image, int percent) {

		super();
		
		if(ImTool.is(image).registered) {
			
			this.setTitle(ImTool.getNameOf(image));
			
		}else {
			
			this.setTitle("Image");
		}
		
		this.display(image, percent);
	}
	
	/**
	 * Creates a specific entitled frame and prints an image on the screen.
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
	 * 
	 * @see ImFrame#ImFrame(BufferedImage) create an image frame without specifying a title
	 */
	public ImFrame(BufferedImage image, int percent, String title) {
		
		super(title);
		this.display(image, percent);
	}

	/**
	 * Resizes the image to cover a precised percent of the height of the screen.
	 * Shows the image inside a frame.
	 * 
	 * @param image to show; must not be null
	 * @param percent of the screen to cover; real size if < 0, else uses an {@link ImTool#estiMateSize(BufferedImage, int, int, int) estimated size}
	 * 
	 * @throws NullPointerException if image is null
	 */
	private void display(BufferedImage image, int percent) {

		if(percent > 0) {
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			image = ImTool.reSize(image, screenSize.width, screenSize.height, percent);
		}

		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
		this.getContentPane().add(label);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
