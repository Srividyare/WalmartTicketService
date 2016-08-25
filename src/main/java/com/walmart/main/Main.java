package com.walmart.main;
import java.net.InetAddress;
import java.util.Optional;
import java.util.Scanner;

import com.walmart.model.SeatHold;
import com.walmart.service.WalmartTicketService;

public class Main {

	static WalmartTicketService walmartTSObject= WalmartTicketService.getInstance();

	public static void main(String args[]){
		Scanner scan = new Scanner(System.in);
		//String s = scan.next();
		//int i = scan.nextInt();
		while(true){
			System.out.println("Select the service required: number only");
			System.out.println(" "+"1.Available Seats");
			System.out.println(" "+"2.Hold Seats");
			System.out.println(" "+"3.Reserve");
			System.out.println(" "+"4.Exit");
			int select=scan.nextInt();

			if(select == 1){
				System.out.println("Enter the level:");
				String s = null;
				if (scan.hasNextLine()) {
					s = scan.nextLine();
					s = scan.nextLine();
				}
				//System.out.println(s);
				Optional<Integer> level = null;
				if (s.equals("")) {
					level=Optional.ofNullable(null);
				} else  {
					level=Optional.of(Integer.parseInt(s));
				}
				System.out.println("Number of seat available:" +walmartTSObject.numSeatsAvailable(level));
			}

			else if(select == 2){
				System.out.println("Enter no .of seat required:");
				int num=scan.nextInt();
				System.out.println("Enter the range of levels desired: or just hit enter twice if not desired");
				String s = null;
				if (scan.hasNextLine()) {
					s = scan.nextLine();
					//s = scan.nextLine();
				}
				//System.out.println(s);
				Optional<Integer> min = null;
				if (s.equals("")) {
					min=Optional.ofNullable(null);
				} else  {
					min=Optional.of(Integer.parseInt(s));
				}
				s = null;
				if (scan.hasNextLine()) {
					s = scan.nextLine();
					s = scan.nextLine();
				}
				//System.out.println(s);
				Optional<Integer> max = null;
				if (s.equals("")) {
					max=Optional.ofNullable(null);
				} else  {
					max=Optional.of(Integer.parseInt(s));
				}
				//Optional<Integer> min=Optional.of(scan.nextInt()); //TODO make optional
				//Optional<Integer> max=Optional.of(scan.nextInt());
				System.out.println("Enter the email address");
				String mail = scan.next();
				long currentTime = System.currentTimeMillis();
				SeatHold seatHoldObject = null;
				try {
					seatHoldObject = walmartTSObject.findAndHoldSeats(num, min, max,mail);
				} catch (Exception e) {
					System.err.println("Exception in holding seats :" + e.getMessage());
				}
				if(seatHoldObject == null){
					System.out.println("Sorry, we only have " + walmartTSObject.numSeatsAvailable(Optional.ofNullable(new Integer(null))) + " seats available" );
				}
				else{
					System.out.println(seatHoldObject.getSeatHoldId());
				}
				long endTime = System.currentTimeMillis();
				System.out.println("operation done in " + (endTime - currentTime) + " milliseconds" );
			}

			else if(select == 3){
				System.out.println("Enter the id:");
				int id=scan.nextInt();
				System.out.println("Enter the email:");
				String mail = scan.next();
				//InetAddress mail = new InetAddress(mail);
				//mail.validate();
				String s = null;
				try {
					s = walmartTSObject.reserveSeats(id, mail);
				} catch (Exception e) {
					System.err.println("Sorry, cannot reserve seats: " + e.getMessage());
				}
				if (s == null) {
					System.out.println("Oops! Looks like you havn't held any seats. Please select option 2 to select hold seats and then reserve");
				} else {
					System.out.println("Confirmation code:" + s);
				}	
			}

			else if(select == 4){
				System.exit(0);
			}
			else {
				System.out.println("Please enter 1 2 3 4 only");
			}
		}
	}
}
