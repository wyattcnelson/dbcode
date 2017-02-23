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
	private static Map<String, List<String>> branchGroups(Map<String, List<String>> groupMap, String ID) {
		
		Map<String, List<String>> output = new HashMap<String, List<String>>();
		
		String branch1Name;
		String branch2Name;

		for(Map.Entry<String, List<String>> entry : groupMap.entrySet()) {
			
			String[] groupKey = entry.getKey().split(FileMapper.GROUP_DELIMITER);
			String groupName = groupKey[0];
			String groupSequence = groupKey[1];

			// Initialize branch points array
			int[] branchPoints = new int[groupSequence.length()];
			Arrays.fill(branchPoints, 0);
			
			// Accumulate branch points
			for(String item : entry.getValue()) {
				String[] member = item.split(FileMapper.MEMBER_DELIMITER);
				String memberFirstName = member[0];
				String memberSequence = member[1];

				int stop = groupSequence.length() < memberSequence.length() ? groupSequence.length() : memberSequence.length();
				for(int i = 0; i <= stop; i++) {
					if(groupSequence.charAt(i) != memberSequence.charAt(i)) {
						branchPoints[i]++;
					}
				}
			}

			// Identify the most common branch point
			int max = 0;
			int index = 0;
			for (int i = 0; i < branchPoints.length; i++) {
				if(branchPoints[i] > max) {
					max = branchPoints[i];
					index = i;
				}
			}

			// Assign branch1Key
			String branch1Key = ID + "1" + groupName + FileMapper.GROUP_DELIMITER + groupSequence;

			// No branches
			if(max == 0) {
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
				// To stay in branch1, the member must be the same length as the group sequence
				// and match the char at the branch point
				if(groupSequence.length() == memberSequence.length() && groupSequence.charAt(index) == memberSequence.charAt(index)){
					output.get(branch1Key).add(item);
				}else{
					output.get(branch2Key).add(item);
				}
			}
		}
		return output;
	}
}
