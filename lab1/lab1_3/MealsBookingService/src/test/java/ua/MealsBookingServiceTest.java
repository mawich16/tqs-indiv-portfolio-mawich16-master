package ua;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MealsBookingServiceTest {
    private MealsBookingService service;

    @BeforeEach
    void setUp() {
        service = new MealsBookingService(2);
    }

    @Test
    void testBookingSuccess() {
        String code = service.bookMeal(new MealBookingRequest("student1", LocalDate.now()));
        assertNotNull(code);
        assertTrue(service.findReservation(code).isPresent());
    }

    @Test
    void testPreventDoubleBooking() {
        MealBookingRequest request = new MealBookingRequest("student1", LocalDate.now());
        service.bookMeal(request);
        assertThrows(IllegalStateException.class, () -> service.bookMeal(request));
    }

    @Test
    void testBookingDifferentDateSameUser() {
        MealBookingRequest first = new MealBookingRequest("student1", LocalDate.now());
        MealBookingRequest second = new MealBookingRequest("student1", LocalDate.now().plusDays(1));

        service.bookMeal(first);

        assertDoesNotThrow(() -> service.bookMeal(second));
    }

    @Test
    void testBookingSameDateSameUserCanceledState() {
        MealBookingRequest meal = new MealBookingRequest("student1", LocalDate.now());
        String code = service.bookMeal(meal);
        service.cancelReservation(code);

        assertDoesNotThrow(() -> service.bookMeal(meal));
    }

    @Test
    void testCapacityLimit() {
        service.bookMeal(new MealBookingRequest("s1", LocalDate.now()));
        service.bookMeal(new MealBookingRequest("s2", LocalDate.now()));
        assertThrows(IllegalStateException.class,
                () -> service.bookMeal(new MealBookingRequest("s3", LocalDate.now())));
    }

    @Test
    void testCancelReservation() {
        String code = service.bookMeal(new MealBookingRequest("student1", LocalDate.now()));
        service.cancelReservation(code);
        assertEquals(ReservationState.CANCELED, service.findReservation(code).get().getState());
    }

    @Test
    void testCancelReservationFailureForNonExistingCode() {
        assertThrows(IllegalStateException.class, () -> service.cancelReservation("code"));
    }

    @Test
    void testCancelReservationFailureForNonActiveReservation() {
        String code = service.bookMeal(new MealBookingRequest("student1", LocalDate.now()));
        service.checkIn(code);
        assertThrows(IllegalStateException.class, () -> service.cancelReservation(code));
    }

    @Test
    void testCheckInSuccess() {
        String code = service.bookMeal(new MealBookingRequest("student1", LocalDate.now()));
        service.checkIn(code);
        assertEquals(ReservationState.USED, service.findReservation(code).get().getState());
    }

    @Test
    void testCheckInFailure() {
        assertThrows(IllegalStateException.class, () -> service.checkIn("code"));
    }

    @Test
    void testCheckInFailsIfCanceled() {
        String code = service.bookMeal(new MealBookingRequest("student1", LocalDate.now()));
        service.cancelReservation(code);
        assertThrows(IllegalStateException.class, () -> service.checkIn(code));
    }
}
