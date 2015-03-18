package hkust.cse.calendar.xmlfactory;

import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.unit.Location;

import java.io.File;
import java.io.IOException;
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

public class LocationXmlFactory {
	public void loadLocationFromXml(ArrayList<Location> mLocations) {
		File locationFile = new File(LocationStorage.locationDataFile);
		if(locationFile.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(locationFile);
				document.getDocumentElement().normalize();

				NodeList locations = document.getElementsByTagName("Location");
				for(int i = 0; i < locations.getLength(); i++) {
					Node node = locations.item(i);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;

						String name = element.getElementsByTagName("Name").item(0).getTextContent();
						int capacity = Integer.parseInt(element.getElementsByTagName("Capacity").item(0).getTextContent());
						
						Location location = new Location(name, capacity);
						mLocations.add(location);
					}
				}
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void saveLocationToXml(Location location) {
		// TODO Auto-generated method stub
		File file = new File(LocationStorage.locationDataFile);
		if(file.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(LocationStorage.locationDataFile);

				Node locations = document.getFirstChild();
				Element newLocation = document.createElement("Location");
				Element locationName = document.createElement("Name");
				locationName.appendChild(document.createTextNode(location.getLocationName()));
				newLocation.appendChild(locationName);
				Element locationCapacity = document.createElement("Capacity");
				locationCapacity.appendChild(document.createTextNode(new Integer(location.getCapacity()).toString()));
				newLocation.appendChild(locationCapacity);

				locations.appendChild(newLocation);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(LocationStorage.locationDataFile));
				transformer.transform(source, result);

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		else {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document document = docBuilder.newDocument();
				Element locations = document.createElement("Locations");
				document.appendChild(locations);

				Element newLocation = document.createElement("Location");
				locations.appendChild(newLocation);
					
				Element locationName = document.createElement("Name");
				locationName.appendChild(document.createTextNode(location.getLocationName()));
				newLocation.appendChild(locationName);
				
				Element locationCapacity = document.createElement("Capacity");
				locationCapacity.appendChild(document.createTextNode(new Integer(location.getCapacity()).toString()));
				newLocation.appendChild(locationCapacity);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(LocationStorage.locationDataFile));

				transformer.transform(source, result);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateLocationInXml(Location location, String locationName, int locationCapacity) {
		// TODO Auto-generated method stub
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(LocationStorage.locationDataFile);

			NodeList locations = doc.getElementsByTagName("Location");
			for(int i = 0; i < locations.getLength(); i++) {
				Node locationNode = locations.item(i);
				if(locationNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eLocation = (Element) locationNode;
					if(location.getLocationName().equals(eLocation.getElementsByTagName("Name").item(0).getTextContent())) {
						eLocation.getElementsByTagName("Name").item(0).setTextContent(locationName);
						eLocation.getElementsByTagName("Capacity").item(0).setTextContent(new Integer(locationCapacity).toString());
						break;
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(LocationStorage.locationDataFile));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addLocationToToBeDeleteList(Location location) {
		File fileObject = new File(LocationStorage.toBeDeleteLocationFile);
		if(fileObject.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(LocationStorage.toBeDeleteLocationFile);

				Node locations = document.getFirstChild();
				Element newLocation = document.createElement("Location");
				newLocation.appendChild(document.createTextNode(location.getLocationName()));

				locations.appendChild(newLocation);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(LocationStorage.toBeDeleteLocationFile));
				transformer.transform(source, result);

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		else {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document document = docBuilder.newDocument();
				Element locations = document.createElement("Locations");
				document.appendChild(locations);

				Element newLocation = document.createElement("Location");
				locations.appendChild(newLocation);
				newLocation.appendChild(document.createTextNode(location.getLocationName()));

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(LocationStorage.toBeDeleteLocationFile));

				transformer.transform(source, result);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Location[] getLocationInToBeDelete() {
		String locationFile = LocationStorage.toBeDeleteLocationFile;
		File fileObject = new File(locationFile);
		ArrayList<Location> locations = new ArrayList<Location>();
		
		if(fileObject.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(locationFile);
				document.getDocumentElement().normalize();

				NodeList locationList = document.getElementsByTagName("Location");
				for(int i = 0; i < locationList.getLength(); i++) {
					Node node = locationList.item(i);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						String name = element.getTextContent();
						
						Location location = new Location(name, 0);
						locations.add(location);
					}
				}
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return locations.toArray(new Location[locations.size()]);
	}
	
	public void deleteLocationInToBeDelete(Location location) {
		String file = LocationStorage.toBeDeleteLocationFile;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			Node locationRootNode = doc.getFirstChild();
			NodeList locations = doc.getElementsByTagName("Location");
			for(int i = 0; i < locations.getLength(); i++) {
				Node locationNode = locations.item(i);
				if(locationNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eLocation = (Element) locationNode;
					if(location.getLocationName().equals(eLocation.getTextContent())) {
						locationRootNode.removeChild(locationNode);
						break;
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(file));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeLocationFromXml(Location location) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(LocationStorage.locationDataFile);

			Node locationRootNode = doc.getFirstChild();
			NodeList locations = doc.getElementsByTagName("Location");
			for(int i = 0; i < locations.getLength(); i++) {
				Node locationNode = locations.item(i);
				if(locationNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eLocation = (Element) locationNode;
					if(location.getLocationName().equals(eLocation.getElementsByTagName("Name").item(0).getTextContent())) {
//						System.out.println("hi");
						locationRootNode.removeChild(locationNode);
						break;
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(LocationStorage.locationDataFile));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
