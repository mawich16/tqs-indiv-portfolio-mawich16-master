package tqs.lab3meals.services;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MealBookingRepository mealBookingRepository;

    public Optional<MealBooking> getMealBookingDetails(Long id) {
        return mealBookingRepository.findById(id);
    }

    @Transactional
    public MealBooking changeMealBookingState(Long id, ReservationState state) {
        MealBooking mealBooking = mealBookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MealBooking not found with id " + id));

        mealBooking.setState(state);
        mealBookingRepository.save(mealBooking);

        return mealBooking;
    }

    @Override
    public MealBooking save(MealBooking mealBooking) {
        return mealBookingRepository.save(mealBooking);
    }

    @Override
    public List<MealBooking> getAllMealBookings() {
        return mealBookingRepository.findAll();
    }

    @Override
    public void deleteMealBooking(MealBooking mealBooking) {
        mealBookingRepository.delete(mealBooking);
    }
}
