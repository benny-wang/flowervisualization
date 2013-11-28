package visualization;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;


public class Repository {
	public Map<String, Contributor> contributorColor = new HashMap<String, Contributor>();
	public Map<String, FlowerPackage> packageColor = new HashMap<String, FlowerPackage>();
	public Map<String, Flower> flowers = new HashMap<String, Flower>();
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
		 
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList frameList = doc.getElementsByTagName("frame");
		 
			
			
			this.frames = new Frame[frameList.getLength()];
		 
			for (int i = 0; i < frameList.getLength(); i++) {
		 
				Element XMLFrame = (Element) frameList.item(i);
		
				System.out.println("----------------------------");
				
				int frameID = Integer.parseInt(XMLFrame.getAttribute("id"));
				System.out.println("Frame: " + frameID);
				
				int frameTime = Integer.parseInt(XMLFrame.getElementsByTagName("time").item(0).getTextContent());
				//System.out.println("Flower id : " + methodName);
				
				NodeList flowerList = XMLFrame.getElementsByTagName("flower");  
	

				
				this.frames[i] = new Frame(frameID, frameTime, new Flower[flowerList.getLength()]);
				
				for ( int j = 0; j < flowerList.getLength(); j++ ) {
					Node flowerNode = flowerList.item(j);													
					Element eElement = (Element) flowerNode;					
										
					String className = eElement.getElementsByTagName("name").item(0).getTextContent();
					System.out.println("Class: " + className);

					int size = Integer.parseInt(eElement.getElementsByTagName("size").item(0).getTextContent());
					System.out.println("Size : " + size);	
					
					int numMethods = Integer.parseInt(eElement.getElementsByTagName("numMethods").item(0).getTextContent());
					System.out.println("Number of Methods : " + numMethods);
					
					String contributor = eElement.getElementsByTagName("committer").item(0).getTextContent();
					System.out.println("Contributor : " + contributor);
										
					String packageName = eElement.getElementsByTagName("package").item(0).getTextContent();
					System.out.println("Package : " + packageName);
					
					int age = Integer.parseInt(eElement.getElementsByTagName("age").item(0).getTextContent());
					System.out.println("Age : " + age);
					
//					String strChanged = eElement.getElementsByTagName("changed").item(0).getTextContent();
//					Boolean changed = (strChanged.equals("true"))? true: false;					
//					System.out.println("Changed : " + changed);
					
					NodeList dependencyName = eElement.getElementsByTagName("dependency");
					
					Map<String, Integer> dependencies = new HashMap<String, Integer>();
					System.out.println("Dependency : " + dependencyName.getLength());
					
					for ( int k = 0; k < dependencyName.getLength(); k++ ) {
						Node dependencyNameNode = dependencyName.item(k);													
						Element dependencyNameElement = (Element) dependencyNameNode;
						String methods = dependencyNameElement.getTextContent();
						int callCount = Integer.parseInt(dependencyNameElement.getAttribute("count"));
						System.out.println("Dependency : " + methods);
						
						dependencies.put(methods, new Integer(callCount));
					}					
					
					if(contributorColor.get(contributor) == null){
						contributorColor.put(contributor, new Contributor(contributor, Color.white));
					}
										
					if(packageColor.get(packageName) == null){
						packageColor.put(packageName, new FlowerPackage(packageName, Color.white));
					}
					
					Flower flower = new Flower(className, contributorColor.get(contributor).color,size,0,0,numMethods, contributor, dependencies, age);
					flower.changed = false;
					flower.packageName = packageName;
					this.frames[i].flowers[j] = flower;
				}	 
			}
			
			initFields();
			
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		  }

	private void initFields() {
		int contributorSize = contributorColor.size();	
		float contributorCount = 0;
		
		for (Map.Entry<String, Contributor> entry : contributorColor.entrySet()) {
		    Contributor contributor = entry.getValue();
		    
		    contributor.color = new Color(Color.HSBtoRGB(contributorCount/contributorSize, 1.0f, 1.0f));
		    
		    contributorCount += 1;
		}
		
		int packageSize = packageColor.size();	
		float packageCount = 0;
		
		for (Map.Entry<String, FlowerPackage> entry : packageColor.entrySet()) {
		    FlowerPackage flowerPackage = entry.getValue();
		    
		    flowerPackage.color = new Color(Color.HSBtoRGB(packageCount/packageSize, 1.0f, (float) (Math.random() * (1 - .5) + .5)));
		    
		    packageCount += 1;
		}
		
		setFlowerSize();
	}
	
	private void setFlowerSize() {
		
		for (int i = 0; i<frames.length; i++){
			Frame frame = frames[i];
			for (int j = 0; j<frame.flowers.length; j++){
				Flower flower = frame.flowers[j];
				
				flower.size = (int)(.5 * Math.sqrt(flower.size));				
				flower.color = contributorColor.get(flower.contributor).color;
				
				if(this.flowers.get(flower.methodName) == null){
					this.flowers.put(flower.methodName, new Flower(flower));
				}
				
				if(i == 0){
					flowers.get(flower.methodName).color = flower.color;
				}
				
				if(i == frames.length -1){
					
					Flower repoFlower = this.flowers.get(flower.methodName);
					repoFlower.size = flower.size;
				
					repoFlower.x = (int)(Math.random() * Visualization.width) - Visualization.legendWidth;
					repoFlower.y = (int)(Math.random() * (Visualization.height));
				}

			}
		}
	}
	
}
