package com.walmart.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;

import com.walmart.config.Config;
import com.walmart.model.Seat;
import com.walmart.model.SeatHold;

public class TestWalmartTicketService {

	WalmartTicketService walmartTS = WalmartTicketService.getInstance();
	Integer none = null;
	
	@After
	public void runAfterTest() {
		walmartTS.makeAllSeatsAvailable();
		Config.heldSeatsExpiryTime = 60000;
	}
	
	@Test
	public void testTotalNumSeatsAvaialable(){
		assertEquals(1250,walmartTS.numSeatsAvailable(Optional.of(1)));	
		assertEquals(6250,walmartTS.numSeatsAvailable(Optional.ofNullable(none)));	
		//assertEquals(6250,walmartTS.numSeatsAvailable(Optional.of(8)));
	}
	
	@Test
	public void testTotalNumSeatsAvaialableWithNull(){
		assertEquals(6250,walmartTS.numSeatsAvailable(Optional.ofNullable(none)));		
	}
	
	@Test
	public void testFindAndholdSeats() throws Exception{
		int level = 1;
		SeatHold actualSeatHold = walmartTS.findAndHoldSeats(10, Optional.of(level), Optional.of(level), "abc1@abc.com");
		List<Seat> heldSeats = new ArrayList<Seat>();
		Seat seat1 = new Seat(1, 1, 1, 0, 100);
		heldSeats.add(seat1);
		SeatHold expectedSeatHold = new SeatHold("abc1@abc.com".hashCode(), "abc1@abc.com", heldSeats, System.currentTimeMillis());
		// check for seat hold properties
		assertEquals(expectedSeatHold.getCustomerEmail(), actualSeatHold.getCustomerEmail());
		assertEquals(expectedSeatHold.getSeatHoldId(), actualSeatHold.getSeatHoldId());
		// test if the seat is booked in the level requested
		assertEquals(expectedSeatHold.getHeldSeat().get(0).getLevel(), actualSeatHold.getHeldSeat().get(0).getLevel());
		assertEquals(level, actualSeatHold.getHeldSeat().get(0).getLevel());
		// test if the available seats decreased after holding seats
		assertEquals(1240,walmartTS.numSeatsAvailable(Optional.of(1)));
		// test if seats are marked as held (1)
		for(int i = 0; i < actualSeatHold.getHeldSeat().size(); i++) {
			assertEquals(1, actualSeatHold.getHeldSeat().get(i).getAvailability());
		}
		//test if the seats requested are more than seats available
		
		//test if max level passed is less than min level
	//	assertNull(null,walmartTS.findAndHoldSeats(10, Optional.of(4), Optional.of(1), "abc@abc.com"));
		
	}
	
	@Test(expected = Exception.class)
	public void testHoldSeatsBadConditions() throws Exception   {
		walmartTS.findAndHoldSeats(100000, Optional.of(1), Optional.of(4), "abc@abc.com");
		walmartTS.findAndHoldSeats(10, Optional.of(4), Optional.of(1), "abc@abc.com");
	}
	
	@Test
	public void testReservation() throws Exception {
		// We will hold new seats here
		SeatHold held = walmartTS.findAndHoldSeats(5, Optional.of(1), Optional.of(4), "newabc@abc.com");
		walmartTS.reserveSeats(held.getSeatHoldId(), "newabc@abc.com");
		for (int i = 0; i < held.getHeldSeat().size(); i++) {
			assertEquals(2, held.getHeldSeat().get(i).getAvailability());
		}
		assertEquals(6245, walmartTS.numSeatsAvailable(Optional.ofNullable(none)));
	}
	
	@Test
	public void testSeatHoldExpiry() throws Exception {
		SeatHold held = walmartTS.findAndHoldSeats(5, Optional.of(1), Optional.of(4), "newabc@abc.com");
		Config.heldSeatsExpiryTime = 50;
		Thread.sleep(2000);
		assertEquals(6250, walmartTS.numSeatsAvailable(Optional.ofNullable(none)));
	}
}
