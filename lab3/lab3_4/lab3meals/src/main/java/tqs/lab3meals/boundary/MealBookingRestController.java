package tqs.lab3meals.boundary;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.ReservationState;
import tqs.lab3meals.services.MealBookingService;

@RestController
@RequestMapping("/api")
public class MealBookingRestController {
    private final MealBookingService mealBookingService;

    public MealBookingRestController(MealBookingService mealBookingService) {
        this.mealBookingService = mealBookingService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<MealBooking> postBooking(@RequestBody MealBooking mealBooking){
        HttpStatus status = HttpStatus.CREATED;
        MealBooking saved = mealBookingService.save(mealBooking);
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<MealBooking> getBookingById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        MealBooking mealBooking = mealBookingService.getMealBookingDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("meal booking not found for id: " + id));
        return ResponseEntity.ok().body(mealBooking);
    }

    @GetMapping(path="/bookings" )
    public List<MealBooking> getAllBookings() {
        return mealBookingService.getAllMealBookings();
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<MealBooking> deleteBookingById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        MealBooking mealBooking = mealBookingService.getMealBookingDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("meal booking not found for id: " + id));
        mealBookingService.deleteMealBooking(mealBooking);
        return ResponseEntity.ok().body(mealBooking); 
    }

    @PatchMapping("/bookings/{id}/state")
    public ResponseEntity<MealBooking> updateBookingState(
            @PathVariable Long id,
            @RequestBody ReservationState state) throws ResourceNotFoundException {

        MealBooking updated = mealBookingService.changeMealBookingState(id, state);
        return ResponseEntity.ok(updated);
    }
}
