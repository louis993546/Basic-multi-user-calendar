package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.unit.Location;

public class LocationStorageController {
	/* Remove the Appt from the storage */
	public final static int REMOVE = 1;

	/* Modify the Appt the storage */
	public final static int MODIFY = 2;

	/* Add a new Appt into the storage */
	public final static int NEW = 3;
	
	private static LocationStorageController instance = new LocationStorageController(LocationStorageModel.getInstance());
	private LocationStorage mLocationStorage;
	
	private LocationStorageController(LocationStorage locationStorage) {
		mLocationStorage = locationStorage;
		loadLocationFromXml();
	}
	
	public static LocationStorageController getInstance() {
		return LocationStorageController.instance;
	}
	
	public Location[] retrieveLocations() {
		return mLocationStorage.retrieveLocations();
	}
	
	public void manageLocation(Location location, int action) throws IllegalArgumentException {
		if(action == NEW) {
			if(location.getCapacity() <= 0) {
				throw new IllegalArgumentException("Location has no enough capacity!");
			}
			if(checkLocationExists(location)) {
				throw new IllegalArgumentException("Location has already exists!");
			}
			else if(location.getLocationName().isEmpty()) {
				throw new IllegalArgumentException("Location's name cannot be empty!");
			}
			else {
				mLocationStorage.saveLocation(location);
			}		
		}
		else if(action == REMOVE) {
			mLocationStorage.removeLocation(location);
		}
	}
	
	public void manageLocation(Location location, int action, String name, int capacity) throws IllegalArgumentException {
		if(action == MODIFY) {
			if(capacity <= 0) {
				throw new IllegalArgumentException("Location has no enough capacity!");
			}
			else if(!location.getLocationName().toString().equals(name)&&checkLocationExists(findLocationByName(name))) {
				throw new IllegalArgumentException("Location has already exists!");
			}
			else if(name.isEmpty()) {
				throw new IllegalArgumentException("Location's name cannot be empty!");
			}
			else {
				mLocationStorage.updateLocation(location, name, capacity);
			}	
		}
	}
	
	public boolean checkLocationExists(Location location) {
		return mLocationStorage.checkLocationExists(location);
	}
	
	public void loadLocationFromXml() {
		mLocationStorage.loadLocationFromXml();
	}
	
	public Location findLocationByName(String locationName) {
		return mLocationStorage.findLocationByName(locationName);
	}
	
	public void addLocationToToBeDeleteList(Location location) {
		mLocationStorage.addLocationToToBeDeleteList(location);
	}
	
	public Location[] getLocationInToBeDelete() {
		return mLocationStorage.getLocationInToBeDelete();
	}
}
