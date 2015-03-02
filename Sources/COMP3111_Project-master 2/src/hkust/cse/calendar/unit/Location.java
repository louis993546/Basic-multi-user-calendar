package hkust.cse.calendar.unit;

import java.io.Serializable;

public class Location implements Serializable {
	private String mLocationName;
	private int mCapacity;
	
	public Location(String name, int capacity) {
		mLocationName = name;
		mCapacity = capacity;
	}
	
	public String getLocationName() {
		return mLocationName;
	}
	
	public void setLocationName(String name) {
		mLocationName = name;
	}
	
	public int getCapacity() {
		return mCapacity;
	}
	
	public void setCapacity(int capacity) {
		mCapacity = capacity;
	}
	
	public String toString() {
		return mLocationName + "\t [" + mCapacity + "]";
	}
}
