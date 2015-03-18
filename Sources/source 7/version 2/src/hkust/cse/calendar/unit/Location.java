package hkust.cse.calendar.unit;

public class Location {
	private String _name; // location class stores the identifier of a location
	private int _capacity; // the number of people that the place can have
	
	public Location(String name){
		this._name = name;
		this._capacity = 0;
	}

	public Location(String name, int capacity){
		this._name = name;
		this._capacity = capacity;
	}
	
	public String getName(){
		return this._name;
	}
	
	/*this method should be used in order to choose the available locations*/
	public void setName(String name){
		this._name = name;
	}
	
	/*ask about this the TA*/
	@Override
	public String toString(){
		return this._name;
	}
	
	public int getCapacity(){
		return this._capacity;
	}
	
	public void setCapacity(int capacity){
		this._capacity = capacity;
	}
}
