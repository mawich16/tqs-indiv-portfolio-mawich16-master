package tqs.hw1.boundary;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import tqs.hw1.data.Booking;
import tqs.hw1.data.ReservationState;
import tqs.hw1.dto.BookingRequest;
import tqs.hw1.dto.StateUpdateRequest;
import tqs.hw1.services.BookingService;

@RestController
@CrossOrigin
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.createBooking(
                request.getMunicipality(),
                request.getDate(),
                request.getDetails(),
                request.getState(),
                request.getUserID()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{token}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long token) {
        try {
            Booking booking = bookingService.getBooking(token);
            return ResponseEntity.ok(booking);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long userID) {
        List<Booking> bookings = bookingService.getBookingsByUserID(userID);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/staff/{staffID}")
    public ResponseEntity<List<Booking>> getBookingsByStaff(@PathVariable Long staffID) {
        List<Booking> bookings = bookingService.getBookingsByStaffID(staffID);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{token}/state")
    public ResponseEntity<Booking> updateBookingState(
            @PathVariable Long token, 
            @RequestBody StateUpdateRequest request) {
        try {
            Booking booking = bookingService.updateBookingState(token, request.getState());
            return ResponseEntity.ok(booking);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long token) {
        try {
            bookingService.deleteBooking(token);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/municipality/{municipality}")
    public ResponseEntity<List<Booking>> getBookingsByMunicipality(@PathVariable String municipality) {
        List<Booking> bookings = bookingService.getBookingsByMunicipality(municipality);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Booking>> getBookingsByState(@PathVariable ReservationState state) {
        List<Booking> bookings = bookingService.getBookingsByState(state);
        return ResponseEntity.ok(bookings);
    }
}