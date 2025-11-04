package tqs.lab3meals.services;

import java.util.List;
import java.util.Optional;

import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.ReservationState;

public interface MealBookingService {

    public Optional<MealBooking> getMealBookingDetails(Long id);
    public List<MealBooking> getAllMealBookings();
    public MealBooking save(MealBooking mb);
    public void deleteMealBooking(MealBooking mb);
    public MealBooking changeMealBookingState(Long id, ReservationState state);
    public void cancelReservation(MealBooking mealBooking);
    public void checkIn(MealBooking mealBooking);

}
