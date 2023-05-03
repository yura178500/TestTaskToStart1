package com.gridnine.testing;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.util.Filter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {
    @Test
    // тест на исключение перелета, если время вылета уже прошло
    public void shouldReturnEmptyWhenDepartureDateBeforeNowFilter(){
        Segment segment = new Segment(LocalDateTime.now().minusHours(1),LocalDateTime.now());
        List<Segment> segs = new ArrayList<>();
        segs.add(segment);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(segs));
        List<Flight> filteredFlights = Filter.filterNotValidDepartureDate(flights);
        assertTrue(filteredFlights.isEmpty(),"Список перелетов должен быть пустым");
    }
    @Test
    // тест на то, что фильтр не влияет на перелет, если время вылета больше или равно текущему времени
    public void shouldReturnNotEmptyWhenDepartureDateNowOrAfterFilter(){
        Segment segment = new Segment(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
        List<Segment> segs = new ArrayList<>();
        segs.add(segment);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(segs));
        List<Flight> filteredFlights = Filter.filterNotValidDepartureDate(flights);
        assertEquals(1, filteredFlights.size(),"В списке перелетов должен быть один элемент");
    }

    @Test
    // тест на исключение перелета, если время вылета позже времени прилета
    public void shouldReturnEmptyWhenArrivalDateBeforeDepartureDateFilter(){
        Segment segment = new Segment(LocalDateTime.now(), LocalDateTime.now().minusHours(1));
        List<Segment> segs = new ArrayList<>();
        segs.add(segment);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(segs));
        List<Flight> filteredFlights = Filter.filterNotValidDatesDepartureAndArrival(flights);
        assertTrue(filteredFlights.isEmpty(),"Список перелетов должен быть пустым");
    }

    @Test
    // тест на то, что фильтр не влияет на перелет, если время вылета раньше времени прилета
    public void shouldReturnNotEmptyWhenArrivalDateAfterDepartureDateFilter(){
        Segment segment = new Segment(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        List<Segment> segs = new ArrayList<>();
        segs.add(segment);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(segs));
        List<Flight> filteredFlights = Filter.filterNotValidDatesDepartureAndArrival(flights);
        assertEquals(1, filteredFlights.size(),"В списке перелетов должен быть один элемент");
    }

    @Test
    // тест на исключение перелета с длительным интервалом ожидания
    public void shouldReturnEmptyWhenLongWaitingInterval(){
        Integer interval = 120; // интервал ожидания в минутах
        LocalDateTime time = LocalDateTime.now();
        Segment segment1 = new Segment(time.minusHours(1), time);
        Segment segment2 = new Segment(time.plusHours(2).plusSeconds(1), time.plusHours(5));
        List<Segment> segs = new ArrayList<>();
        segs.add(segment1);
        segs.add(segment2);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(segs));
        List<Flight> filteredFlights = Filter.filterFlightsWhenIntervalMoreThan(flights, interval);
        assertTrue(filteredFlights.isEmpty(),"Список перелетов должен быть пустым");
    }

    @Test
    // тест на то, что фильтр не влияет на перелет с малым интервалом ожидания
    public void shouldReturnEmptyWhenNormalWaitingInterval(){
        Integer interval = 160; // интервал ожидания в минутах
        LocalDateTime time = LocalDateTime.now();
        Segment segment1 = new Segment(time.minusHours(1), time);
        Segment segment2 = new Segment(time.plusHours(2).plusSeconds(1), time.plusHours(5));
        List<Segment> segs = new ArrayList<>();
        segs.add(segment1);
        segs.add(segment2);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(segs));
        List<Flight> filteredFlights = Filter.filterFlightsWhenIntervalMoreThan(flights, interval);
        assertEquals(1, filteredFlights.size(),"В списке должен быть один перелет");
    }
}
