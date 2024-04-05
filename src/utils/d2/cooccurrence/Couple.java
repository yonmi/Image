package utils.d2.cooccurrence;

/**
 * Class determining a couple of two generic instances.
 * @param <T> generic type
 */
public class Couple<T> {

	/* Members */
	private final T member1;
	private final T member2;

	public Couple(T _member1, T _member2) {
		
		this.member1 = _member1;
		this.member2 = _member2;
	}
	
	public T getMember1() {
		
		return this.member1;
	}

	public T getMember2() {
		
		return this.member2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals (Object o) {
		
		assert o.getClass() == Couple.class;
		Couple<T> couple = (Couple<T>) o;
		return (this.member1.equals(couple.getMember1()) && this.member2.equals(couple.getMember2())) ||
			   (this.member1.equals(couple.getMember2()) && this.member2.equals(couple.getMember1()));
	}
	
	@Override
	public int hashCode() {
		
		/* Not enough tested to be trustworthy */
		String str1 = this.member1.toString() +"_"+ this.member2.toString();
		String str2 = this.member2.toString() +"_"+ this.member1.toString();
		int hash = str1.hashCode() + str2.hashCode();
		return hash;
	}

	public void print() {

		System.out.println("<"+ this.member1.toString() +", "+ this.member2.toString() +">");
	}
	
	@Override
	public String toString() {

		return "<"+ this.member1.toString() +", "+ this.member2.toString() +">";
	}
}
