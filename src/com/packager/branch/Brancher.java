package com.packager.branch;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.packager.util.FileMapper;

public class Brancher {

	/**
	 * Subdivides groups based on similarity in mismatch positions
	 *
	 * @author wnelson
	 * @param groupMap
	 * @param ID
	 *
	 */
	public static Map<String, List<String>> branchGroups(Map<String, List<String>> groupMap, String ID) {
		
		Map<String, List<String>> output = new HashMap<String, List<String>>();
		
		for(Map.Entry<String, List<String>> entry : groupMap.entrySet()) {
			
			String[] groupKey = entry.getKey().split(FileMapper.GROUP_DELIMITER);
			String groupName = groupKey[0];
			String groupSequence = groupKey[1];
			
			// Find branch point index
			int index = findBranchPoint(groupSequence, entry.getValue());

			// Assign branch1Key
			String branch1Key = ID + "1" + groupName + FileMapper.GROUP_DELIMITER + groupSequence;

			// No branches
			if(index == -1) {
				output.put(branch1Key, entry.getValue());
				continue;
			}

			// Identify lexicographic min memberFirstName of
			// a sequence having the most common branch point
			String branch2FirstName = "";
			String branch2GroupSequence = "";
			for(String item : entry.getValue()) {
				String[] member = item.split(FileMapper.MEMBER_DELIMITER);
				String memberFirstName = member[0];
				String memberSequence = member[1];
				if(index >= memberSequence.length()) {
					continue;
				}
				if(groupSequence.charAt(index) != memberSequence.charAt(index)){
					if(branch2FirstName.equals("") || branch2FirstName.compareTo(memberFirstName) > 0) {
						branch2FirstName = memberFirstName;
						branch2GroupSequence = memberSequence;
					}
				}
			}

			// Assign branch2Key
			String branch2Key = ID + "2" + groupName + FileMapper.GROUP_DELIMITER + branch2GroupSequence;
	
			// Divide entries by branchPoint
			output.put(branch1Key, new ArrayList<String>());
			output.put(branch2Key, new ArrayList<String>());
			for(String item : entry.getValue()) {
				String[] member = item.split(FileMapper.MEMBER_DELIMITER);
				String memberFirstName = member[0];
				String memberSequence = member[1];
				Brancher.Member m = new Brancher.Member(item, FileMapper.MEMBER_DELIMITER);
				// To stay in branch1, the member must be the same length as the group sequence
				// and match the char at the branch point
				if(groupSequence.length() == m.getLength() && groupSequence.charAt(index) == m.getBase(index)){
					output.get(branch1Key).add(item);
				}else{
					output.get(branch2Key).add(item);
				}
			}
		}

		// Print some info
		System.out.println("After branching...");
		System.out.println("# of groups: " + output.size());

		return output;
	}

	/**
	 * Check mismatches between entry and group sequence to find
	 * index of max mismatches, becomes branch point
	 *
	 * @author wnelson
	 * @param groupSequence
	 * @param list
	 *
	 */
	private static int findBranchPoint(String groupSequence, List<String> list) {
		
		// Declare and intialize to 0 an array to store counts
		int[] branchPoints = new int[groupSequence.length()];
		Arrays.fill(branchPoints, 0);
		
		// For each sequence in the list
		for (String item : list) {
			Brancher.Member m = new Brancher.Member(item, FileMapper.MEMBER_DELIMITER);
			
			// Test all positions for mismatch against the group sequence
			int stop = groupSequence.length() < m.getLength()
				? groupSequence.length()
				: m.getLength();
			for(int i = 0; i < stop; i++) {
				if(groupSequence.charAt(i) != m.getBase(i)) {
					branchPoints[i]++;	
				}
			}
		}
		
		// 
		return indexOfMax(branchPoints);
	}

	/**
	 * Return index of max value in an int array
	 * return -1 if all are equal to 0
	 *
	 * @author wnelson
	 * @param array
	 *
	 */
	private static int indexOfMax(int[] array) {
		int max = 0;
		int index = 0;
		for(int i = 0; i < array.length; i++) {
			if(array[i] > max) {
				max = array[i];
				index = i;
			}
		}
		return (max == 0) ? -1 : index;
	}

	static class Member {
		
		private String name;
		private String sequence;

		public Member(String item, String delimiter) {
			this.name = item.split(delimiter)[0];
			this.sequence = item.split(delimiter)[1];
		}
		
		public String getName() {
			return this.name;
		}
		public String getSequence() {
			return this.sequence;
		}
		public int getLength() {
			return this.sequence.length();
		}
		public char getBase(int i) {
			return this.sequence.charAt(i);
		}
	}
}
