package utils.d2.cooccurrence;

public class Value<T> {

	private int id;
	private T val;
	
	public Value(int _id, T _val) {
		
		this.id = _id;
		this.val = _val;
	}
	
	public int getId() {
		
		return this.id;
	}
	
	public T getValue() {
		
		return this.val;
	}
}
