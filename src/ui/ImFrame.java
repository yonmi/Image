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
