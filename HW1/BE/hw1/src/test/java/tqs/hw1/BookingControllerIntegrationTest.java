package tqs.hw1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityNotFoundException;
import tqs.hw1.boundary.BookingController;
import tqs.hw1.data.Booking;
import tqs.hw1.data.ReservationState;
import tqs.hw1.dto.BookingRequest;
import tqs.hw1.dto.StateUpdateRequest;
import tqs.hw1.services.BookingService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private BookingService bookingService;

    private Booking testBooking;
    private BookingRequest bookingRequest;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2025, 11, 15, 10, 0);
        testBooking = new Booking("Aveiro", testDate, "1 mattress", 
                                   ReservationState.RECEIVED, 1L);
        
        bookingRequest = new BookingRequest();
        bookingRequest.setMunicipality("Aveiro");
        bookingRequest.setDate(testDate);
        bookingRequest.setDetails("1 mattress");
        bookingRequest.setState(ReservationState.RECEIVED);
        bookingRequest.setUserID(1L);
    }

    @Test
    void whenPostBooking_thenCreateBooking() throws Exception {
        when(bookingService.createBooking(any(), any(), any(), any(), any()))
            .thenReturn(testBooking);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.municipality", is("Aveiro")))
                .andExpect(jsonPath("$.details", is("1 mattress")))
                .andExpect(jsonPath("$.state", is("RECEIVED")));

        verify(bookingService, times(1)).createBooking(any(), any(), any(), any(), any());
    }

    @Test
    void whenPostInvalidBooking_thenBadRequest() throws Exception {
        when(bookingService.createBooking(any(), any(), any(), any(), any()))
            .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetBookingByValidToken_thenReturnBooking() throws Exception {
        Long token = 1L;
        when(bookingService.getBooking(token)).thenReturn(testBooking);

        mockMvc.perform(get("/api/bookings/{token}", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.municipality", is("Aveiro")))
                .andExpect(jsonPath("$.details", is("1 mattress")))
                .andExpect(jsonPath("$.userID", is(1)));

        verify(bookingService, times(1)).getBooking(token);
    }

    @Test
    void whenGetBookingByInvalidToken_thenNotFound() throws Exception {
        Long invalidToken = 999L;
        when(bookingService.getBooking(invalidToken))
            .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/bookings/{token}", invalidToken))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBooking(invalidToken);
    }

    @Test
    void whenGetAllBookings_thenReturnJsonArray() throws Exception {
        Booking booking2 = new Booking("Porto", testDate.plusDays(1), 
                                       "2 mattresses", ReservationState.ASSIGNED, 2L);
        List<Booking> allBookings = Arrays.asList(testBooking, booking2);
        when(bookingService.getAllBookings()).thenReturn(allBookings);

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].municipality", is("Aveiro")))
                .andExpect(jsonPath("$[1].municipality", is("Porto")));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void whenGetBookingsByUser_thenReturnUserBookings() throws Exception {
        Long userID = 1L;
        List<Booking> userBookings = Arrays.asList(testBooking);
        when(bookingService.getBookingsByUserID(userID)).thenReturn(userBookings);

        mockMvc.perform(get("/api/bookings/user/{userID}", userID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userID", is(1)));

        verify(bookingService, times(1)).getBookingsByUserID(userID);
    }

    @Test
    void whenGetBookingsByStaff_thenReturnStaffBookings() throws Exception {
        Long staffID = 10L;
        testBooking.setStaffID(staffID);
        List<Booking> staffBookings = Arrays.asList(testBooking);
        when(bookingService.getBookingsByStaffID(staffID)).thenReturn(staffBookings);

        mockMvc.perform(get("/api/bookings/staff/{staffID}", staffID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].staffID", is(10)));

        verify(bookingService, times(1)).getBookingsByStaffID(staffID);
    }

    @Test
    void whenUpdateBookingState_thenReturnUpdatedBooking() throws Exception {
        Long token = 1L;
        StateUpdateRequest stateRequest = new StateUpdateRequest();
        stateRequest.setState(ReservationState.INPROGRESS);
        
        testBooking.setState(ReservationState.INPROGRESS);
        when(bookingService.updateBookingState(eq(token), any(ReservationState.class)))
            .thenReturn(testBooking);

        mockMvc.perform(put("/api/bookings/{token}/state", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state", is("INPROGRESS")));

        verify(bookingService, times(1)).updateBookingState(eq(token), any());
    }

    @Test
    void whenUpdateNonExistentBookingState_thenNotFound() throws Exception {
        Long invalidToken = 999L;
        StateUpdateRequest stateRequest = new StateUpdateRequest();
        stateRequest.setState(ReservationState.ASSIGNED);
        
        when(bookingService.updateBookingState(eq(invalidToken), any()))
            .thenThrow(new EntityNotFoundException());

        mockMvc.perform(put("/api/bookings/{token}/state", invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteBooking_thenNoContent() throws Exception {
        Long token = 1L;
        doNothing().when(bookingService).deleteBooking(token);

        mockMvc.perform(delete("/api/bookings/{token}", token))
                .andExpect(status().isNoContent());

        verify(bookingService, times(1)).deleteBooking(token);
    }

    @Test
    void whenDeleteNonExistentBooking_thenNotFound() throws Exception {
        Long invalidToken = 999L;
        doThrow(new EntityNotFoundException()).when(bookingService).deleteBooking(invalidToken);

        mockMvc.perform(delete("/api/bookings/{token}", invalidToken))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).deleteBooking(invalidToken);
    }

    @Test
    void whenFindByMunicipality_thenReturnBookingsFromThatMunicipality() throws Exception {
            String municipality = "Aveiro";
            when(bookingService.getBookingsByMunicipality(municipality))
                .thenReturn(Arrays.asList(testBooking));

            mockMvc.perform(get("/api/bookings/municipality/{municipality}", municipality))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].municipality", is("Aveiro")));

            verify(bookingService, times(1)).getBookingsByMunicipality(municipality);
    }

    @Test
    void whenFindByState_thenReturnBookingsFromThatState() throws Exception {
        ReservationState state = ReservationState.RECEIVED;
        when(bookingService.getBookingsByState(state))
            .thenReturn(Arrays.asList(testBooking));

        mockMvc.perform(get("/api/bookings/state/{state}", state))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].state", is("RECEIVED")));

        verify(bookingService, times(1)).getBookingsByState(state);

    }

}