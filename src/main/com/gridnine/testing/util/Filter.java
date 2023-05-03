package com.gridnine.testing.util;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Filter {
    // исключение вылетов, которые уже прошли
    public static List<Flight> filterNotValidDepartureDate(List<Flight> flights) {
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight != null) {
                Optional<Segment> badSegment = flight.getSegments().stream()
                        .filter(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now()))
                        .findFirst();
                if (badSegment.isEmpty()) {
                    filteredFlights.add(new Flight(flight.getSegments()));
                }
            }
        }
        return filteredFlights;
    }
    // исключение перелетов, если имеются сегменты с датой прилёта раньше даты вылета
    public static List<Flight> filterNotValidDatesDepartureAndArrival(List<Flight> flights) {
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight != null) {
                Optional<Segment> badSegment = flight.getSegments().stream()
                        .filter(segment ->
                                segment.getArrivalDate().isBefore(segment.getDepartureDate()))
                        .findFirst();
                if (badSegment.isEmpty()) {
                    filteredFlights.add(new Flight(flight.getSegments()));
                }
            }
        }
        return filteredFlights;
    }

    // исключение перелетов, если
    // общее время, проведённое на земле превышает два часа
    // (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
    public static List<Flight> filterFlightsWhenIntervalMoreThan(List<Flight> flights, Integer minutesInterval) {
        List<Flight> filteredFlights = new ArrayList<>();
        LocalDateTime startTime;
        LocalDateTime endTime;
        Boolean isValid = true;
        for (Flight flight : flights) {
            if (flight != null) {
                if (flight.getSegments().size() > 1) {
                    for (int i = 0; i < flight.getSegments().size() - 1; i++) {
                        startTime = flight.getSegments().get(i).getArrivalDate();
                        endTime = flight.getSegments().get(i + 1).getDepartureDate();
                        if (SECONDS.between(startTime, endTime) > minutesInterval * 60) {
                            isValid = false;
                            break;
                        }
                    }
                }
            }
            if (isValid) {
                filteredFlights.add(new Flight(flight.getSegments()));
            }
        }
        return filteredFlights;
    }
}
