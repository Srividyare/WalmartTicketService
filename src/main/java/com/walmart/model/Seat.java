package com.walmart.model;

public class Seat {

	private int availability;
	private int level;
	private int row;
	private int seatNum;
	private int price;

	/**
	 * Constructor that initializes the class variables to the values passed
	 * @param seatNum number assigned to each seat in a row
	 * @param level the level a particular seat is in
	 * @param row the row a particular seat is in 
	 * @param availability determines the availability status of a seat, 0 for available, 1 for held, 2 for reserved
	 */
	public Seat(int seatNum, int level, int row, int availability, int price){
		this.seatNum = seatNum;
		this.level = level;
		this.row = row;
		this.availability = availability;
		this.price = price;
	}
	/**
	 * Returns the availability status of a seat
	 * @return availability
	 */
	public int getAvailability() {
		return availability;
	}
	/**
	 * Sets the availability to the value passed
	 * @param availability determines the availability status of a seat, 0 for available, 1 for held, 2 for reserved
	 */
	public void setAvailability(int availability) {
		this.availability = availability;
	}
	/**
	 * Returns the level the seat is in 
	 * @return level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * Returns the row the seat is in 
	 * @return row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * Returns the seat number assigned to each seat in a row 
	 * @return seatNum
	 */
	public int getSeatNum() {
		return seatNum;
	}
	
	/**
	 * Returns the price of the seat
	 * @return price
	 */
	public int getPrice() {
		return price;
	}
}
