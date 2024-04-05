package examples.d2;

import utils.d2.cooccurrence.Couple;

public class CoupleExample {

	public static void main(String[] args) {
		
		Couple<Integer> c1 = new Couple<Integer>(1, 2);
		Couple<Integer> c2 = new Couple<Integer>(2, 1);
		Couple<Integer> c3 = new Couple<Integer>(0, 5);
		
		System.out.println("c1: <1, 2>");
		System.out.println("c2: <2, 1>");
		System.out.println("c3: <0, 5>");
		System.out.println("-----------");
		System.out.println("Are equals c1 and c1 ? "+ c1.equals(c1));
		System.out.println("Are equals c1 and c2 ? "+ c1.equals(c2));
		System.out.println("Are equals c2 and c1 ? "+ c2.equals(c1));
		System.out.println("Are equals c1 and c3 ? "+ c1.equals(c3));
		System.out.println("Are equals c2 and c3 ? "+ c2.equals(c3));
	}
}
