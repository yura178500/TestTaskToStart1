package com.gridnine.testing;

import com.gridnine.testing.builder.FlightBuilder;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.util.Filter;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Исходный тестовый набор перелетов:");
        List<Flight> flights = FlightBuilder.createFlights();
        printFlights(flights);
        System.out.println("-------------------------------------------------");
        System.out.println("Фильтр прошедших перелетов");
        printFlights(Filter.filterNotValidDepartureDate(flights));
        System.out.println("-------------------------------------------------");
        System.out.println("Фильтр неправильных дат вылета и прилета");
        printFlights(Filter.filterNotValidDatesDepartureAndArrival(flights));
        System.out.println("-------------------------------------------------");
        System.out.println("Фильтр перелетов с длительным интервалом ожидания");
        printFlights(Filter.filterFlightsWhenIntervalMoreThan(flights, 120));
    }
    // вывод в консоль списка перелетов
    static void printFlights(List<Flight> flights) {
        flights.stream()
                .forEach(System.out::println);
    }
}

