package program;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadAndPrintXMLFile {

	public static void readFile(String fileName, List<Professor> professors, List<Course> courses) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			try {
				Document doc = docBuilder
						.parse(new File(fileName));
				// normalize text representation
				doc.getDocumentElement().normalize();
				System.out.println("Rood element of the doc is : "
						+ doc.getDocumentElement().getNodeName());
				NodeList listOfProfessors = doc
						.getElementsByTagName("instructorsList").item(0)
						.getChildNodes();
				
				// Read data of professors
				
				for (int t = 0; t < listOfProfessors.getLength(); t++) {
					Node nNode = listOfProfessors.item(t);
					if (nNode.getNodeName().equals("instructor")) {
						Professor professor = new Professor();
						Element eElement = (Element) nNode;

						int id = Integer.parseInt(eElement
								.getElementsByTagName("id").item(0)
								.getTextContent());
						professor.setId(id);

						String name = eElement.getElementsByTagName("name")
								.item(0).getTextContent();
						professor.setName(name);

						NodeList busys = eElement.getElementsByTagName("busy");
						List<Busy> busyList = new ArrayList<Busy>();

						for (int i = 0; i < busys.getLength(); i++) {
							Node nodeBusy = busys.item(i);
							Element busyE = (Element) nodeBusy;
							int day = Integer.parseInt(busyE
									.getElementsByTagName("day").item(0)
									.getTextContent());
							int slot = Integer.parseInt(busyE
									.getElementsByTagName("slot").item(0)
									.getTextContent());
							Busy busy = new Busy(day, slot);
							busyList.add(busy);
						}
						professor.setBusyList(busyList);
						professors.add(professor);
					}
				}
				
				// Read data of courses
				NodeList listOfCourses = doc
						.getElementsByTagName("coursesList").item(0).getChildNodes();
				for (int t = 0; t < listOfCourses.getLength(); t++) {
					Node nNode = listOfCourses.item(t);
					if (nNode.getNodeName().equals("course")) {
						Element eElement = (Element) nNode;
						int courseId = Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());
						String name = eElement.getElementsByTagName("name").item(0).getTextContent();
						int professorId = Integer.parseInt(eElement.getElementsByTagName("instructor").item(0).getTextContent());
						int numberOfSlot = Integer.parseInt(eElement.getElementsByTagName("nbrSlots").item(0).getTextContent());
						
						NodeList classNodeList = eElement.getElementsByTagName("class");
						List<ClassName> classNames =  new ArrayList<ClassName>();
						
						for (int i = 0; i < classNodeList.getLength(); i++) {
							Node nodeClass = classNodeList.item(i);
							Element aClass = (Element) nodeClass;
							classNames.add(new ClassName(aClass.getTextContent()));
						}
						courses.add(new Course(courseId, name, professorId, numberOfSlot, classNames));
					}
				}

			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
