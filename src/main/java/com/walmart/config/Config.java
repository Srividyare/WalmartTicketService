package com.walmart.config;

public class Config {

	public  int[] priceArray = new int[5];
	public  String[] levelNameArray = new String[5];
	public  int[] rowArray = new int[5];
	public  int[] seatRowArray = new int[5];
	public static int heldSeatsExpiryTime = 10000; // in ms

	/**
	 * Constructor sets the values as given below
	 */
	public Config(){ // constructor

		levelNameArray[1] = "Orchestra";
		priceArray[1] = 100;
		rowArray[1] = 25;
		seatRowArray[1] = 50;

		levelNameArray[2] = "Main";
		priceArray[2] = 75;
		rowArray[2] = 20;
		seatRowArray[2] = 100;

		levelNameArray[3] = "Balcony1";
		priceArray[3] = 50;
		rowArray[3] = 15;
		seatRowArray[3] = 100;

		levelNameArray[4] = "Balcony2";
		priceArray[4] = 40;
		rowArray[4] = 15;
		seatRowArray[4] = 100;
	}
}
