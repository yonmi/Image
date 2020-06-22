package utils;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Create a segment for each "composantes connexe". Use 8-connexity. 
 * Use equality for pixels on integer level.
 */
public class SegmentByConnexityRaw {

	// Inputs parameters
	public BufferedImage inputImageRaw;
	public HashMap<Integer,ArrayList<Double>> inputRegion;  
	
	public int xDim;
	public int yDim;
	public int bDim;

	// Outputs parameters
	public LabelMatrix labelMatrix;
	HashMap<Integer, Integer> output = new HashMap<Integer, Integer>();
	
	/**
	 * Constructor
	 * 
	 */
	public SegmentByConnexityRaw(BufferedImage src) {
		
		this.inputImageRaw=src;	
		this.xDim=this.inputImageRaw.getWidth();
		this.yDim=this.inputImageRaw.getHeight();
		this.bDim=ImTool.getNbBandsOf(this.inputImageRaw);
	}
	
	public LabelMatrix runForFullImage(){
		
		this.labelMatrix = new LabelMatrix(this.xDim, this.yDim);	
		this.labelMatrix.fill(-1);
	
		int label = 0;
		int x, y;
		for (x = 0; x < this.xDim; ++x)
			for (y = 0; y < this.yDim; ++y)
				if (this.labelMatrix.getLabel(x, y) == -1)
					newSegmentForFullImage(x, y, label++);
		
		this.labelMatrix.setNbRegions(label);	
		return labelMatrix;
	}
	
	
	
	public HashMap<Integer,Integer> runForRegions(){
		
		for(int p:inputRegion.keySet()){
			output.put(p,-1);
		}
		
		int label = 0;
		int x, y;
		for(int p:inputRegion.keySet()){
			x=p % xDim;
			y=p / xDim;
			
			if (output.get(p) == -1){
				newSegmentForRegions(x, y, label++);
			}
		}	
		return output;
	}
	
	
	private void newSegmentForFullImage(int x, int y, int label) {
		
		this.labelMatrix.setLabel(label, x, y);

		Stack<Point> fifo = new Stack<Point>();
		fifo.push(new Point(x, y));

		Point p;
		int l, k;
		while (!fifo.empty()) {
			p = fifo.pop();
			this.labelMatrix.setLabel(label, p.x, p.y);

			// For every pixel in the 8-neighbourhood of the pixel
			for (l = p.y - 1; l <= p.y + 1; ++l) {
				for (k = p.x - 1; k <= p.x + 1; ++k) {
					if (k < 0 || k >= this.xDim || l < 0 || l >= this.yDim)
						continue;
					
					if (!(k == p.x && l == p.y)) {
						
						if (this.labelMatrix.getLabel(k, l) == -1
							&& areEqualsForFullImage(p.x, p.y, k, l)){				
							fifo.push(new Point(k, l));
						}
					}
				}
			}
		}
	}
	
	
	private void newSegmentForRegions(int x, int y, int label) {
		output.put( (y*xDim) + x ,label);

		Stack<Point> fifo = new Stack<Point>();
		fifo.push(new Point(x, y));

		Point p;
		int l, k;
		while (!fifo.empty()) {
			p = fifo.pop();
			output.put( p.y*xDim + p.x ,label);

			// For every pixel in the 8-neighbourhood of the pixel
			for (l = p.y - 1; l <= p.y + 1; ++l) {
				for (k = p.x - 1; k <= p.x + 1; ++k) {
					if (k < 0 || k >= xDim || l < 0|| l >= yDim)
						continue;
					
					if (!(k == p.x && l == p.y) && inputRegion.containsKey(l*(xDim)+k)) {
						if (output.get(l*(xDim)+k) == -1 
							&& areEqualsRegion(p.x, p.y, k, l)){
							fifo.push(new Point(k, l));
						}
					}
				}
			}
		}
	}
	

	private boolean areEqualsForFullImage(int x1, int y1, int x2, int y2) {
		
		for (int b = 0; b < bDim; ++b)
			if (ImTool.getPixelValue(x1, y1, b, this.inputImageRaw) != 
				ImTool.getPixelValue(x2, y2, b, this.inputImageRaw))
				return false;
		
		return true;
	}
	
	private boolean areEqualsRegion(int x1, int y1, int x2, int y2) {
		
		int p1= y1*xDim + x1;	
		int p2= y2*xDim + x2;
		
		for (int b = 0; b < bDim; ++b){
			if (inputRegion.get(p1).get(b).doubleValue() != inputRegion.get(p2).get(b).doubleValue()){	
				return false;
			}
		}
		return true;
	}
	
}
