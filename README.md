# Walmart Homework - TicketService
## Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.


Tech Stack:
1. Java 8
2. Jersey 1.8
3. Maven

Assumptions:

1. User may hold multiple seats, but in a single request. If a user tries to hold seats again, he will be prompted to reserve the seats he currently holds, or wait until they expire. This is made to restrict any user from holding all seats in multiple request (if a maximum number of seats than can be held by a particular user comes in future). 
2. No disk based data-structure, or database is used so all seats hold requests and reservations are valid only in the current run of the application.

The solution allows a user to find all available seats, either for a particular level or all levels. Then the user may hold seats providing the number of seats to be held, the levels where he want to hold then (Optional
and their email id. After holding the seats he receives an unique id for the seats he held. Currently a user can have only one seat hold request and if he tries to create a new one, he will be prompted to either reserve his previous 
seat hold request or wait for 60 seconds till they expire. Finally, the user may provide the seat hold id and the email id he used to hold the seats to reserve their seats and receives a confirmation id. 

Deploying and running the app.

The application can be run in two ways as discussed below,

1. Command Line 
  - Clone the project
     `git@github.com:Srividyare/WalmartTicketService.git`
  - Go to the directory
     `cd WalmartTicketService` 
  - Compile the app with maven and run the test with
      `mvn clean compile install`
  - Check if the jar file is created in target/TicketService-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  - To run the app
     `java -jar TicketService-0.0.1-SNAPSHOT-jar-with-dependencies.jar`
  - Follow the instructions on the command line. 

  Example Run:
  `Select the service required: number only
  1.Available Seats
  2.Hold Seats
  3.Reserve
  4.Exit
 1 <Enter>
 Enter the level:
 1 <Enter>
 Number of seat available:1250
 Select the service required: number only
 1.Available Seats
 2.Hold Seats
 3.Reserve
 4.Exit
 2 <Enter>
 Enter no .of seat required:
 10 <Enter>
 Enter the range of levels desired: or just hit enter twice if not desired
 1 <Enter>
 2 <Enter>
 Enter the email address
 srividya.re@gmail.com <Enter>
 -983208765
 operation done in 3 milliseconds`

2. Using REST API
 - Clone the project
     `git@github.com:Srividyare/WalmartTicketService.git`
 - Go to the directory
     `cd WalmartTicketService` 
 - All api are GET requests only 

 - To start the server run,
   `mvn jetty:run`

 - To get the number of available seats. levelNumber parameter is optional
   `http://localhost:8080/ticketservice/tickets/availableSeats?levelNumber={Optional-Level-Number}`

 - To hold the seats. numSeats and email parameters are mandatory and exception will be thrown if they are not provided. minLevel and maxLevel parameters are optional.
   `http://localhost:8080/ticketservice/tickets/holdSeats?numSeats={Num-Seats}&email={Email-ID}&minLevel={Optional-min-level}&maxLevel={Optional-max-level}`

 - To reserve the seats. seatHoldId and email parameters are mandatory
   `http://localhost:8080/ticketservice/tickets/reserveSeats?seatHoldId={SeatHoldId}&email={Email-ID}`

## Tl;dr
###### Design
A few things to note about design in the project is that there is no external service or disk based storage implemented to hold the seats and venue information.
Seat is a class which holds information about each seat in the venue. SeatHold is the class that gets created whenever a user asks to hold seats.
SeatDB is the class which maintains the in-memory database about seats in the venue and also a map with the SeatHold requests. 
###### Coding and Development
While developing the project, I tried to handle many edge cases like bad inputs from user etc. 
SeatDB -  The map that holds the seat information is a regular
hashmap that is not synchronized or concurrent, and hence the methods the use and modify it are synchronized to provide correctness. Whereas, the map to hold the SeatHold requests is
a ConcurrentHashMap because we want to expire the requests without any locks and a `get` operation on a ConcurrentHashMap is volatile and hence we never get stale data. 

###### Testing
To test the sanity of the app, I created a lots of test cases that captures multiple edge cases. One of them is WalmartServiceLoadTest, which runs multiple threads (50 in this case) and requests to hold seats and reserve them. This validates the correctness of the underlying logic with heavy load and that makes sure that no two threads/users get the same seats.
This is achieved because of synchronization implemented in the logic. I had to use synchronization because of the database chosen for the seats, an in-memory map.  

