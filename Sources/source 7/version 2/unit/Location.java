package hkust.cse.calendar.unit;

public class Location {
	private String _name; // location class stores the identifier of a location
	
	public Location(String name){
		_name = name;
	}

	public String getName(){
		return _name;
	}
	
	/*this method should be used in order to choose the available locations*/
	public void setName(String name){
		_name = name;
	}
	
	/*ask about this the TA*/
	@Override
	public String toString(){
		return _name;
	}
}
