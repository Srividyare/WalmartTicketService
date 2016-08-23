package com.walmart.database;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import static org.junit.Assert.assertEquals;


public class TestSeatDB {

	SeatDB seatDbObject = SeatDB.getInstance();
	
	@Test
	public void testSeatMap() {
		assertNotNull(seatDbObject.getSeatMap());
		assertNotNull(seatDbObject.getSeatMap().get(1));
		assertNotNull(seatDbObject.getSeatMap().get(1).get(1));
		assertNull(seatDbObject.getSeatMap().get(0));
	}
	
}
