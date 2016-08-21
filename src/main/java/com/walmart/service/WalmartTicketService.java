package com.walmart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.walmart.database.*;

public class WalmartTicketService implements TicketService {

	SeatDB obj= SeatDB.getInstance();
	private static WalmartTicketService instance = null;
	/**
	 * 
	 */
	
	public static WalmartTicketService getInstance(){
		if (instance == null) {
			instance = new WalmartTicketService();
		} 
		return instance;
	}
	
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		synchronized(this) {

			Seat obj2 = null;
			int count=0;
			if(venueLevel.isPresent()) {
				Map<Integer, ArrayList<Seat>> rowMap = obj.getSeatMap().get(venueLevel.get());
				for( Entry<Integer, ArrayList<Seat>> e :  rowMap.entrySet()){

					for(int i=0; i< e.getValue().size(); i++){
						obj2 = e.getValue().get(i);
						if(obj2.getAvailability() == 0) count++ ;
					}


				}
				return count;

			} else {
				Map<Integer, HashMap<Integer, ArrayList<Seat>>> levelMap = obj.getSeatMap();
				for(Entry<Integer, HashMap<Integer, ArrayList<Seat>>> e : levelMap.entrySet()){
					for(Entry<Integer, ArrayList<Seat>> e1 : e.getValue().entrySet()){
						for(int i=0; i< e1.getValue().size(); i++){
							obj2 = e1.getValue().get(i);
							if(obj2.getAvailability() == 0) count++ ;
						}
					}

				}
				return count;

			}
		}
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		int minimumLevel = 1, maximumLevel = 4;
		if (minLevel.isPresent()) {
			minimumLevel = minLevel.get();
		} 
		if (maxLevel.isPresent()) {
			maximumLevel = maxLevel.get();
		}
		Seat obj2 = null;
		ArrayList<Seat> holdList = new ArrayList<Seat>();
		synchronized(this) {
			Map<Integer, HashMap<Integer, ArrayList<Seat>>> levelMap = obj.getSeatMap();
		
			if(numSeats != 0){
				for(int i= minimumLevel; i<= maximumLevel ; i++){
					Map<Integer, ArrayList<Seat>> rowMap = levelMap.get(i);
					for( Entry<Integer, ArrayList<Seat>> e :  rowMap.entrySet()){

						for(int j=0; j< e.getValue().size(); j++){
							obj2 = e.getValue().get(j);
							if(obj2.getAvailability() == 0) {
								obj2.setAvailability(1);
								holdList.add(obj2);
								numSeats--;
								if(numSeats == 0){
									SeatHold seatHold  = new SeatHold(customerEmail.hashCode(),customerEmail, holdList, System.currentTimeMillis());
									obj.getSeatHoldMap().put(customerEmail, seatHold);
									
									return seatHold;
								}

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
	public String reserveSeats(int seatHoldId, String customerEmail) {
		Seat obj2=null;
		if (obj.getSeatHoldMap().get(customerEmail) == null) {
			return null;
		}
		List<Seat> reserveList = obj.getSeatHoldMap().get(customerEmail).getHeldSeat();
		
		for(int i= 0; i<reserveList.size();i++){
			obj2= reserveList.get(i);
			obj2.setAvailability(2);
					
		}
		obj.getSeatHoldMap().remove(customerEmail);
		
		return String.valueOf(seatHoldId);
	}



}
