package com.walmart.database;

import java.util.ArrayList;
import java.util.List;

public class SeatHold {

	private int seatHoldId;
	private String customerEmail;
	private List<Seat> heldSeat = new ArrayList<Seat>();
	private long time;
	/**
	 * Constructor that initializes the class variables to the values passed
	 * @param seatHoldId unique identifier generated for a set of seats held by a customer
	 * @param customerEmail unique identifier for the customer
	 * @param heldSeat list of seats which are held
	 * @param time time stamp at which seats are held
	 */
	public SeatHold(int seatHoldId, String customerEmail, List<Seat> heldSeat, long time) {

		this.seatHoldId = seatHoldId;
		this.customerEmail = customerEmail;
		this.heldSeat = heldSeat;
		this.time = time;
	}
	/**
	 * Returns the SeatHoldId of the seats held
	 * @return seatHoldId
	 */
	public int getSeatHoldId() {
		return seatHoldId;
	}
	/**
	 * Sets the seatHoldId to the value passed as parameter
	 * @param seatHoldId unique identifier generated for a set of seats held by a customer
	 */
	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}
	/**
	 * Returns the customerEmail of the seats held
	 * @return customerEmail
	 */
	public String getCustomerEmail() {
		return customerEmail;
	}
	/**
	 * Sets the customerEmail to the value passed as parameter
	 * @param customerEmail unique identifier for the customer
	 */
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	/**
	 * Returns a list of seats that are held
	 * @return heldSeat
	 */
	public List<Seat> getHeldSeat() {
		return heldSeat;
	}
	/**
	 * Sets the list of seats to the value passed as parameter 
	 * @param heldSeat list of seats which are held
	 */
	public void setHeldSeat(List<Seat> heldSeat) {
		this.heldSeat = heldSeat;
	}
	/**
	 * Returns a time stamp at which the seats are held
	 * @return time 
	 */
	public long getTime() {
		return time;
	}
	/**
	 * Sets the time passed as parameter
	 * @param time time stamp at which seats are held
	 */
	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return new String();

	}


}
