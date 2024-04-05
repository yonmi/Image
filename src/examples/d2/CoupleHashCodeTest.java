package examples.d2;

import utils.d2.cooccurrence.Couple;

public class CoupleHashCodeTest {

	public static void main(String[] args) {
		
		Couple<Integer> c1 = new Couple<Integer>(1, 2);
		Couple<Integer> c2 = new Couple<Integer>(2, 1);
		Couple<Integer> c3 = new Couple<Integer>(0, 5);
		Couple<Integer> c4 = new Couple<Integer>(5, 0);
		
		System.out.println("c1: <1, 2> hash: "+ c1.hashCode());
		System.out.println("c2: <2, 1>  hash: "+ c2.hashCode());
		System.out.println("c3: <0, 5>  hash: "+ c3.hashCode());
		System.out.println("c4: <5, 0>  hash: "+ c4.hashCode());
	}
}
