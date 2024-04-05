package utils.d2.cooccurrence;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import utils.ImTool;
import utils.d2.ValueGetter;

public class CooccurrenceMatrix<T> {

	private ArrayList<T> valueList; // giving the id of each value
	private HashMap<Couple<T>, Integer> cooccHisto; // defining the matrix, couple is refers to the couple of values
	private ValueGetter<T> valueGetter;
	private ArrayList<Couple<Point>> coupleOfNeighourgs; // helping to check if a couple of neighbors have already been treated, couple here refers to neighbors

	/**
	 * Construction of a cooccurrence matrix from one band at a time.
	 * @param img image to consider.
	 * @param band number of the band to consider.
	 */
	@SuppressWarnings("unchecked")
	public CooccurrenceMatrix(BufferedImage img, int band, int connexity, ValueGetter<T> _valueGetter) {
		
		this.valueGetter = _valueGetter;
		if (this.valueGetter == null) { /* Default value getter */
			this.valueGetter = defaultValueGetter(band);
		}
		
		this.coupleOfNeighourgs = new ArrayList<Couple<Point>>();
		this.initValueSetFromOneBand(img, band);
		this.initCooccHisto();
		this.fill(img, connexity);
	}
	
	@SuppressWarnings("unchecked")
	private ValueGetter<T> defaultValueGetter(int band) {
		
		ValueGetter<Double> valueGetter = (x, y, image)->{
			return ImTool.getPixelValue(x, y, band, image);
		};
		return (ValueGetter<T>) valueGetter;
	}

	/**
	 * 
	 * @param img
	 * @param band
	 * @param _valueList
	 * @param connexity
	 * @param _valueGetter
	 */
	public CooccurrenceMatrix(BufferedImage img, int band, ArrayList<T> _valueList, int connexity, ValueGetter<T> _valueGetter) {
		
		this.valueGetter = _valueGetter;
		if (this.valueGetter == null) { /* Default value getter */
			this.valueGetter = defaultValueGetter(band);
		}

		this.coupleOfNeighourgs = new ArrayList<Couple<Point>>();
		this.valueList = _valueList;
		System.out.println("Nb of values: "+ this.getNumberOfValues() +" set: "+ this.valueList.toString());
		
		this.initCooccHisto();
		this.fill(img, connexity);
	}
	
	/**
	 * 
	 * @param img
	 * @param bandOfInterest
	 * @param valueSet
	 * @param connexity
	 */
	public CooccurrenceMatrix(BufferedImage img, int bandOfInterest, ArrayList<T> valueSet, int connexity) {
		
		this(img, bandOfInterest, valueSet, connexity, null);
	}

	/**
	 * 
	 * @param img
	 * @param valueSet
	 * @param connexity
	 */
	public CooccurrenceMatrix(BufferedImage img, ArrayList<T> valueSet, int connexity) {
		
		this(img, 0, valueSet, connexity, null); // default band = 0
	}

	private void fill(BufferedImage img, int connexity) {
		
		for (int y = 0; y <  img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				
				int neighborsCoords[][] = ImTool.getConnectedNeibhorgs(connexity, x, y);
				for (int n = 0; n < neighborsCoords.length; n++) {
					
					int nx = neighborsCoords[n][0];
					int ny = neighborsCoords[n][1];
					if (ImTool.isInStudiedAread(nx, ny, img)) {
						
						T pixelValue = this.valueGetter.getValueFrom(x, y, img);
						T neighborValue = this.valueGetter.getValueFrom(nx, ny, img);
						
						/* Check first if the couple of neighbors were not already treated */
						Point p = new Point(x, y);
						Point pn = new Point(nx, ny);
						Couple<Point> coupleNeighbors = new Couple<Point>(p, pn);
						if(!this.coupleOfNeighourgs.contains(coupleNeighbors)) { // if not in the list
							
							this.coupleOfNeighourgs.add(coupleNeighbors); // mark the already treated neighbors
							Couple<T> couple = new Couple<T>(pixelValue, neighborValue);
							int occurrence = 1;
							if(this.cooccHisto.containsKey(couple)) {
								
								occurrence = this.cooccHisto.get(couple) + 1;
							}
							this.cooccHisto.put(couple, occurrence);
						}								
					}
				}
			}
		}
		
		/* Times 2 for the diagonals */
		for(int i = 0; i < this.getNumberOfValues(); ++i) {
			
			T val = this.valueList.get(i);
			Couple<T> c = new Couple<T>(val, val);
			this.cooccHisto.put(c, this.cooccHisto.get(c) * 2);
		}
	}

	private void initCooccHisto() {

		this.cooccHisto = new HashMap<Couple<T>, Integer>();
		
		/* Determine all couples and insert them in the histo.
		 * A couple here is defined by the couple of ids of the values in valueList
		 * */
		for (int i = 0; i < this.getNumberOfValues(); i++) {
			for (int j = i; j < this.getNumberOfValues(); j++) {
				
				Couple<T> couple = new Couple<T>(this.valueList.get(i), this.valueList.get(j));
				this.cooccHisto.put((Couple<T>) couple, 0);
			}
		}
		
		System.out.println("Number of couples: "+ this.cooccHisto.size());
	}

	private void initValueSetFromOneBand(BufferedImage img, int band) {
		
		assert img != null;
		this.valueList = new ArrayList<T>();
		
		try {
			
			for (int y = 0; y < img.getHeight(); y++) {
				for (int x = 0; x < img.getWidth(); x++) {

					T value = this.valueGetter.getValueFrom(x, y, img);
					if (!this.valueList.contains(value)) {
						
						this.valueList.add(value);
					}
				}
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Nb of values: "+ this.getNumberOfValues() +" set: "+ this.valueList.toString());
	}
	
	public int getNumberOfValues() {
		
		return this.valueList.size();
	}

	public ArrayList<T> getValueSet(){
		
		return this.valueList;
	}

	public void print() {
		
		System.out.println("< Cooccurrence Histogram >");
		
		for (Entry<Couple<T>, Integer> entry: this.cooccHisto.entrySet()) {
			
			Couple<T> couple = entry.getKey();
			String val1 = couple.getMember1().toString();
			String val2 = couple.getMember2().toString();
			System.out.println("<"+ val1 +", "+ val2 +">, "+ entry.getValue());
		}
	}
	
	public void println() {
		
		System.out.println(this.cooccHisto);
	}

	public int getOcc(Couple<T> coupleOfValues) {
		
		return this.cooccHisto.get(coupleOfValues);
	}
	
	/**
	 * Add a new couple in the histogram.
	 * @param newCouple the new couple.
	 * @param newValue the new cooccurrence value.
	 */
	public void put(Couple<T> newCouple, int newValue) {
		
		this.cooccHisto.put(newCouple, newValue);
	}

	/**
	 * Check if the couple is in the matrix ~ histogram.
	 * @param couple
	 * @return true if a value is found.
	 */
	public boolean contains(Couple<T> couple) {
		
		return this.cooccHisto.containsKey(couple);
	}
	
	/**
	 * 
	 * @return the number of elements in the histogram ~ matrix.
	 */
	public int size() {

		return this.cooccHisto.size();
	}
}
