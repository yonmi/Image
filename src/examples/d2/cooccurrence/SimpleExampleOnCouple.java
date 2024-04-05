package examples.d2.cooccurrence;

import utils.d2.cooccurrence.Couple;

public class SimpleExampleOnCouple {

	public static void main(String[] args) {
		
		System.out.println("Example on the Couple class.");
		
		Couple<Integer> c1 = new Couple<Integer>(1, 2);
		c1.print();
		
		Couple<Integer> c2 = new Couple<Integer>(1, 3);
		c2.print();
		
		Couple<Integer> c3 = new Couple<Integer>(2, 1);
		c3.print();

		System.out.println("C1 is equals to C1: "+ c1.equals(c1));
		System.out.println("C1 is not equals to C2: "+ c1.equals(c2));
		System.out.println("C1 is equals to C3: "+ c1.equals(c3));
	}
}
