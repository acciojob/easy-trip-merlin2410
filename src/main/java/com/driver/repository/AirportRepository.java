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
        if(airportDb.size()==0)
            return list;
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
        if(flightDb.containsKey(flightId))
        {
            Flight flight = flightDb.get(flightId);
            City fromCity = flight.getFromCity();
            for(Airport airport: airportDb.values())
            {
                if(fromCity.equals(airport.getCity()))
                    return airport.getAirportName();
            }
        }
        return null;

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
        if(flightDb.size()==0)
            return list;
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
        if(Objects.isNull(airportDb))
            return 0;
        City city = airportDb.get(airportName).getCity();

        for(Flight flight: flightDb.values())
        {

            if(date.equals(flight.getFlightDate()))
            {
                if(city.equals(flight.getFromCity()) || city.equals(flight.getToCity()))
                {
                    numberOfPeople += bookings.get(flight.getFlightId()).size();
                }
            }
        }
        return numberOfPeople;
    }

    public int calculateRevenueOfAFlight(Integer flightId)
    {
        int revenue = 0;
        if(!bookings.containsKey(flightId))
            return 0;
        HashMap<Integer,Integer> passengerList = bookings.get(flightId);
        if(passengerList.size()==0)
            return 0;
        for(Integer fare: passengerList.values())
        {
            revenue += fare;
        }
        return revenue;
    }
}
