package hkust.cse.calendar.unit;

import java.util.List;  

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  

@XmlRootElement
public class LocationList {
	private List<Location> locations; 
	
	public LocationList() {}
	
	public LocationList(List<Location> locations){
		this.locations = locations;
	}

	@XmlElement  
	public List<Location> getLocations() {  
	    return locations;  
	}  
	public void setLocations(List<Location> locations) {  
	    this.locations = locations;  
	}  
	
}
