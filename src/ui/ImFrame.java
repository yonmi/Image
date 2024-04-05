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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.ImTool;

/**
 * Simple {@link JFrame frame} displaying an image.
 *
 */
public class ImFrame extends JFrame implements ChangeListener{

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

	private JSlider levelSlider;
	private ArrayList<BufferedImage> images;
	private JPanel mainPanel;
	private JPanel imgPanel;
	private int percent;
	
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
		
		this.percent = percent;
		this.display(image);
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
		this.percent = percent;
		this.display(image);
	}
	
	public ImFrame(ArrayList<BufferedImage> images, int percent, String title, int sliderMin, int sliderMax, int sliderInitValue) {
		
		super(title);
		this.percent = percent;
		this.display(images, sliderMin, sliderMax, sliderInitValue);
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
	private void display(BufferedImage image) {

		if(this.percent > 0) {
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			image = ImTool.reSize(image, screenSize.width, screenSize.height, this.percent);
		}

		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
		this.getContentPane().add(label);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void display(ArrayList<BufferedImage> images, int sliderMin, int sliderMax, int sliderInitVal) {

		// main Panel
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());

		// show the first image
		this.imgPanel = new JPanel();
		this.images = images;
		
		if(percent > 0) {
			
			// Resize and show the first image
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int lastId = images.size() - 1;
			BufferedImage img = ImTool.reSize(this.images.get(lastId), screenSize.width, screenSize.height, percent);
			ImageIcon icon = new ImageIcon(img);
			this.imgPanel.add(new JLabel(icon));
		}
		this.mainPanel.add(this.imgPanel, BorderLayout.NORTH);
		
		// show slider
		this.levelSlider = new JSlider(sliderMin, sliderMax, sliderInitVal);
		this.levelSlider.setInverted(true);
		this.levelSlider.setPaintTrack(true); 
		this.levelSlider.setPaintTicks(true); 
		this.levelSlider.setPaintLabels(true); 
		this.levelSlider.setMajorTickSpacing(sliderMax/10);
		this.levelSlider.addChangeListener(this);
		this.mainPanel.add(this.levelSlider, BorderLayout.SOUTH);
        
        this.getContentPane().add(this.mainPanel);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		
		// slider value
		int sliderValue = this.levelSlider.getValue();
		int imgId = sliderValue - this.levelSlider.getMinimum();
		
		// changes for the slider
		this.setTitle(sliderValue +" regions");
		
		// update the image visu
		this.imgPanel.removeAll();
		
		if(this.percent > 0) {
			
			// Resize and show the first image
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			BufferedImage img = ImTool.reSize(this.images.get(imgId), screenSize.width, screenSize.height, this.percent);
			ImageIcon icon = new ImageIcon(img);
			this.imgPanel.add(new JLabel(icon));
		}
		
		this.imgPanel.revalidate();
		this.imgPanel.repaint();
	}
}
