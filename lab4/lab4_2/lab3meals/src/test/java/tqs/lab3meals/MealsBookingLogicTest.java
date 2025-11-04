package tqs.lab3meals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.data.ReservationState;
import tqs.lab3meals.services.MealBookingService;
import tqs.lab3meals.services.MealBookingServiceImpl;
import jakarta.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.*;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MealsBookingLogicTest {
    
    @Mock
    private MealBookingRepository repo;

    @InjectMocks
    private MealBookingServiceImpl service;

    private MealBooking booking1;
    private MealBooking booking2;
    private MealBooking booking3;

    @BeforeEach
    void setUp() {
        booking1 = new MealBooking("1", LocalDate.of(2025, 10, 20),ReservationState.ACTIVE );
        booking1.setId(1L);

        booking2 = new MealBooking("2",LocalDate.of(2025, 10, 21),ReservationState.USED );
        booking2.setId(2L);

        booking3 = new MealBooking("3",LocalDate.of(2025, 10, 22), ReservationState.CANCELED );
        booking3.setId(3L);

    }

    @Test
    void whenGetMealBookingDetails_withValidId_thenReturnBooking() {
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(booking1));

        Optional<MealBooking> found = service.getMealBookingDetails(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
        assertThat(found.get().getUserId()).isEqualTo("1");
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void whenGetMealBookingDetails_withInvalidId_thenReturnEmpty() {
        Mockito.when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<MealBooking> found = service.getMealBookingDetails(99L);

        assertThat(found).isEmpty();
        verify(repo, times(1)).findById(99L);
    }

    @Test
    void whenChangeMealBookingState_withValidId_thenUpdateState() {
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(booking1));
        Mockito.when(repo.save(any(MealBooking.class))).thenReturn(booking1);

        MealBooking updated = service.changeMealBookingState(1L, ReservationState.USED);

        assertThat(updated.getState()).isEqualTo(ReservationState.USED);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void whenChangeMealBookingState_withInvalidId_thenThrowException() {
        Mockito.when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.changeMealBookingState(99L, ReservationState.USED))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("MealBooking not found with id 99");

        verify(repo, times(1)).findById(99L);
        verify(repo, never()).save(any(MealBooking.class));
    }

    @Test
    void whenSaveNewBooking_withNoDuplicates_thenSaveSuccessfully() {
        MealBooking newBooking = new MealBooking();
        newBooking.setUserId("user999");
        newBooking.setDate(LocalDate.of(2025, 10, 25));
        newBooking.setState(ReservationState.ACTIVE);

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(booking1, booking2));
        Mockito.when(repo.save(any(MealBooking.class))).thenReturn(newBooking);

        MealBooking saved = service.save(newBooking);

        assertThat(saved).isNotNull();
        verify(repo, times(1)).findAll();
        verify(repo, times(1)).save(newBooking);
    }

    @Test
    void whenSaveBooking_withDuplicateActiveBooking_thenThrowException() {
        MealBooking duplicateBooking = new MealBooking();
        duplicateBooking.setUserId("1");
        duplicateBooking.setDate(LocalDate.of(2025, 10, 20));
        duplicateBooking.setState(ReservationState.ACTIVE);

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        assertThatThrownBy(() -> service.save(duplicateBooking))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User already has an active booking for this date");

        verify(repo, times(1)).findAll();
        verify(repo, never()).save(any(MealBooking.class));
    }

    @Test
    void whenSaveBooking_withSameUserAndDateButDifferentState_thenSaveSuccessfully() {
        MealBooking newBooking = new MealBooking();
        newBooking.setUserId("user789");
        newBooking.setDate(LocalDate.of(2025, 10, 22));
        newBooking.setState(ReservationState.ACTIVE);

        // booking3 has same user and date but is CANCELED
        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(booking1, booking2, booking3));
        Mockito.when(repo.save(any(MealBooking.class))).thenReturn(newBooking);

        MealBooking saved = service.save(newBooking);

        assertThat(saved).isNotNull();
        verify(repo, times(1)).findAll();
        verify(repo, times(1)).save(newBooking);
    }

    @Test
    void whenGetAllMealBookings_thenReturnAllBookings() {
        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(booking1, booking2, booking3));

        List<MealBooking> bookings = service.getAllMealBookings();

        assertThat(bookings).hasSize(3);
        assertThat(bookings).containsExactly(booking1, booking2, booking3);
        verify(repo, times(1)).findAll();
    }

    @Test
    void whenDeleteMealBooking_withValidId_thenDeleteSuccessfully() {
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(booking1));
        doNothing().when(repo).delete(booking1);

        service.deleteMealBooking(booking1);

        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).delete(booking1);
    }

    @Test
    void whenDeleteMealBooking_withInvalidId_thenThrowException() {
        MealBooking nonExistentBooking = new MealBooking();
        nonExistentBooking.setId(99L);

        Mockito.when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteMealBooking(nonExistentBooking))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("MealBooking not found with id 99");

        verify(repo, times(1)).findById(99L);
        verify(repo, never()).delete(any(MealBooking.class));
    }

    @Test
    void whenCancelReservation_withActiveBooking_thenCancelSuccessfully() {
        booking1.setState(ReservationState.ACTIVE);
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(booking1));
        Mockito.when(repo.save(any(MealBooking.class))).thenReturn(booking1);

        service.cancelReservation(booking1);

        assertThat(booking1.getState()).isEqualTo(ReservationState.CANCELED);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void whenCancelReservation_withNonActiveBooking_thenThrowException() {
        booking2.setState(ReservationState.CANCELED);

        assertThatThrownBy(() -> service.cancelReservation(booking2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Meal Booking reservation state must be ACTIVE to be canceled");

        verify(repo, never()).save(any(MealBooking.class));
    }

    @Test
    void whenCheckIn_withValidActiveBooking_thenMarkAsUsed() {
        booking1.setState(ReservationState.ACTIVE);
        Mockito.when(repo.save(any(MealBooking.class))).thenReturn(booking1);

        service.checkIn(booking1);

        assertThat(booking1.getState()).isEqualTo(ReservationState.USED);
    }

    @Test
    void whenCheckIn_withNullId_thenThrowException() {
        MealBooking bookingWithoutId = new MealBooking();
        bookingWithoutId.setId(null);

        assertThatThrownBy(() -> service.checkIn(bookingWithoutId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Reservation not found");

        verify(repo, never()).save(any(MealBooking.class));
    }

    @Test
    void whenCheckIn_withNonActiveBooking_thenThrowException() {
        booking3.setState(ReservationState.CANCELED);

        assertThatThrownBy(() -> service.checkIn(booking3))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Reservation is not active");

        verify(repo, never()).save(any(MealBooking.class));
    }

    @Test
    void whenCheckIn_withCanceledBooking_thenThrowException() {
        booking2.setState(ReservationState.CANCELED);

        assertThatThrownBy(() -> service.checkIn(booking2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Reservation is not active");

        verify(repo, never()).save(any(MealBooking.class));
    }

    @Test
    void findActiveBookings() {
        booking2.setState(ReservationState.ACTIVE);

        Mockito.when(repo.findByState(ReservationState.ACTIVE))
                .thenReturn(Arrays.asList(booking1, booking2));

        List<MealBooking> results = repo.findByState(ReservationState.ACTIVE);

        assertThat(results)
                .hasSize(2)
                .extracting(MealBooking::getUserId)
                .containsExactlyInAnyOrder(
                        "1",
                        "2"
                );

        verify(repo, times(1)).findByState(ReservationState.ACTIVE);
    }
}
