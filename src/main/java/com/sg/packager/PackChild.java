package com.sg.packager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PackChild {

	private String sequence;
	private List<String> oldNames;	
	
	private String getMismatchString(String parentSequence) {
		
		String[] seqs = equalizeSequenceLengths(this.sequence, parentSequence);
		
		char[] arr1 = seqs[0].toCharArray();
		char[] arr2 = seqs[1].toCharArray();
		
		String out = "";

		for(int i = 0; i < arr1.length; i++) {

			// Find a mismatch
			if(arr1[i] != arr2[i]) {
				// Sequences start at 1 instead of 0
				int basePos = i + 1;
				out += out.equals("") ? "" + basePos + arr1[i] : "-" + basePos + arr1[i];
			} 
		}
		return out;
	}

	private String[] equalizeSequenceLengths(String seq1, String seq2) {

		// Remove all 'N' from inputs 
		seq1.replaceAll("N","");
		seq2.replaceAll("N","");

		// No need to pad the inputs
		if(seq1.length() == seq2.length()) {
			return new String[]{seq1, seq2};
		}

		// The output array has on original input and an end-padded version of the short input
		int nPadLength = Math.abs(seq1.length() - seq2.length());		
		if(seq1.length() < seq2.length()) {
			return new String[]{StringUtils.rightPad(seq1, nPadLength, "N"), seq2};
		}else{
			return new String[]{seq1, StringUtils.rightPad(seq2, nPadLength, "N")};
		}
	}

	public void setOldNames(List<String> oldNames) {
		this.oldNames = oldNames;
	}
	public List<String> getOldNames() {
		return this.oldNames;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getSequence() {
		return this.sequence;
	}
}
