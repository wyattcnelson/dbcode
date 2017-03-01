package com.sg.packager;

import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WriteXml {

	public static void main(String[] args) {
		System.out.println("compile check java.text...");
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
		System.out.println(dateFormat.format(date));
//		System.out.println("compile check javax.xml...");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//		System.out.println("compile check org.w3c...");
//		Document d = new Document();
	}
}
