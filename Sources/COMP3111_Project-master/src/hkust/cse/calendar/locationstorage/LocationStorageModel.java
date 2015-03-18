package hkust.cse.calendar.locationstorage;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import hkust.cse.calendar.unit.Admin;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.RegularUser;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorage;
import hkust.cse.calendar.xmlfactory.LocationXmlFactory;

public class LocationStorageModel extends LocationStorage {

	public static LocationStorage getInstance() {
		if(LocationStorage.instance == null) {
			LocationStorage.instance = new LocationStorageModel();
		}
		return LocationStorage.instance;
	}

	public LocationStorageModel() {
		mLocations = new ArrayList<Location>();
		xmlFactory = new LocationXmlFactory();
	}

	@Override
	public void saveLocation(Location location) {
		// TODO Auto-generated method stub
		mLocations.add(location);
		saveLocationToXml(location);
	}

	public void saveLocationButNotXml(Location location) {
		mLocations.add(location);
	}

	@Override
	public void updateLocation(Location location, String newLocationName, int newLocationCapacity) {
		// TODO Auto-generated method stub
		updateLocationInXml(location, newLocationName, newLocationCapacity);
		location.setLocationName(newLocationName);
		location.setCapacity(newLocationCapacity);
	}

	@Override
	public void removeLocation(Location location) {
		// TODO Auto-generated method stub
		mLocations.remove(location);
		removeLocationFromXml(location);
	}

	public void removeLocationFromXml(Location location) {
		xmlFactory.removeLocationFromXml(location);
	}

	@Override
	public void loadLocationFromXml() {
		// TODO Auto-generated method stub
		xmlFactory.loadLocationFromXml(mLocations);
	}

	@Override
	public Location[] retrieveLocations() {
		// TODO Auto-generated method stub
		return mLocations.toArray(new Location[mLocations.size()]);
	}

	@Override
	public boolean checkLocationExists(Location location) {
		// TODO Auto-generated method stub
		if(location == null) {
			return false;
		}
		
		for(int i = 0; i < mLocations.size(); i++) {
			if(location.getLocationName().equals(mLocations.get(i).getLocationName())) {
				return true;
			}
		}
		return false;
	}

	public Location findLocationByName(String locationName) {
		for(int i = 0; i < mLocations.size(); i++) {
			if(mLocations.get(i).getLocationName().equals(locationName)) {
				return mLocations.get(i);
			}
		}
		return null;
	}

	@Override
	public void saveLocationToXml(Location location) {
		// TODO Auto-generated method stub
		xmlFactory.saveLocationToXml(location);
	}

	@Override
	public void updateLocationInXml(Location location, String locationName, int locationCapacity) {
		// TODO Auto-generated method stub
		xmlFactory.updateLocationInXml(location, locationName, locationCapacity);
	}

	@Override
	public void addLocationToToBeDeleteList(Location location) {
		// TODO Auto-generated method stub
		xmlFactory.addLocationToToBeDeleteList(location);
	}

	@Override
	public Location[] getLocationInToBeDelete() {
		// TODO Auto-generated method stub
		return xmlFactory.getLocationInToBeDelete();
	}

}
