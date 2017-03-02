package com.sg.packager;

import java.util.List;

import com.sg.packager.PackChild;

public class PackParent {

	private String name;
	private String sequence;
	private List<PackChild> children;

	// Constructor
	public PackParent (String name, String sequence, List<PackChild> children) {
		this.name = name;
		this.sequence = sequence;
		this.children = children;
	}

	public void setChildren(List<PackChild> children) {
		this.children = children;
	}
	public List<PackChild> getChildren() {
		return this.children;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}	

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getSequence() {
		return this.sequence;
	}
}
