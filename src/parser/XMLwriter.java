package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

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

public class XMLwriter {
	
	public static void GenerateXML(ArrayList<FlowerObject> flor, int date, int frameNum)
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
				attr.setValue(""+frameNum);
				frame.setAttributeNode(attr);
				
				Element time = doc.createElement("time");
				time.appendChild(doc.createTextNode(""+date));
				frame.appendChild(time);
				
				for(int i = 0; i<flor.size();i++)
				{
					FlowerObject temp = flor.get(i);
					Element flower = doc.createElement("flower");
					
					Element name = doc.createElement("name");
					name.appendChild(doc.createTextNode(temp.getName()));
					flower.appendChild(name);
					
					Element packages = doc.createElement("package");
					packages.appendChild(doc.createTextNode(temp.getPackname()));
					flower.appendChild(packages);
					
					Element size = doc.createElement("size");
					size.appendChild(doc.createTextNode(""+temp.getLineNumber()));
					flower.appendChild(size);
					
					Element x = doc.createElement("x");
					x.appendChild(doc.createTextNode(""));
					flower.appendChild(x);
					
					Element y = doc.createElement("y");
					y.appendChild(doc.createTextNode(""));
					flower.appendChild(y);
					
					Element committer = doc.createElement("committer");
					committer.appendChild(doc.createTextNode(""+temp.getLastCommitter()));
					flower.appendChild(committer);
					
					Element age = doc.createElement("age");
					age.appendChild(doc.createTextNode(""+temp.getAge()));
					flower.appendChild(age);
					
					Element numMethods = doc.createElement("numMethods");
					numMethods.appendChild(doc.createTextNode(""+temp.getMethodNumber()));
					flower.appendChild(numMethods);
					
					Element dependency = doc.createElement("dependencies");
					for(Map.Entry<String, Integer> entry : temp.getImportClasses().entrySet())
					{
						String tempvalue = entry.getKey();
						if(tempvalue=="MethodNumber")
						{
							continue;
						}
						
						Element dep = doc.createElement("dependency");
						dep.appendChild(doc.createTextNode(tempvalue));
						
						
						Attr tempattr = doc.createAttribute("count");
						tempattr.setValue(entry.getValue()+"");
						dep.setAttributeNode(tempattr);
						
						dependency.appendChild(dep);
					}
					flower.appendChild(dependency);
					
					frame.appendChild(flower);
				}
				
		 
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("frames/frame" + frameNum +".xml"));
		 

		 
				transformer.transform(source, result);
		 
				System.out.println("File saved!");
		 
			  } catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			  } catch (TransformerException tfe) {
				tfe.printStackTrace();
			  }
			}
	
}
