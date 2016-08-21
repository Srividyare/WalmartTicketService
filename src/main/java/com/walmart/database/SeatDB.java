package com.walmart.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SeatDB {


	private HashMap<Integer, HashMap<Integer, ArrayList<Seat>>> seatMap = new HashMap<Integer, HashMap<Integer, ArrayList<Seat>>>();
	private static SeatDB instance = null;
	
	private HashMap<String, SeatHold> seatHoldMap = new HashMap<String, SeatHold>();
	private SeatDB() {
		fillLevel();
	}
	private void fillLevel() {		
		fillSeats(1, 25, 50);
		fillSeats(2, 20, 100);
		fillSeats(3, 15, 100);
		fillSeats(4, 15, 100);		
	}
	private void fillSeats(int level, int row, int numSeats) {
		
			HashMap<Integer, ArrayList<Seat>> rowMap = new HashMap<Integer, ArrayList<Seat>>();

			for(int i = 1; i <= row; i++){
				ArrayList<Seat> seatList = new ArrayList<Seat>();
				for(int j = 1; j <= numSeats; j++){
					seatList.add(new Seat(j,level, i, 0));
				}
				rowMap.put(i, seatList);
			}
			seatMap.put(level, rowMap);

	}
	public static SeatDB getInstance(){
		if (instance == null) {
			instance = new SeatDB();
		} 
		return instance;
	}

	public HashMap<Integer, HashMap<Integer, ArrayList<Seat>>> getSeatMap() {
		return seatMap;
	}
	public HashMap<String, SeatHold> getSeatHoldMap() {
		return seatHoldMap;
	}
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
