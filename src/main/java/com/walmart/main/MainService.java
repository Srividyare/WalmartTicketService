package com.walmart.main;

import java.util.Optional;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.walmart.database.SeatDB;
import com.walmart.model.SeatHold;
import com.walmart.service.WalmartTicketService;

@Path("/tickets")
public class MainService {
  WalmartTicketService walmartTS = WalmartTicketService.getInstance();
  Integer none;
  //  @GET
  //  @Path("/{param}")
  //  public Response getMsg(@PathParam("param") String msg) {
  //
  //    String output = "Jersey say : " + msg;
  //
  //    return Response.status(200).entity(output).build();
  //
  //  }

  @GET
  @Path("/availableSeats")
  public Response getNumAvailableSeats(@QueryParam("levelNumber") @DefaultValue("0") int levelNumber) {
    Integer seats = 0;
    if (levelNumber >= 1 && levelNumber <= 4) {
      seats = walmartTS.numSeatsAvailable(Optional.of(levelNumber)); 
    } else {
      seats = walmartTS.numSeatsAvailable(Optional.ofNullable(none)); 
    }

    return Response.status(200).entity(seats.toString()).build();

  }

  @GET
  @Path("/holdSeats")
  @Produces(MediaType.APPLICATION_JSON)
  public SeatHold holdSeats(@QueryParam("numSeats") int numSeats,
      @QueryParam("minLevel") @DefaultValue("0") int minLevel,
      @QueryParam("maxLevel") @DefaultValue("0") int maxLevel,
      @QueryParam("email") String email) throws Exception {
    SeatHold seatHold;
    if (email == null || email.equals("") || numSeats == 0) {
    	throw new Exception("Please give a valid input");
    }
    if (minLevel >= 1 && maxLevel <= 4) {
      try {
				seatHold = walmartTS.findAndHoldSeats(numSeats, Optional.of(minLevel), Optional.of(maxLevel), email);
			} catch (Exception e) {
			return new SeatHold(e);
			}
    } else {
      minLevel = 1;
      maxLevel = 4;
      try {
				seatHold = walmartTS.findAndHoldSeats(numSeats, Optional.ofNullable(none), Optional.ofNullable(none), email);
			} catch (Exception e) {
				return new SeatHold(e);
			}
    }
    return seatHold;
  }

  @GET
  @Path("/reserveSeats")
  public Response reserveSeats(@QueryParam("seatHoldId") int seatHoldId,
      @QueryParam("email") String email) {

    String reservationConfirm = null;
    try {
      reservationConfirm = walmartTS.reserveSeats(seatHoldId, email);
    } catch (Exception e) {

      return Response.status(200).entity("Error reserving your seats. Please re-hold your seats." ).build();
    }

    return Response.status(200).entity("Your confirmation code is " + reservationConfirm).build();
  }

}