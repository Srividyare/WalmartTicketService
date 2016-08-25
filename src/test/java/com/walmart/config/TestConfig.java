package com.walmart.config;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConfig {
	Config configObj = new Config();
	
	@Test
	public void test(){
		assertEquals(null, configObj.levelNameArray[0]);
	}

}
