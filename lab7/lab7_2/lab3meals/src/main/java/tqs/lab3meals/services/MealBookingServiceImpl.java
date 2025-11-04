package tqs.lab3meals.services;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.data.ReservationState;

import java.util.List;
import java.util.Optional;

@Service
public class MealBookingServiceImpl implements MealBookingService {

    private final MealBookingRepository mealBookingRepository;

    public MealBookingServiceImpl(MealBookingRepository mealBookingRepository) {
        this.mealBookingRepository = mealBookingRepository;
    }

    public Optional<MealBooking> getMealBookingDetails(Long id) {
        return mealBookingRepository.findById(id);
    }

    @Transactional
    public MealBooking changeMealBookingState(Long id, ReservationState state) {
        MealBooking mealBooking = mealBookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MealBooking not found with id " + id));

        mealBooking.setState(state);

        return mealBooking;
    }

    @Override
    @Transactional
    public MealBooking save(MealBooking mealBooking) {
        for (MealBooking booking : mealBookingRepository.findAll()) {
            if (booking.getUserId().equals(mealBooking.getUserId())
                    && booking.getDate().equals(mealBooking.getDate())
                    && booking.getState() == ReservationState.ACTIVE) {
                throw new IllegalStateException("User already has an active booking for this date.");
            }
        }
        return mealBookingRepository.save(mealBooking);
    }

    @Override
    public List<MealBooking> getAllMealBookings() {
        return mealBookingRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteMealBooking(MealBooking mealBooking) {
        MealBooking mb = mealBookingRepository.findById(mealBooking.getId())
                .orElseThrow(() -> new EntityNotFoundException("MealBooking not found with id " + mealBooking.getId()));
        mealBookingRepository.delete(mb);
    }

    @Override
    @Transactional
    public void cancelReservation(MealBooking mealBooking) {
        if (mealBooking.getState() == ReservationState.ACTIVE) {
            changeMealBookingState(mealBooking.getId(), ReservationState.CANCELED);
        } else {
            throw new IllegalStateException("Meal Booking reservation state must be ACTIVE to be canceled");
   
        }
    }

    @Override
    @Transactional
    public void checkIn(MealBooking mealBooking) {
        if (mealBooking.getId() == null) {
            throw new IllegalStateException("Reservation not found.");
        }
        if (mealBooking.getState() != ReservationState.ACTIVE) {
            throw new IllegalStateException("Reservation is not active.");
        }
        mealBooking.setState(ReservationState.USED);
    }
}
