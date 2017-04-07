package com.sg.packager;

import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Nester {

	public static Element namesToNestedElements(Document doc, Element parent, List<String> list, String tag, String delimiter) {
	
		// Load all names into nested Child objects
		Nester.Child root = new Nester.Child(-1, "root", new ArrayList<Nester.Child>());
		for (String name : list) {
			String[] fields = name.split(delimiter);
			int level = 0;
			insertChildren(root, fields, level);
		}

		// Add child elements
		growChildElements(doc, parent, root, tag);

		return parent;
	}

	private static void growChildElements(Document doc, Element parent, Nester.Child root, String tag) {	
		
		if (root.getChildren().isEmpty()) {
			return;
		}
		
		// Add nested child elements to parent element
		for (Nester.Child child : root.getChildren()) {
			Element element = doc.createElement(tag);
			element.setAttribute("id","" +  child.getLevel());
			element.setAttribute("name", "" + child.getName());
			parent.appendChild(element);
			// Recursive call
			growChildElements(doc, element, child, tag);
		}
	}
	
	private static void insertChildren(Nester.Child parent, String[] fields, int level) {
		if (level < fields.length) {
			String name = fields[level];
			// Check the parent's children for this name
			for (Nester.Child child : parent.getChildren()) {
				if (child.getName().equals(name)) {
					insertChildren(child, fields, level + 1);
				}
			}
			// If the child with this name is not found, create it
			Nester.Child newChild = new Nester.Child(level, name, new ArrayList<Nester.Child>());
			insertChildren(newChild, fields, level + 1);
			parent.getChildren().add(newChild);
		}
	}

	public static class Child {
		
		private int level;
		private String name;
		private List<Child> children;

			public Child(int level, String name, List<Child> children) {
				this.level = level;
				this.name = name;
				this.children = children;
			}

		public int getLevel() {
			return this.level;
		}  
		public void setLevel(int level) {
			this.level = level;
		}

		public String getName() {
			return this.name;
		}
		public void setName(String name) {
			this.name = name;
		}

		public List<Child> getChildren() {
			return this.children;
		}
		public void setChildren(List<Child> children) {
			this.children = children;
		}
	}
}
