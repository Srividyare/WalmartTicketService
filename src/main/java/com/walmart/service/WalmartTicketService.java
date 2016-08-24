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

import com.walmart.database.*;

public class WalmartTicketService implements TicketService {

	SeatDB seatDbobject= SeatDB.getInstance();
	private static WalmartTicketService instance = null;


	public static WalmartTicketService getInstance(){
		if (instance == null) {
			instance = new WalmartTicketService();
		} 
		return instance;
	}
	@Override
	public synchronized int numSeatsAvailable(Optional<Integer> venueLevel) {



		Seat seatObject = null;
		int count=0;
		//	synchronized(this){
		expireOldHeldSeats();
		if(venueLevel.isPresent()) {
			//edge condition if the customer enters a level which is more than existing then it returns all available seats
			/*if(Optional.ofNullable(venueLevel)> 4){
					venueLevel = Optional.of(0);
				}*/
			Map<Integer, ArrayList<Seat>> rowMap = seatDbobject.getSeatMap().get(venueLevel.get());
			for( Entry<Integer, ArrayList<Seat>> e :  rowMap.entrySet()){

				for(int i=0; i< e.getValue().size(); i++){
					seatObject = e.getValue().get(i);
					if(seatObject.getAvailability() == 0) count++ ;
				}


			}
			return count;

		} else {
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
		if(maximumLevel < minimumLevel) {
			throw new Exception("Max Level should be greater than Min Level");
		}
		if(seatDbobject.getSeatHoldMap().containsKey(customerEmail)){
			throw new Exception("Seats are being held with the given Email address");
		}

		Seat seatObject = null;
		ArrayList<Seat> holdList = new ArrayList<Seat>();

		//	synchronized(this) {
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
		//	}
		for (int i = 0; i < holdList.size(); i ++) {
			holdList.get(i).setAvailability(0);
		}
		holdList.clear();
		return null;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) throws Exception {
		expireOldHeldSeats();
		Seat seatObject=null;
		if (seatDbobject.getSeatHoldMap().get(customerEmail) == null) {
			throw new Exception("Sorry no seats are being hold or held seats have expired for this id. Please hold seats again.");
		}
		List<Seat> reserveList = seatDbobject.getSeatHoldMap().get(customerEmail).getHeldSeat();

		for(int i= 0; i<reserveList.size();i++){
			seatObject= reserveList.get(i);
			seatObject.setAvailability(2);

		}
		seatDbobject.getSeatHoldMap().remove(customerEmail);

		return String.valueOf((seatHoldId + customerEmail).hashCode());
	}

	public void expireOldHeldSeats() {
		Collection c = Collections.synchronizedCollection(seatDbobject.getSeatHoldMap().entrySet());
		Iterator<Entry<String, SeatHold>> iter = c.iterator();
		while (iter.hasNext()) {
			Entry<String, SeatHold> entry = iter.next();
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

	public  void makeAllSeatsAvailable() {
		seatDbobject.fillLevel();
	}


}
