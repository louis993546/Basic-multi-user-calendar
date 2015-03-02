package hkust.cse.calendar.xmlfactory;

import hkust.cse.calendar.system.UserFactory;
import hkust.cse.calendar.unit.Admin;
import hkust.cse.calendar.unit.RegularUser;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorage;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

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

public class UserXmlFactory {

	public void loadUserFromXml(String file, HashMap<String, User> mUsers) {
		File userDataFile = new File(file);
		if(userDataFile.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(userDataFile);
				document.getDocumentElement().normalize();

				NodeList users = document.getElementsByTagName("User");

				for(int i = 0; i < users.getLength(); i++) {
					Node node = users.item(i);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;

						String id = element.getElementsByTagName("ID").item(0).getTextContent();
						String password = element.getElementsByTagName("Password").item(0).getTextContent();
						String email = element.getElementsByTagName("Email").item(0).getTextContent();
						String role = element.getElementsByTagName("Role").item(0).getTextContent();

						Element name = (Element) element.getElementsByTagName("Name").item(0);
						String firstName = name.getElementsByTagName("FirstName").item(0).getTextContent();
						String lastName = name.getElementsByTagName("LastName").item(0).getTextContent();

						Element birthday = (Element) element.getElementsByTagName("Birthday").item(0);
						String yearString = birthday.getElementsByTagName("Year").item(0).getTextContent();
						String monthString = birthday.getElementsByTagName("Month").item(0).getTextContent();
						String dateString = birthday.getElementsByTagName("Date").item(0).getTextContent();
						int year = Integer.parseInt(yearString);
						int month = Integer.parseInt(monthString);
						int date = Integer.parseInt(dateString);

						User user = UserFactory.getInstance().createUser(role, id, password);
						user.setEmail(email);
						user.setName(firstName, lastName);
						int[] bday = {year, month, date};
						Timestamp start = TimeSpan.CreateTimeStamp(bday, 0);
						Timestamp end = TimeSpan.CreateTimeStamp(bday, 23 * 60 + 59);
						user.setBirthday(new TimeSpan(start, end));

						mUsers.put(user.ID(), user);

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

	public void updateUserInXml(String file, User user) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			NodeList users = doc.getElementsByTagName("User");
			for(int i = 0; i < users.getLength(); i++) {
				Node userNode = users.item(i);
				if(userNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eUser = (Element) userNode;
					if(user.ID().equals(eUser.getElementsByTagName("ID").item(0).getTextContent())) {
						eUser.getElementsByTagName("Password").item(0).setTextContent(user.Password());
						eUser.getElementsByTagName("Email").item(0).setTextContent(user.getEmail());
						Element name = (Element) eUser.getElementsByTagName("Name").item(0);
						name.getElementsByTagName("FirstName").item(0).setTextContent(user.getFirstName());
						name.getElementsByTagName("LastName").item(0).setTextContent(user.getLastName());
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

	public void saveUserToXml(String file, User user) {
		// TODO Auto-generated method stub
		File fileObject = new File(file);
		if(fileObject.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(file);

				Node users = document.getFirstChild();
				Element newUser = document.createElement("User");

				Element userId = document.createElement("ID");
				userId.appendChild(document.createTextNode(user.ID()));
				newUser.appendChild(userId);

				Element password = document.createElement("Password");
				password.appendChild(document.createTextNode(user.Password()));
				newUser.appendChild(password);

				Element role = document.createElement("Role");
				role.appendChild(document.createTextNode(user.getRole()));
				newUser.appendChild(role);

				Element name = document.createElement("Name");
				Element firstName = document.createElement("FirstName");
				firstName.appendChild(document.createTextNode(user.getFirstName()));
				name.appendChild(firstName);
				Element lastName = document.createElement("LastName");
				lastName.appendChild(document.createTextNode(user.getLastName()));
				name.appendChild(lastName);
				newUser.appendChild(name);

				Element email = document.createElement("Email");
				email.appendChild(document.createTextNode(user.getEmail()));
				newUser.appendChild(email);

				Element birthday = document.createElement("Birthday");
				Element year = document.createElement("Year");
				year.appendChild(document.createTextNode(new Integer(user.getBirthday().StartTime().getYear() + 1900).toString()));
				birthday.appendChild(year);
				Element month = document.createElement("Month");
				month.appendChild(document.createTextNode(new Integer(user.getBirthday().StartTime().getMonth() + 1).toString()));
				birthday.appendChild(month);
				Element date = document.createElement("Date");
				date.appendChild(document.createTextNode(new Integer(user.getBirthday().StartTime().getDate()).toString()));
				birthday.appendChild(date);
				newUser.appendChild(birthday);

				users.appendChild(newUser);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
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
		else {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document document = docBuilder.newDocument();
				Element users = document.createElement("Users");
				document.appendChild(users);

				Element newUser = document.createElement("User");
				users.appendChild(newUser);

				Element userId = document.createElement("ID");
				userId.appendChild(document.createTextNode(user.ID()));
				newUser.appendChild(userId);

				Element password = document.createElement("Password");
				password.appendChild(document.createTextNode(user.Password()));
				newUser.appendChild(password);

				Element role = document.createElement("Role");
				role.appendChild(document.createTextNode(user.getRole()));
				newUser.appendChild(role);

				Element name = document.createElement("Name");
				Element firstName = document.createElement("FirstName");
				firstName.appendChild(document.createTextNode(user.getFirstName()));
				name.appendChild(firstName);
				Element lastName = document.createElement("LastName");
				lastName.appendChild(document.createTextNode(user.getLastName()));
				name.appendChild(lastName);
				newUser.appendChild(name);

				Element email = document.createElement("Email");
				email.appendChild(document.createTextNode(user.getEmail()));
				newUser.appendChild(email);

				Element birthday = document.createElement("Birthday");
				Element year = document.createElement("Year");
				year.appendChild(document.createTextNode(new Integer(user.getBirthday().StartTime().getYear() + 1900).toString()));
				birthday.appendChild(year);
				Element month = document.createElement("Month");
				month.appendChild(document.createTextNode(new Integer(user.getBirthday().StartTime().getMonth() + 1).toString()));
				birthday.appendChild(month);
				Element date = document.createElement("Date");
				date.appendChild(document.createTextNode(new Integer(user.getBirthday().StartTime().getDate()).toString()));
				birthday.appendChild(date);
				newUser.appendChild(birthday);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(file));

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

	public void removeUserFromXml(String file, User user) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			Node userRootNode = doc.getFirstChild();
			NodeList users = doc.getElementsByTagName("User");
			for(int i = 0; i < users.getLength(); i++) {
				Node userNode = users.item(i);
				if(userNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eUser = (Element) userNode;
					if(user.ID().equals(eUser.getElementsByTagName("ID").item(0).getTextContent())) {
						userRootNode.removeChild(userNode);
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

	public void addUserToToBeDeletedListXml(String file, User user) {
		File fileObject = new File(file);
		if(fileObject.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(file);

				Node users = document.getFirstChild();
				Element newUser = document.createElement("User");
				newUser.appendChild(document.createTextNode(user.ID()));

				users.appendChild(newUser);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
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
		else {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document document = docBuilder.newDocument();
				Element users = document.createElement("Users");
				document.appendChild(users);

				Element newUser = document.createElement("User");
				users.appendChild(newUser);
				newUser.appendChild(document.createTextNode(user.ID()));

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(file));

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

	public void loadUserFromToBeDeletedListXml(String file, ArrayList<String> mToBeDeletedUsers) {
		File userDataFile = new File(file);
		if(userDataFile.isFile()) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document document = docBuilder.parse(userDataFile);
				document.getDocumentElement().normalize();

				NodeList users = document.getElementsByTagName("User");
				for(int i = 0; i < users.getLength(); i++) {
					Node node = users.item(i);
					Element eUser = (Element) node;
					String userId = eUser.getTextContent();	
					mToBeDeletedUsers.add(userId);
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
	
	public void removeUserFromToBeDeletedListXml(String file, User user) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			Node userRootNode = doc.getFirstChild();
			NodeList users = doc.getElementsByTagName("User");
			for(int i = 0; i < users.getLength(); i++) {
				Node userNode = users.item(i);
				if(userNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eUser = (Element) userNode;
					if(user.ID().equals(eUser.getTextContent())) {
						userRootNode.removeChild(userNode);
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
}
