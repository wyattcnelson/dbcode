package com.sg.packager;

import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sg.packager.PackParent;
import com.sg.packager.PackChild;

public class WriteXml {

	public static final String PROJECT_TAG = "project";
	public static final String RULESET_TAG = "ruleset";
	public static final String TARGET_TAG = "target";
	public static final String PARENT_TAG = "parent";
	public static final String CHILD_TAG = "child";
	public static final String FIELD_TAG = "field";

	public static final String NAME_DELIMITER = ":";
	
	public static void writeXmlRuleset(Map<String, List<PackParent>> map, File file) {
	
		// Create a new doc
		Document doc = createDocument();

		// Add root element <project> to xml and timestamp it
		String projectId = "Project-ID";
		Element projectElement = doc.createElement(PROJECT_TAG);
		projectElement.setAttribute("id", projectId);
		projectElement.setAttribute("timestamp", getTimeStamp());
		doc.appendChild(projectElement);
		
		// Append the ruleset
		String rulesetId = "IMGT-325";
		Element rulesetElement = doc.createElement(RULESET_TAG);
		rulesetElement.setAttribute("id", rulesetId);
		projectElement.appendChild(rulesetElement);

		// Add map to ruleset element;
		appendMap(doc, rulesetElement, map);		

		// Save doc to file
		writeDocToFile(doc, file);
	}

	private static void writeDocToFile(Document doc, File file) {
	
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// Configure output
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");			
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");			

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);		
			transformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void appendMap(Document doc, Element rulesetElement, Map<String, List<PackParent>> map) {

		for(Map.Entry<String, List<PackParent>> entry : map.entrySet()) {

			System.out.println("Mapping: " + entry.getKey());
			Element targetElement = doc.createElement(TARGET_TAG);
			targetElement.setAttribute("id", entry.getKey());
			rulesetElement.appendChild(targetElement);

			for(PackParent pParent : entry.getValue()) {
				Element parentElement = doc.createElement(PARENT_TAG);
				parentElement.setAttribute("id", pParent.getName());
				parentElement.setAttribute("sequence", pParent.getSequence());
				targetElement.appendChild(parentElement);

				for(PackChild pChild : pParent.getChildren()) {
					Element childElement = doc.createElement(CHILD_TAG);
					childElement.setAttribute("id", pChild.getMismatchString(pChild.getSequence(), pParent.getSequence()));
					childElement.setAttribute("num-old-names", pChild.getOldNames().size() + "");

					// Add nested elements for fields 1 to n 
					// e.g.,  <field1><field2>...<fieldn></fieldn></field2></field1>
					childElement = Nester.namesToNestedElements(doc, childElement, pChild.getOldNames(), FIELD_TAG, NAME_DELIMITER);
					parentElement.appendChild(childElement);
				}
			}
		}	
	}

	private static Document createDocument() {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			return docBuilder.newDocument();
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to create document");
		}
	}

	private static String getTimeStamp() {	
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
		return dateFormat.format(date);
	}
}
