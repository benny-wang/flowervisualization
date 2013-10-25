package visualization;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.awt.Color;

/*
String - Repository name
Array[Contributor class] - Contributors
Array[Commit Class] - Commits
Array[Class Node] - Classes
Array[Method Node] - Methods
Int - average class life?? (for flower color saturation ratio)
Int - largest lines of code of class (for flower ratio)
Int - smallest lines of code
Int - current commit number

 */

public class Repository {
	public String name;
	public Contributor[] contributors;
	public Commit[] commits;
	public ClassNode[] classes;
	public MethodNode[] methods;
	
	public Frame[] frames;
	
	public Repository(String xmlFilename) {
		readXMLFile(xmlFilename);
	}
	
	public void readXMLFile (String fillename) {
		  
		
		//Sample from http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		    try {
		 
			File fXmlFile = new File(fillename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList frameList = doc.getElementsByTagName("frame");
		 
			System.out.println("----------------------------");
			
			this.frames = new Frame[frameList.getLength()];
		 
			for (int i = 0; i < frameList.getLength(); i++) {
		 
				Element XMLFrame = (Element) frameList.item(i);
		 
				System.out.println("\nCurrent Element :" + XMLFrame.getNodeName());
				
				NodeList flowerList = XMLFrame.getElementsByTagName("flower");  
	
				
				this.frames[i] = new Frame(new Flower[flowerList.getLength()]);
				
				for ( int j = 0; j < flowerList.getLength(); j++ ) {
					Node flowerNode = flowerList.item(j);													
					Element eElement = (Element) flowerNode;					
										
				
					System.out.println("Flower id : " + eElement.getAttribute("id"));
					Color color = Color.blue;
					int size = Integer.parseInt(eElement.getElementsByTagName("size").item(0).getTextContent());
					System.out.println("Size : " + size);
					int x = Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent());
					System.out.println("X : " + x);
					int y = Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent());
					System.out.println("Y : " + y);
					int numMethods = Integer.parseInt(eElement.getElementsByTagName("numMethods").item(0).getTextContent());
					System.out.println("Number of Methods : " + numMethods);
										
					Flower flower = new Flower(color,size,x,y,numMethods);
					this.frames[i].flowers[j] = flower;
				}
	 
			}
			
			
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		  }
	
}
