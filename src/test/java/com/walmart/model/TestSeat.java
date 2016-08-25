package com.walmart.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestSeat {

	Seat seatObj = new Seat(10,3,1,0, 100);

	@Test
	public void testAvailability() {
		assertEquals(0, seatObj.getAvailability());
		seatObj.setAvailability(2);
		assertEquals(2, seatObj.getAvailability());
	}
	public void testLevel() {
		assertEquals(3, seatObj.getLevel());
	}
	public void testRow() {
		assertEquals(1, seatObj.getRow());
	}
	public void testSeatNum() {
		assertEquals(10, seatObj.getSeatNum());
	}
	public void testPrice() {
		assertEquals(100, seatObj.getPrice());
	}
}
