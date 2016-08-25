package com.walmart.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.After;
import org.junit.Test;

import com.walmart.model.SeatHold;

public class WalmartServiceLoadTest implements Runnable {

	WalmartTicketService walmartTS = WalmartTicketService.getInstance();
	private int numThreads = 50;
	private int timeoutInMs = 1000;
	private int numSeatsToHold = 10;
	static Set<String> reservationCode = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	Integer none = null;
	@After
	public void runAfterTest() {
		walmartTS.makeAllSeatsAvailable();
	}
	
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		SeatHold held;
		try {
			held = walmartTS.findAndHoldSeats(numSeatsToHold, Optional.of(1), Optional.of(4), "abc"+Thread.currentThread().getId()+"@abc.com");
		
	//	System.out.println("Time to hold seats for thread " + Thread.currentThread().getId() + " is " + (System.currentTimeMillis() - startTime) + " ms");
		startTime = System.currentTimeMillis();
		String code = walmartTS.reserveSeats(held.getSeatHoldId(), "abc"+Thread.currentThread().getId()+"@abc.com");
	//	System.out.println("Time to reserve seats for thread " + Thread.currentThread().getId() + " is " + (System.currentTimeMillis() - startTime) + " ms");
		reservationCode.add(code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void loadTest() {
		for (int i = 0; i < numThreads; i ++) {
			Thread t = new Thread(new WalmartServiceLoadTest());
			t.start();
		}
		try {
			Thread.sleep(timeoutInMs);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator iter = reservationCode.iterator();
		while(iter.hasNext()) {
			String s = (String) iter.next();
			assertNotNull(s);
		}
		
		assertEquals(6250 - (numThreads * numSeatsToHold) , walmartTS.numSeatsAvailable(Optional.ofNullable(none)));
	}
}
