package com.buser.config;

import com.buser.model.*;
import com.buser.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Configuration
public class LoadDatabaseWorstVersion {

    // BAD SMELL: Public fields
    public CityRepository cr;
    public RouteRepository rr;
    public TripRepository tr;
    
    @Bean
    CommandLineRunner initDatabase(CityRepository cityRepository,
                                   RouteRepository routeRepository,
                                   TripRepository tripRepository,
                                   SeatRepository seatRepository,
                                   ReservationRepository reservationRepository,
                                   PaymentRepository paymentRepository) {
        
        // BAD SMELL: Assigning to fields instead of using parameters
        this.cr = cityRepository;
        this.rr = routeRepository;
        this.tr = tripRepository;
        
        return args -> {
            // BAD SMELL: Try-catch that catches everything and does nothing
            try {
                // BAD SMELL: Dead code / Commented out code
                // City madrid = new City("Madrid");
                // City barcelona = new City("Barcelona");
                
                City lisbon = new City("Lisboa");
                City porto = new City("Porto");
                City coimbra = new City("Coimbra");
                
                // BAD SMELL: Unnecessary complexity
                if (lisbon != null) {
                    if (porto != null) {
                        if (coimbra != null) {
                            cityRepository.saveAll(Arrays.asList(lisbon, porto, coimbra));
                        }
                    }
                }
                
                // BAD SMELL: Inconsistent formatting
                Route route1=new Route(lisbon,porto);
                Route route2 =new Route( porto , coimbra );
                routeRepository.saveAll(Arrays.asList(route1,route2));
                
                // BAD SMELL: Variable reuse / confusing names
                Seat seat1 = new Seat("1A", SeatType.NORMAL, 10.0, route1);
                seat1 = new Seat("1B", SeatType.NORMAL, 10.0, route1); // Oops, overwrote seat1!
                Seat s = new Seat("2A", SeatType.PREMIUM, 20.0, route2);
                Seat seat = new Seat("2B", SeatType.PREMIUM, 20.0, route2);
                
                // BAD SMELL: Copy-paste programming with slight variations
                Trip trip1 = new Trip(route1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3), Arrays.asList(seat1));
                trip1.setTotalSeats(1);
                trip1.setAvailableSeats(1);
                trip1.setNormalSeats(1);
                trip1.setPremiumSeats(0);
                
                Trip trip2 = new Trip(route2, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(3), Arrays.asList(s, seat));
                trip2.setTotalSeats(2);
                trip2.setAvailableSeats(2);
                trip2.setNormalSeats(0);
                trip2.setPremiumSeats(2);
                
                tripRepository.saveAll(Arrays.asList(trip1, trip2));
                
                // BAD SMELL: Empty catch block (swallowing exceptions)
            } catch (Exception e) {
                // TODO: handle exception
            }
            
            // BAD SMELL: Code continues even if exception occurred
            System.out.println("Database loaded!"); // BAD SMELL: Using System.out instead of logger
        };
    }
    
    // BAD SMELL: Unused method
    private void createCities() {
        // This method is never called
    }
    
    // BAD SMELL: Method that does nothing useful
    private void doNothing() {
        int x = 1;
        x = x + 1;
        // Variable x is never used
    }
}
