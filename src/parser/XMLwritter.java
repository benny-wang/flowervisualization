package parser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLwritter {
	public static void GenerateXML(ArrayList<FlowerObject> flor)
	{
		 try {
			 
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		 

				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("Visualization");
				doc.appendChild(rootElement);
		 

				Element frame = doc.createElement("frame");
				rootElement.appendChild(frame);
		 

				Attr attr = doc.createAttribute("id");
				attr.setValue("0");
				frame.setAttributeNode(attr);
				
				Element time = doc.createElement("time");
				frame.appendChild(time);
				
				for(int i = 0; i<flor.size();i++)
				{
					FlowerObject temp = flor.get(i);
					Element flower = doc.createElement("flower");
					
					
					Attr floId = doc.createAttribute("id");
					floId.setValue(""+i);
					flower.setAttributeNode(floId);
					
					Element name = doc.createElement("name");
					name.appendChild(doc.createTextNode(temp.getName()));
					flower.appendChild(name);
					
					Element size = doc.createElement("size");
					size.appendChild(doc.createTextNode(""+temp.getLineNumber()));
					flower.appendChild(size);
					
					Element x = doc.createElement("x");
					x.appendChild(doc.createTextNode(""));
					flower.appendChild(x);
					
					Element y = doc.createElement("y");
					y.appendChild(doc.createTextNode(""));
					flower.appendChild(y);
					
					Element numMethods = doc.createElement("numMethods");
					numMethods.appendChild(doc.createTextNode(""+temp.getMethodNumber()));
					flower.appendChild(numMethods);
					
					Element dependency = doc.createElement("dependency");
					flower.appendChild(dependency);
					
					frame.appendChild(flower);
				}
				
		 
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("result.xml"));
		 

		 
				transformer.transform(source, result);
		 
				System.out.println("File saved!");
		 
			  } catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			  } catch (TransformerException tfe) {
				tfe.printStackTrace();
			  }
			}
	
}
