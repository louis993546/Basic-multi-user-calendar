package hkust.cse.calendar.unit;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Location {
	private String _name;
	private int _capacity;
	
	public Location(){}
	
	public Location(String name){
		_name = name;
		_capacity = 50;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public int getCapacity() {
		return _capacity;
	}
	
	public void setCapacity(int capacity){
		_capacity = capacity;
	}
	
	public String toString(){
		return _name;
	}
}
