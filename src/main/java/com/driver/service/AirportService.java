package com.driver.service;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirportService {

    @Autowired
    AirportRepository airportRepository;

    public void addAirport(Airport airport)
    {
        airportRepository.addAirport(airport);
    }

    public String getLargestAirportName()
    {
        List<Airport> list = airportRepository.listOfAirports();
        Collections.sort(list, new Comparator<Airport>() {
            @Override
            public int compare(Airport o1, Airport o2) {
                if(o1.getNoOfTerminals()!=o2.getNoOfTerminals())
                    return o1.getNoOfTerminals()- o2.getNoOfTerminals();
                return o2.getAirportName().compareTo(o1.getAirportName());
            }
        });
        return list.get(list.size()-1).getAirportName();
    }

    public void addFlight(Flight flight)
    {
        airportRepository.addFlight(flight);
    }

    public void addPassenger(Passenger passenger)
    {
        airportRepository.addPassenger(passenger);
    }

    public String bookATicket(Integer flightId, Integer passengerId)
    {
        String message = airportRepository.bookATicket(flightId,passengerId);
        return message;
    }

    public String cancelATicket(Integer flightId, Integer passengerId)
    {
        String message = airportRepository.cancelATicket(flightId,passengerId);
        return message;
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId)
    {
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public String getAirportNameFromFlightId(Integer flightId)
    {
        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public int calculateFlightFare(Integer flightId)
    {
        return airportRepository.calculateFlightFare(flightId);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity)
    {
        List<Flight> list = airportRepository.listOfFlightsBetweenCities(fromCity,toCity);
        if(list.size()==0)
            return -1;
        Collections.sort(list, new Comparator<Flight>() {
            @Override
            public int compare(Flight o1, Flight o2) {
                if(o1.getDuration()>o2.getDuration())
                    return 1;
                else if(o1.getDuration()<o2.getDuration())
                    return -1;
                return 0;
            }
        });

        return list.get(0).getDuration();
    }

    public int getNumberOfPeopleOn(Date date, String airportName)
    {
        return airportRepository.getNumberOfPeopleOn(date,airportName);
    }

    public int calculateRevenueOfAFlight(Integer flightId)
    {
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }


}
