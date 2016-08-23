package com.walmart.database;

public class Seat {
	
	private int availability;
	private int level;
	private int row;
	private int seatNum;
	
	/**
	 * 
	 * @param seatNum
	 * @param level
	 * @param row
	 * @param availability
	 */
	public Seat(int seatNum, int level, int row, int availability){
		this.seatNum = seatNum;
		this.level = level;
		this.row = row;
		this.availability = availability;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public int getLevel() {
		return level;
	}

	public int getRow() {
		return row;
	}

	public int getSeatNum() {
		return seatNum;
	}
	
	

}
