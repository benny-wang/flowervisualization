package visualization;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
	public Map<String, Contributor> contributorColor = new HashMap<String, Contributor>();
	public Map<String, FlowerPackage> packageColor = new HashMap<String, FlowerPackage>();
	public Map<String, Flower> flowers = new HashMap<String, Flower>();
	public Frame[] frames;
	
	public int classNum;
	public int maxClassLines;
	public int maxGridX;
	public int maxGridY;
	
	public Contributor[] contributors;
	private int maxDependenciesNumber;
	
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
										
					String methodName = eElement.getElementsByTagName("name").item(0).getTextContent();
					//System.out.println("Flower id : " + methodName);

					int size = Integer.parseInt(eElement.getElementsByTagName("size").item(0).getTextContent());
					//System.out.println("Size : " + size);	
					
					int numMethods = Integer.parseInt(eElement.getElementsByTagName("numMethods").item(0).getTextContent());
					//System.out.println("Number of Methods : " + numMethods);
					String contributor = eElement.getElementsByTagName("committer").item(0).getTextContent();
					System.out.println("Contributor : " + contributor);
										
					String packageName = eElement.getElementsByTagName("package").item(0).getTextContent();
					System.out.println("Package : " + packageName);
					
//					String strChanged = eElement.getElementsByTagName("changed").item(0).getTextContent();
//					Boolean changed = (strChanged.equals("true"))? true: false;					
//					System.out.println("Changed : " + changed);
					
					NodeList dependencyName = eElement.getElementsByTagName("dependency");
