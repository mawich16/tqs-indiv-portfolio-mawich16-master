package tqs.hw1.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import tqs.hw1.data.Booking;
import tqs.hw1.data.BookingRepository;
import tqs.hw1.data.ReservationState;

@Service
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    String notFoundMessage = "Booking not found with id ";

    public BookingServiceImp(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(String municipality, LocalDateTime date, String details, ReservationState state, Long userID) {
        return bookingRepository.save(new Booking(municipality, date, details, state, userID));
    }

    @Override
    public Booking getBooking(Long token) {
        return bookingRepository.findById(token)
        .orElseThrow(() -> new EntityNotFoundException(notFoundMessage + token));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking updateBookingState(Long token, ReservationState state) {
        Booking booking = bookingRepository.findById(token)
        .orElseThrow(() -> new EntityNotFoundException(notFoundMessage + token));

        booking.setState(state);
        bookingRepository.save(booking);

        return booking;
    }

    @Override
    public void deleteBooking(Long token) {
        Booking booking = bookingRepository.findById(token)
        .orElseThrow(() -> new EntityNotFoundException(notFoundMessage + token));

        bookingRepository.delete(booking);
    }

    @Override
    public List<Booking> getBookingsByUserID(Long userID) {
        return bookingRepository.findByUserID(userID);
    }

    @Override
    public List<Booking> getBookingsByStaffID(Long staffID) {
        return bookingRepository.findByStaffID(staffID);
    }

    @Override
    public List<Booking> getBookingsByMunicipality(String municipality) {
        return bookingRepository.findByMunicipality(municipality);
    }

    @Override
    public List<Booking> getBookingsByState(ReservationState state) {
        return bookingRepository.findByState(state);
    }

}
