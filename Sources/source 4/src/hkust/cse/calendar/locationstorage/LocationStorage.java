package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.xmlfactory.LocationXmlFactory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class LocationStorage {
	public static LocationStorage instance = null;
	public ArrayList<Location> mLocations;
	public LocationXmlFactory xmlFactory;
	public static final String locationDataFile = "location.xml";
	public static final String toBeDeleteLocationFile = "locationToBeDelete.xml";
	
	public abstract void saveLocation(Location location);	//abstract method to save an appointment record

	public abstract void updateLocation(Location location, String newLocationName, int newLocationCapacity);	//abstract method to remove an appointment record
	
	public abstract void removeLocation(Location location);	//abstract method to remove an appointment record
	
	public abstract void saveLocationToXml(Location location);		//abstract method to load appointment from xml reocrd into hash map
	
	public abstract void updateLocationInXml(Location location, String newLocationName, int newLocationCapacity);		//abstract method to load appointment from xml reocrd into hash map
	
	public abstract void removeLocationFromXml(Location location);
	
	public abstract void loadLocationFromXml();		//abstract method to load appointment from xml reocrd into hash map
	
	public abstract Location[] retrieveLocations();	//abstract method to retrieve an appointment record by a given timespan
	
	public abstract boolean checkLocationExists(Location location);	
	
	public abstract Location findLocationByName(String locationName);
	
	public abstract void addLocationToToBeDeleteList(Location location);
	
	public abstract Location[] getLocationInToBeDelete();
}
