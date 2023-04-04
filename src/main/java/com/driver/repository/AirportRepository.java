package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {

    HashMap<String, Airport> airportDb = new HashMap<>();
    HashMap<Integer, Flight> flightDb = new HashMap<>();
    HashMap<Integer, Passenger> passengerDb = new HashMap<>();
    HashMap<Integer, HashMap<Integer,Integer>> bookings = new HashMap<>();
    HashMap<Integer, Integer> numberOfBookings = new HashMap<>();

    public void addAirport(Airport airport)
    {
        String airportName = airport.getAirportName();
        airportDb.put(airportName,airport);
    }

    public List<Airport> listOfAirports()
    {
        List<Airport> list = new ArrayList<>();
        for(Airport airport: airportDb.values())
        {
            list.add(airport);
        }
        return list;
    }

    public void addFlight(Flight flight)
    {
        int flightId = flight.getFlightId();
        flightDb.put(flightId,flight);
    }

    public void addPassenger(Passenger passenger)
    {
        int passengerId = passenger.getPassengerId();
        passengerDb.put(passengerId,passenger);
    }

    public String bookATicket(Integer flightId, Integer passengerId)
    {
        if(!flightDb.containsKey(flightId))
            return "FAILURE";
        Flight flight = flightDb.get(flightId);
        if(bookings.containsKey(flightId))
        {
            HashMap<Integer,Integer> passengerList = bookings.get(flightId);
            if(passengerList.containsKey(passengerId))
                return "FAILURE";
            if(passengerList.size()>=flight.getMaxCapacity())
                return "FAILURE";
            int fare = calculateFlightFare(flightId);
            passengerList.put(passengerId,fare);
            bookings.put(flightId,passengerList);
        }
        else
        {
            HashMap<Integer,Integer> passengerList = new HashMap<>();
            int fare = calculateFlightFare(flightId);
            passengerList.put(passengerId,fare);
            bookings.put(flightId,passengerList);
        }
        numberOfBookings.put(passengerId,numberOfBookings.getOrDefault(passengerId,0)+1);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId)
    {
        if(!bookings.containsKey(flightId))
            return "FAILURE";

        HashMap<Integer,Integer> passengerList = bookings.get(flightId);

        if(!passengerList.containsKey(passengerId))
            return "FAILURE";

        passengerList.remove(passengerId);
        if(passengerList.size()==0)
            bookings.remove(flightId);
        else
            bookings.put(flightId,passengerList);
        numberOfBookings.put(passengerId,numberOfBookings.getOrDefault(passengerId,0)-1);
        return "SUCCESS";

    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId)
    {
        return numberOfBookings.getOrDefault(passengerId,0);
    }

    public String getAirportNameFromFlightId(Integer flightId)
    {
        if(!flightDb.containsKey(flightId))
            return null;
        Flight flight = flightDb.get(flightId);
        return flight.getFromCity().toString();
    }

    public int calculateFlightFare(Integer flightId)
    {
        int variable = 0;
        if(bookings.containsKey(flightId))
            variable = 50*bookings.get(flightId).size();
        int fare = 3000 + variable;
        return fare;
    }

    public List<Flight> listOfFlightsBetweenCities(City fromCity, City toCity)
    {
        List<Flight> list = new ArrayList<>();
        for(Flight flight: flightDb.values())
        {
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity))
            {
                list.add(flight);
            }
        }
        return list;
    }

    public int getNumberOfPeopleOn(Date date, String airportName)
    {
        int numberOfPeople = 0;
        for(Flight flight: flightDb.values())
        {
            if(flight.getFromCity().toString().equals(airportName) || flight.getToCity().toString().equals(airportName))
            {
                numberOfPeople += bookings.get(flight.getFlightId()).size();
            }
        }
        return numberOfPeople;
    }

    public int calculateRevenueOfAFlight(Integer flightId)
    {
        int revenue = 0;
        HashMap<Integer,Integer> passengerList = bookings.get(flightId);
        for(Integer fare: passengerList.values())
        {
            revenue += fare;
        }
        return revenue;
    }
}