//					String[] dependencies = new String[dependencyName.getLength()];
					Map<String, Integer> dependencies = new HashMap<String, Integer>();
					System.out.println("Dependency : " + dependencyName.getLength());
					for ( int k = 0; k < dependencyName.getLength(); k++ ) {
						Node dependencyNameNode = dependencyName.item(k);													
						Element dependencyNameElement = (Element) dependencyNameNode;
						String methods = dependencyNameElement.getTextContent();
						int callCount = Integer.parseInt(dependencyNameElement.getAttribute("count"));
						System.out.println("Dependency : " + methods);
						
//						dependencies[k].name = methods;
//						dependencies[k].count = callCount;
						
						dependencies.put(methods, new Integer(callCount));
					}
					
					
					if(contributorColor.get(contributor) == null){
						int R = (int)(Math.random()*256);
						int G = (int)(Math.random()*256);
						int B= (int)(Math.random()*256);
						contributorColor.put(contributor, new Contributor(contributor, new Color(R,G,B)));
					}
										
					if(packageColor.get(packageName) == null){
						int R = (int)(Math.random()*256);
						int G = (int)(Math.random()*256);
						int B= (int)(Math.random()*256);
						packageColor.put(packageName, new FlowerPackage(packageName, new Color(R,G,B)));
					}
					
					Flower flower = new Flower(methodName, contributorColor.get(contributor).color,size,0,0,numMethods, contributor, dependencies);
					flower.changed = false;
					flower.packageName = packageName;
					this.frames[i].flowers[j] = flower;				

					
				}	 
			}
			
			
			setFields();
			
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		  }

	private void setFields() {
		Frame lastFrame = this.frames[this.frames.length-1];
		classNum = lastFrame.flowers.length;
		maxClassLines = 0;
		maxDependenciesNumber = 0;
		double combinedflowerArea = 0;
				
		for(int i = 0; i<lastFrame.flowers.length; i++){
			Flower flower = lastFrame.flowers[i];
			
			maxClassLines = Math.max(maxClassLines, lastFrame.flowers[i].size);
			//maxDependenciesNumber = Math.max(maxDependenciesNumber, lastFrame.flowers[i].dependencies.size());
			if(this.flowers.get(lastFrame.flowers[i].methodName) == null){
				this.flowers.put(lastFrame.flowers[i].methodName, new Flower(lastFrame.flowers[i]));
			}
			
			combinedflowerArea = Math.PI * Math.pow(flower.size * 3, 2);
			
		}
		
		int contributorSize = contributorColor.size();	
		float contributorCount = 0;
		
		for (Map.Entry<String, Contributor> entry : contributorColor.entrySet()) {
		    String contributorName = entry.getKey();
		    Contributor contributor = entry.getValue();
		    
		    contributor.color = new Color(Color.HSBtoRGB(contributorCount/contributorSize, 1.0f, 1.0f));
		    
		    contributorCount += 1;
		}
		
		int packageSize = packageColor.size();	
		float packageCount = 0;
		
		for (Map.Entry<String, FlowerPackage> entry : packageColor.entrySet()) {
		    String contributorName = entry.getKey();
		    FlowerPackage flowerPackage = entry.getValue();
		    
		    flowerPackage.color = new Color(Color.HSBtoRGB(packageCount/packageSize, 1.0f, (float) (Math.random() * (1 - .5) + .5)));
		    
		    packageCount += 1;
		}
		
		//System.out.println("Most dependencies: " + mostDependencies(lastFrame.flowers).methodName);
		
		maxGridX = Visualization.width / (int)Math.ceil(Math.sqrt(classNum));
		maxGridY = Visualization.height / (int) Math.ceil(Math.sqrt(classNum));
		
		setFlowerSize(combinedflowerArea);
		setFlowerPosition();
		String i = "3";
	}
	
	private void setFlowerSize(double combinedArea) {
		
		double ratio;
		double panelArea = ((Visualization.width * Visualization.height)*.9 );
		
		if(combinedArea > panelArea){
			ratio = panelArea/combinedArea;
		}else{
			ratio = combinedArea/panelArea;
		}
		
		double maxFlowerSize = Math.sqrt(panelArea/classNum)/2;
		
		for (int i = 0; i<frames.length; i++){
			Frame frame = frames[i];
			for (int j = 0; j<frame.flowers.length; j++){
				Flower flower = frame.flowers[j];
				
				flower.size = (int) (flower.size*ratio);
				
//				int maxFlowerSize = (int) (Visualization.width * .9) / (classNum);
				double size = ((double)flower.size / (double) maxClassLines) * (maxFlowerSize - maxFlowerSize*0.4) + maxFlowerSize*0.4;
				
//				if (size >= maxFlowerSize) {
//					flower.size = (int) maxFlowerSize;
//				} else {
//					flower.size = (int) size;
//				}
				
				flower.size = (int)(.5 * Math.sqrt(flower.size));
				
				
				
				System.out.println(flower.methodName + "---" + flower.size + "---Ratio---" + maxFlowerSize);
				
				

			}
		}
	}
	
	private void setFlowerPosition(){
		
		Flower[] lastFlowers = frames[frames.length-1].flowers;
		
		if(lastFlowers == null)
			return;
		
		for(int i=0;i<lastFlowers.length;i++){
			Flower frameFlower = lastFlowers[i];
						
			Flower flower = this.flowers.get(frameFlower.methodName);
			flower.size = frameFlower.size;
			
		int x,y;
		do{
			x = (int)(Math.random() * Visualization.width);
			y = (int)(Math.random() * (Visualization.height)); // + legend
			
			//System.out.println("Flower :" + flower.methodName + "---Diameter--" + flower.size + "--RealSize:" + frameFlower.size);
			
		}while(false);
		
		flower.x = x;
		flower.y = y;		
		}
	}

	private Flower mostDependencies (Flower[] flowers){
		int highest = 0;
		int highestIndex = -1;
		
		for(int i = 0; i<flowers.length; i++){
			Flower flower = flowers[i];
			
//			System.out.println("Dependencies length: " + flower.dependencies.length );
			
			if(flower.dependencies.size() > highest){
				highest = flower.dependencies.size();
				highestIndex = i;
			}
		}
		
		if(highestIndex == -1)
			return null;
		
		return flowers[highestIndex];
		
	}
	
	
}
