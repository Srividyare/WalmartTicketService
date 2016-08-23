package com.walmart.database;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestSeatHold {

	static List<Seat> seatList = new ArrayList<Seat>();
	static SeatHold seatHoldObject;
	@BeforeClass
	public static void before() {
		for (int i = 0; i< 3; i++) {
			Seat seat = new Seat (i, 1,1,0);
			seatList.add(seat);
		}
		seatHoldObject = new SeatHold(0, "walmart@walmart.com", seatList, (long) 1200000000);
	}

	@Test
	public void testSeatHoldId() {
		assertEquals(0, seatHoldObject.getSeatHoldId());
		seatHoldObject.setSeatHoldId(123);
		assertEquals(123, seatHoldObject.getSeatHoldId());
	}
	public void testCustomerEmail() {
		assertEquals("walmart@walmart.com", seatHoldObject.getCustomerEmail());
		seatHoldObject.setCustomerEmail("abc.com");
		assertEquals("abc.com", seatHoldObject.getCustomerEmail());
	}
	public void testHeldSeat() {
		assertEquals(seatList, seatHoldObject.getHeldSeat());
	}
	public void testTime() {
		assertEquals(1200000000, seatHoldObject.getTime());
		seatHoldObject.setTime(123);
		assertEquals(123, seatHoldObject.getTime());
	}
}
