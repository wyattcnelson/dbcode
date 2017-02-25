package com.sg.packager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import com.sg.packager.Brancher;

public class FileMapper {

	public static final String GROUP_DELIMITER = "@";
	public static final String MEMBER_DELIMITER = "x";

	public static void main(String[] args) {
		
		Map<String, List<String>> redundantSequenceMap = mapRedundantSequences(args[0]);
		Map<String, List<String>> groupMap = mapGroups(redundantSequenceMap, 3);	
		Map<String, List<String>> branchMap = Brancher.branchGroups(groupMap, "A");	

	}

	/**
	 *
	 * Provides a map containing a key for every unique  
	 * sequence in the sequence.fa file
	 * <p>
	 * Each key is a String representation of a unique sequence
	 * <p>
	 * The value of each key is a list of sequence names that  
	 * correspond to the key
	 *
	 * @author wcnelson
	 * @param filePath	String path of sequence.fa
	 *
	 */
	private static Map<String, List<String>> mapRedundantSequences(String filePath){
	
		// FileReader instance created from character files using default encoding
		
		// BufferedReader has methods to read test from a character-input stream
		// Single argument implies default buffer size

		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){

			// Initialize a map to output
			Map<String, List<String>> output = new HashMap<String, List<String>>();

			// Count all headers
			int count = 0;

			// readLine() returns either
			// a String containing the contents of a line
			// or
			// null if the end of the stream has been reached
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				
				String header;
				String sequence;

				if(line.startsWith(">")){
					count++;
					header = line.substring(1);  // chop off the ">" 
					sequence = bufferedReader.readLine();
				}else{
					continue;
				}
					
				// If output doesn't contain this sequence, add it and initialize a list as its value
				if(!output.containsKey(sequence)) {
					output.put(sequence, new ArrayList<String>());
				}

				// If the list does not contain this header, add it
				if(!output.get(sequence).contains(header)) {
					output.get(sequence).add(header);
				}

			}

			// Print some info
			System.out.println("File has " + count + " sequences");
			System.out.println("Found " + output.size() + " unique sequences");
			
			return output;
		} catch (IOException e) {
			System.out.println("IO Exception Error");
			e.printStackTrace();
			return null;
		}	
		//finally not necessary Java 7+
		
	}

	/**
	 *
	 *
	 *
	 * @author wcnelson
	 * @param redundantSequenceMap
	 * @param diffs	number of positions that differ between any two members of a group
	 *
	 */
	private static Map<String, List<String>> mapGroups(Map<String, List<String>> redundantSequenceMap, int diffs) {
		
		// Initialize output map
		Map<String, List<String>> output = new HashMap<String, List<String>>();
		
		// Initialize groupName
		String groupName = "G000000";
		int count = 0;

		// Use hashset to keep track of allocated sequences
		Set<String> spokenFor = new HashSet<String>();

		// Iterate entries in redundantSequenceMap
		for(String sequence : redundantSequenceMap.keySet()) {
		
			if(spokenFor.contains(sequence)) {
				continue;
			}

			String groupKey = groupName + GROUP_DELIMITER + sequence;
			output.put(groupKey, new ArrayList<String>());

			// Test remaining entries against this key
			for(Map.Entry<String, List<String>> entry : redundantSequenceMap.entrySet()) {
				if(spokenFor.contains(entry.getKey())) {
					continue;
				}
				if(passDifferenceTest(diffs, sequence, entry.getKey())){
					Collections.sort(entry.getValue());
					String firstName = entry.getValue().get(0);
					output.get(groupKey).add(firstName + MEMBER_DELIMITER + entry.getKey());
					spokenFor.add(entry.getKey());
				}
			}

			count++;
			groupName = groupName.substring(0,groupName.length() - ("" + count).length()) + count;
		}

		// print some info
		System.out.println("After grouping...");
		System.out.println("# of groups: " + output.size());

		return output;
	}

	/**
	 *
	 *
	 *
	 * @author wcnelson
	 * @param maxDiffs	number of positions that differ between any two members of a group
	 * @param string1
	 * @param string2
	 *
	 */
	private static boolean passDifferenceTest(int maxDiffs, String string1, String string2) {
		
		// Assume the strings have different lengths
		String longString = "";
		String shortString = "";

		if(string1.length() < string2.length()) {
			longString = string2;
			shortString = string1;
		}else{
			longString = string1;
			shortString = string2;
		}

		// initialize diffCount to length difference
		int diffCount = longString.length() - shortString.length();
		for(int i = 0; i < shortString.length(); i++) {
			if(longString.charAt(i) != shortString.charAt(i)) {
				diffCount++;
			}
			if(diffCount > maxDiffs){
				return false;
			}
		}
		return true;
	}
}













