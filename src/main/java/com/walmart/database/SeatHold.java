package com.walmart.database;

import java.util.ArrayList;
import java.util.List;

public class SeatHold {
	
	
	private int seatHoldId;
	private String customerEmail;
	private List<Seat> heldSeat = new ArrayList<Seat>();
	private long time;
	
	public SeatHold(int seatHoldId, String customerEmail, List<Seat> heldSeat, long time) {
		
		this.seatHoldId = seatHoldId;
		this.customerEmail = customerEmail;
		this.heldSeat = heldSeat;
		this.time = time;
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public List<Seat> getHeldSeat() {
		return heldSeat;
	}

	public void setHeldSeat(List<Seat> heldSeat) {
		this.heldSeat = heldSeat;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return new String();
		
	}
	

}
