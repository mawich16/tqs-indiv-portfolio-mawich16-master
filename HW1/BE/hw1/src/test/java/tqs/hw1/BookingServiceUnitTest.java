package tqs.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import tqs.hw1.data.Booking;
import tqs.hw1.data.BookingRepository;
import tqs.hw1.data.ReservationState;
import tqs.hw1.services.BookingServiceImp;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImp bookingService;

    private Booking testBooking;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2025, 11, 15, 10, 0);
        testBooking = new Booking("Aveiro", testDate, "3 mattresses", 
                                   ReservationState.RECEIVED, 1L);
    }

    @Test
    void whenCreateBooking_thenBookingIsCreated() {
        String municipality = "Aveiro";
        String details = "3 mattresses";
        
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Booking booking = bookingService.createBooking(
            municipality, testDate, details, ReservationState.RECEIVED, 1L
        );

        verify(bookingRepository).save(argThat(b -> 
            b.getMunicipality().equals(municipality) &&
            b.getDate().equals(testDate) &&
            b.getDetails().equals(details) &&
            b.getState().equals(ReservationState.RECEIVED) &&
            b.getUserID().equals(1L)
        ));
        
        assertThat(booking).isNotNull();
        assertThat(booking.getMunicipality()).isEqualTo(municipality);
        assertThat(booking.getDate()).isEqualTo(testDate);
        assertThat(booking.getDetails()).isEqualTo(details);
        assertThat(booking.getState()).isEqualTo(ReservationState.RECEIVED);
        assertThat(booking.getUserID()).isEqualTo(1L);
    }

    @Test
    void whenGetBookingWithValidToken_thenBookingIsReturned() {
        Long token = 1L;
        when(bookingRepository.findById(token)).thenReturn(Optional.of(testBooking));

        Booking found = bookingService.getBooking(token);

        assertThat(found).isNotNull();
        assertThat(found.getMunicipality()).isEqualTo("Aveiro");
        verify(bookingRepository, times(1)).findById(token);
    }

    @Test
    void whenGetBookingWithInvalidToken_thenThrowException() {
        Long invalidToken = 999L;
        when(bookingRepository.findById(invalidToken)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBooking(invalidToken))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Booking not found with id");
        
        verify(bookingRepository, times(1)).findById(invalidToken);
    }

    @Test
    void whenGetAllBookings_thenReturnAllBookings() {
        Booking booking2 = new Booking("Porto", testDate.plusDays(1), 
                                       "1 mattress", ReservationState.ASSIGNED, 2L);
        List<Booking> allBookings = Arrays.asList(testBooking, booking2);
        when(bookingRepository.findAll()).thenReturn(allBookings);

        List<Booking> found = bookingService.getAllBookings();

        assertThat(found).hasSize(2).containsExactlyInAnyOrder(testBooking, booking2);
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void whenUpdateBookingState_thenStateIsUpdated() {
        Long token = 1L;
        ReservationState newState = ReservationState.INPROGRESS;
        when(bookingRepository.findById(token)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking updated = bookingService.updateBookingState(token, newState);

        assertThat(updated.getState()).isEqualTo(newState);
        verify(bookingRepository, times(1)).findById(token);
        verify(bookingRepository, times(1)).save(testBooking);
    }

    @Test
    void whenUpdateNonExistentBooking_thenThrowException() {
        Long invalidToken = 999L;
        when(bookingRepository.findById(invalidToken)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> 
            bookingService.updateBookingState(invalidToken, ReservationState.ASSIGNED))
            .isInstanceOf(EntityNotFoundException.class);
        
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void whenDeleteBooking_thenBookingIsDeleted() {
        Long token = 1L;
        when(bookingRepository.findById(token)).thenReturn(Optional.of(testBooking));

        bookingService.deleteBooking(token);

        verify(bookingRepository, times(1)).findById(token);
        verify(bookingRepository, times(1)).delete(testBooking);
    }

    @Test
    void whenDeleteNonExistentBooking_thenThrowException() {
        Long invalidToken = 999L;
        when(bookingRepository.findById(invalidToken)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.deleteBooking(invalidToken))
            .isInstanceOf(EntityNotFoundException.class);
        
        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void whenGetBookingsByUserID_thenReturnUserBookings() {
        Long userID = 1L;
        Booking booking2 = new Booking("Lisboa", testDate.plusDays(2), 
                                       "2 mattresses", ReservationState.RECEIVED, userID);
        List<Booking> userBookings = Arrays.asList(testBooking, booking2);
        when(bookingRepository.findByUserID(userID)).thenReturn(userBookings);

        List<Booking> found = bookingService.getBookingsByUserID(userID);

        assertThat(found).hasSize(2).allMatch(b -> b.getUserID().equals(userID));
        verify(bookingRepository, times(1)).findByUserID(userID);
    }

    @Test
    void whenGetBookingsByStaffID_thenReturnStaffBookings() {
        Long staffID = 10L;
        testBooking.setStaffID(staffID);
        List<Booking> staffBookings = Arrays.asList(testBooking);
        when(bookingRepository.findByStaffID(staffID)).thenReturn(staffBookings);

        List<Booking> found = bookingService.getBookingsByStaffID(staffID);

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getStaffID()).isEqualTo(staffID);
        verify(bookingRepository, times(1)).findByStaffID(staffID);
    }

    @Test
    void whenGetBookingsByStaffIDWithNoAssignments_thenReturnEmptyList() {
        Long staffID = 10L;
        when(bookingRepository.findByStaffID(staffID)).thenReturn(Arrays.asList());

        List<Booking> found = bookingService.getBookingsByStaffID(staffID);

        assertThat(found).isEmpty();
        verify(bookingRepository, times(1)).findByStaffID(staffID);
    }
}