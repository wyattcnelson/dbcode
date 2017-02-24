package com.packager;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.packager.Brancher;

public class TestBrancher {

	// Test function of function indexOfMax(int[])
	@Test
	public void TestIndexOfMax() {
		
		System.out.println("\n\nTesting: Brancher.indexOfMax()");
		
		// Test array, max of 4354 exists in 20th position
		int[] arr = new int[]{0,0,5,3,42,234,34,34,2,34,2,4,32,4,323,6,76,78,23,46,4354,2344,243,3,23,4,234,44,0,0};
		assertEquals(20, Brancher.indexOfMax(arr));
	}

}
