package tqs.hw1.services;

import java.time.LocalDateTime;
import java.util.List;

import tqs.hw1.data.Booking;
import tqs.hw1.data.ReservationState;

public interface BookingService {
    Booking createBooking(String municipality, LocalDateTime date, String details, ReservationState state, Long userID);
    Booking getBooking(Long token);
    List<Booking> getAllBookings();
    Booking updateBookingState(Long token, ReservationState state);
    void deleteBooking(Long token);
    List<Booking> getBookingsByUserID(Long userID);
    List<Booking> getBookingsByStaffID(Long staffID);
    List<Booking> getBookingsByMunicipality(String municipality);
    List<Booking> getBookingsByState(ReservationState state);
}