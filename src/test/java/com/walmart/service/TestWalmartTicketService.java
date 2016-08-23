package com.walmart.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.walmart.database.Seat;
import com.walmart.database.SeatHold;

import sun.reflect.annotation.ExceptionProxy;

public class TestWalmartTicketService {

	WalmartTicketService walmartTS = WalmartTicketService.getInstance();
	
	@Test
	public void testTotalNumSeatsAvaialable(){
		assertEquals(1250,walmartTS.numSeatsAvailable(Optional.of(1)));		
	}
	
	@Test
	public void testTotalNumSeatsAvaialableWithNull(){
		assertEquals(6250,walmartTS.numSeatsAvailable(Optional.ofNullable(null)));		
	}
	
	@Test
	public void testFindAndholdSeats(){
		int level = 1;
		SeatHold actualSeatHold = walmartTS.findAndHoldSeats(10, Optional.of(level), Optional.of(level), "abc@abc.com");
		List<Seat> heldSeats = new ArrayList<Seat>();
		Seat seat1 = new Seat(1, 1, 1, 0);
		heldSeats.add(seat1);
		SeatHold expectedSeatHold = new SeatHold("abc@abc.com".hashCode(), "abc@abc.com", heldSeats, System.currentTimeMillis());
		// check for seat hold properties
		assertEquals(expectedSeatHold.getCustomerEmail(), actualSeatHold.getCustomerEmail());
		assertEquals(expectedSeatHold.getSeatHoldId(), actualSeatHold.getSeatHoldId());
		// test if the seat is book in the level requested
		assertEquals(expectedSeatHold.getHeldSeat().get(0).getLevel(), actualSeatHold.getHeldSeat().get(0).getLevel());
		assertEquals(level, actualSeatHold.getHeldSeat().get(0).getLevel());
		// test if the available seats decreased after holding seats
		assertEquals(1240,walmartTS.numSeatsAvailable(Optional.of(1)));
		// test if seats are marked as held (1)
		for(int i = 0; i < actualSeatHold.getHeldSeat().size(); i++) {
			assertEquals(1, actualSeatHold.getHeldSeat().get(i).getAvailability());
		}
	}
	
	@Test
	public void testReservation() {
		// We will hold new seats here
		SeatHold held = walmartTS.findAndHoldSeats(5, Optional.of(1), Optional.of(4), "newabc@abc.com");
		walmartTS.reserveSeats(held.getSeatHoldId(), "newabc@abc.com");
		for (int i = 0; i < held.getHeldSeat().size(); i++) {
			assertEquals(2, held.getHeldSeat().get(i).getAvailability());
		}
		assertEquals(6235, walmartTS.numSeatsAvailable(Optional.ofNullable(null)));
	}
}
