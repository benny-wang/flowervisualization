package frameaggregator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FrameAggregator {
	Document doc;
	Element rootElement;
	public FrameAggregator () throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		doc.createElement("Visualization");
		rootElement = doc.createElement("Visualization");
		doc.appendChild(rootElement);
	}
	
	public void aggregateToRoot(String fileName) throws ParserConfigurationException, SAXException, IOException{
		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document docParse = dBuilder.parse(fXmlFile);
		docParse.getDocumentElement().normalize();
		NodeList frameList = docParse.getElementsByTagName("frame");
		
		for (int i = 0; i < frameList.getLength(); i++) {
			Node importedNode = doc.importNode(frameList.item(i), true);
			rootElement.appendChild((Element) importedNode);
		}

	}
	
	public void endAggregate(String path) throws IOException, TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path));
		transformer.transform(source, result);
	}
	
}
