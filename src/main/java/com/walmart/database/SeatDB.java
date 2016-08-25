package com.walmart.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.walmart.config.Config;
import com.walmart.model.Seat;
import com.walmart.model.SeatHold;

public class SeatDB {

	private Map<Integer, HashMap<Integer, ArrayList<Seat>>> seatMap = 
			new HashMap<Integer, HashMap<Integer, ArrayList<Seat>>>();
	private static SeatDB instance = null;

	private Map<String, SeatHold> seatHoldMap = new ConcurrentHashMap<String, SeatHold>();
	/**
	 * Constructor which in turn calls the fillLevel method when an object of the class is created
	 */
	private SeatDB() {
		fillLevel();
	}
	/**
	 * Calls the fillSeats method by passing the level, row, and total number of seats in a row
	 */
	public void fillLevel() {	
		Config configObj = new Config();

		for(int i=1;i<=4;i++){
			fillSeats(i, configObj.rowArray[i], configObj.seatRowArray[i], configObj.priceArray[i]);
		}
	}
	/**
	 * Initializes each seat in each row and in each level
	 * @param level level value that is passed
	 * @param row number of rows to be filled in each level
	 * @param numSeats number of seats to be filled in each level
	 * @param price price of each seat in the level
	 */
	private void fillSeats(int level, int row, int numSeats, int price) {

		HashMap<Integer, ArrayList<Seat>> rowMap = new HashMap<Integer, ArrayList<Seat>>();

		for(int i = 1; i <= row; i++){
			ArrayList<Seat> seatList = new ArrayList<Seat>();
			for(int j = 1; j <= numSeats; j++){
				seatList.add(new Seat(j,level, i, 0, price));
			}
			rowMap.put(i, seatList);
		}
		seatMap.put(level, rowMap);
	}
	/**
	 * Generates the object of the class
	 * @return instance
	 */
	public static SeatDB getInstance(){
		if (instance == null) {
			instance = new SeatDB();
		} 
		return instance;
	}
	/**
	 * Returns the map of all seats
	 * @return seatMap
	 */
	public Map<Integer, HashMap<Integer, ArrayList<Seat>>> getSeatMap() {
		return seatMap;
	}
	/**
	 * Returns the map of seats that are held
	 * @return seatHoldMap
	 */
	public Map<String, SeatHold> getSeatHoldMap() {
		return seatHoldMap;
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		SeatDB ins = SeatDB.getInstance();
		for(Entry<Integer, HashMap<Integer, ArrayList<Seat>>> e : ins.seatMap.entrySet()){
			System.out.println(e.getKey());
			for(Entry<Integer, ArrayList<Seat>> e1 : e.getValue().entrySet()){
				System.out.println(e1.getKey());
				System.out.println(e1.getValue());
			}
		}
	}
}
