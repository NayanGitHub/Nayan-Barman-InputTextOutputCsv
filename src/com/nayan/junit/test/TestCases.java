package com.nayan.junit.test;

import org.junit.Test;

import com.nayan.main.InputTxtOutputCsv;


public class TestCases {

	@Test
	public void test() {
		InputTxtOutputCsv mainObj= new InputTxtOutputCsv(); 
		mainObj.getOutputCsv();
	}	
}
