package com.walmart.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.walmart.config.Config;
import com.walmart.database.SeatDB;
import com.walmart.model.Seat;
import com.walmart.model.SeatHold;

public class WalmartTicketService implements TicketService {

	SeatDB seatDbobject= SeatDB.getInstance();
	private static WalmartTicketService instance = null;

	/**
	 * Generates the object of WalmastTicketService class
	 * @return instance
	 */
	public static WalmartTicketService getInstance(){
		if (instance == null) {
			instance = new WalmartTicketService();
		} 
		return instance;
	}
	//Synchronized to make it thread safe so that no two users can hold or reserve a single seat
	@Override
	public synchronized int numSeatsAvailable(Optional<Integer> venueLevel) {

		Seat seatObject = null;
		int count=0;

		expireOldHeldSeats();
		//if the venueLevel is a valid value (1<= venueLevel <=4) 
		if(venueLevel.isPresent()) {

			Map<Integer, ArrayList<Seat>> rowMap = seatDbobject.getSeatMap().get(venueLevel.get());

			//Iterate over the map to count the available seats in the particular level by checking if the availability is 0
			for( Entry<Integer, ArrayList<Seat>> e :  rowMap.entrySet()){

				for(int i=0; i< e.getValue().size(); i++){
					seatObject = e.getValue().get(i);
					if(seatObject.getAvailability() == 0) count++ ;
				}
			}
			return count;
		} 
		//if the venueLevel is not a valid value then return all the seats available
		else {
			Map<Integer, HashMap<Integer, ArrayList<Seat>>> levelMap = seatDbobject.getSeatMap();
			for(Entry<Integer, HashMap<Integer, ArrayList<Seat>>> e : levelMap.entrySet()){
				for(Entry<Integer, ArrayList<Seat>> e1 : e.getValue().entrySet()){
					for(int i=0; i< e1.getValue().size(); i++){
						seatObject = e1.getValue().get(i);
						if(seatObject.getAvailability() == 0) count++ ;
					}
				}
			}
		}
		//}
		return count;
	}
	//Synchronized to make it thread safe so that no two users can hold or reserve a single seat
	@Override
	public synchronized SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) throws Exception {

		int minimumLevel = 1, maximumLevel = 4;
		if (minLevel.isPresent()) {
			minimumLevel = minLevel.get();
		} 
		if (maxLevel.isPresent()) {
			maximumLevel = maxLevel.get();
		}
		// Check the input of levels given by user
		if(maximumLevel < minimumLevel) {
			throw new Exception("Max Level should be greater than Min Level");
		}
		// Check if there are seats being held on the given email address. Fail fast scenarios. 
		if(seatDbobject.getSeatHoldMap().containsKey(customerEmail)){
			throw new Exception("Seats are already being held with the given Email address");
		}

		Seat seatObject = null;
		ArrayList<Seat> holdList = new ArrayList<Seat>();


		expireOldHeldSeats();
		Map<Integer, HashMap<Integer, ArrayList<Seat>>> levelMap = seatDbobject.getSeatMap();

		if(numSeats != 0){
			for(int i= minimumLevel; i<= maximumLevel ; i++){
				Map<Integer, ArrayList<Seat>> rowMap = levelMap.get(i);
				for( Entry<Integer, ArrayList<Seat>> e :  rowMap.entrySet()){

					for(int j=0; j< e.getValue().size(); j++){
						seatObject = e.getValue().get(j);
						if(seatObject.getAvailability() == 0) {
							seatObject.setAvailability(1);
							holdList.add(seatObject);
							numSeats--;
							if(numSeats == 0){
								SeatHold seatHold  = new SeatHold(customerEmail.hashCode(),customerEmail, holdList, System.currentTimeMillis());
								seatDbobject.getSeatHoldMap().put(customerEmail, seatHold);

								return seatHold;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < holdList.size(); i ++) {
			holdList.get(i).setAvailability(0);
		}
		holdList.clear();
		return null;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) throws Exception { 
	// here customerEmail is the one that we use as key. seatHoldId maybe used in future when the design
  // is changed to have multiple seatHold requests per email.
		  
		expireOldHeldSeats();
		Seat seatObject=null;
		//Check if there are seats being held or expired
		if (seatDbobject.getSeatHoldMap().get(customerEmail) == null) {
			throw new Exception("Sorry no seats are being hold or held seats have expired for this id. Please hold seats again.");
		}
		List<Seat> reserveList = seatDbobject.getSeatHoldMap().get(customerEmail).getHeldSeat();
		// Setting availability status to reserved
		for(int i= 0; i<reserveList.size();i++){
			seatObject= reserveList.get(i);
			seatObject.setAvailability(2);
		}
		seatDbobject.getSeatHoldMap().remove(customerEmail);

		return String.valueOf((seatHoldId + customerEmail).hashCode());
	}
	/**
	 * Method releases seats which have expired. 
	 * This is called before all operations, i.e finding available seats,
	 * holding seats and reserving seats.
	 * 
	 * Other approaches:
	 * One idea for expiry is to use a guava cache with time expiry, although
	 * this approach in this scenario is much cheaper than guava.
	 * Other idea is to use a expiry thread that keeps running in the background
	 * to expire old seats. This may cause some delay in the expiry as we don't want
	 * to run the thread continuously and burn the cpu.
	 */
	public void expireOldHeldSeats() {
		Collection c = Collections.synchronizedCollection(seatDbobject.getSeatHoldMap().entrySet());
		Iterator<Entry<String, SeatHold>> iter = c.iterator();
		while (iter.hasNext()) {
			Entry<String, SeatHold> entry = iter.next();
			//Check if the difference between current time and the holding request time exceeds the expire time
			if((System.currentTimeMillis() - entry.getValue().getTime()) > Config.heldSeatsExpiryTime){

				List<Seat> seats = entry.getValue().getHeldSeat();

				for (int i=0;i<seats.size();i++) {
					Seat s = seats.get(i);
					seatDbobject.getSeatMap().get(s.getLevel()).get(s.getRow()).get(s.getSeatNum()-1).setAvailability(0);
				}
				iter.remove();
			}
		}
	}
	/**
	 * Helper method for testing
	 * Method clears all seats and set availability status to 0
	 */
	public  void makeAllSeatsAvailable() {
		seatDbobject.fillLevel();
	}
}
