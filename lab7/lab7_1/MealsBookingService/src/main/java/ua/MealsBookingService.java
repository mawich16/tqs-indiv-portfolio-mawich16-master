package ua;

import java.time.LocalDate;
import java.util.*;

class MealsBookingService {
    private final Map<String, MealBookingRequest> bookings = new HashMap<>();
    private final int maxCapacityPerDay;
    private final Map<LocalDate, Integer> dailyBookingsCount = new HashMap<>();

    public MealsBookingService(int maxCapacityPerDay) {
        this.maxCapacityPerDay = maxCapacityPerDay;
    }

    public String bookMeal(MealBookingRequest request) {
        // 1. Prevent double booking by same user per date
        for (MealBookingRequest booking : bookings.values()) {
            if (booking.getUserId().equals(request.getUserId())
                    && booking.getDate().equals(request.getDate())
                    && booking.getState() == ReservationState.ACTIVE) {
                throw new IllegalStateException("User already has an active booking for this date.");
            }
        }

        // 2. Check capacity
        int currentCount = dailyBookingsCount.getOrDefault(request.getDate(), 0);
        if (currentCount >= maxCapacityPerDay) {
            throw new IllegalStateException("Capacity reached for this date.");
        }

        // 3. Create reservation code
        String reservationCode = UUID.randomUUID().toString();
        bookings.put(reservationCode, request);

        // 4. Update capacity
        dailyBookingsCount.put(request.getDate(), currentCount + 1);

        return reservationCode;
    }

    // Cancel reservation
    public void cancelReservation(String code) {
        MealBookingRequest booking = bookings.get(code);
        if (booking == null || booking.getState() != ReservationState.ACTIVE) {
            throw new IllegalStateException("Reservation not found or already inactive.");
        }
        booking.setState(ReservationState.CANCELED);
    }

    // Find reservation
    public Optional<MealBookingRequest> findReservation(String code) {
        return Optional.ofNullable(bookings.get(code));
    }

    // Check-in reservation
    public void checkIn(String code) {
        MealBookingRequest booking = bookings.get(code);
        if (booking == null) {
            throw new IllegalStateException("Reservation not found.");
        }
        if (booking.getState() != ReservationState.ACTIVE) {
            throw new IllegalStateException("Reservation is not active.");
        }
        booking.setState(ReservationState.USED);
    }
}
