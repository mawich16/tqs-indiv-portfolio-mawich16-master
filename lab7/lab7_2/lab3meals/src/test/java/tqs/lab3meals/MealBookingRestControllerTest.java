package tqs.lab3meals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import tqs.lab3meals.boundary.MealBookingRestController;
import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.ReservationState;
import tqs.lab3meals.services.MealBookingService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MealBookingRestController.class)
class MealBookingRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    MealBookingService service;

    //save
    @Test
    void whenPostBooking_thenCreateBooking() throws Exception {
        MealBooking booking =  new MealBooking("1", LocalDate.now() , ReservationState.ACTIVE);

        when(service.save(Mockito.any())).thenReturn(booking);

        mvc.perform(
            post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(booking)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId", is("1")));

        verify(service, times(1)).save(Mockito.any());
    }

    //getAllMealBookings
    @Test
    void givenManyBookings_whenGetBookings_thenReturnJsonArray() throws Exception {
        MealBooking mb1 = new MealBooking("1", LocalDate.now(), ReservationState.ACTIVE);
        MealBooking mb2 = new MealBooking("2", LocalDate.now(), ReservationState.ACTIVE);
        MealBooking mb3 = new MealBooking("3", LocalDate.now(), ReservationState.ACTIVE);

        List<MealBooking> allBookings = Arrays.asList(mb1, mb2, mb3);

        when(service.getAllMealBookings()).thenReturn(allBookings);

        mvc.perform(
                get("/api/bookings").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(mb1.getUserId())))
                .andExpect(jsonPath("$[1].userId", is(mb2.getUserId())))
                .andExpect(jsonPath("$[2].userId", is(mb3.getUserId())));

        verify(service, times(1)).getAllMealBookings();
    }

    //getMealBookingDetails
    @Test
    void whenGetBookingById_thenReturnBooking() throws Exception {
        MealBooking booking = new MealBooking("1", LocalDate.now(), ReservationState.ACTIVE);
        booking.setId(1L);

        when(service.getMealBookingDetails(1L)).thenReturn(Optional.of(booking));

        mvc.perform(
            get("/api/bookings/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(booking.getUserId())));

        verify(service, times(1)).getMealBookingDetails(1L);
    }

    @Test
    void whenGetBookingById_withInvalidId_thenReturnNotFound() throws Exception {
        when(service.getMealBookingDetails(99L)).thenReturn(Optional.empty());

        mvc.perform(
            get("/api/bookings/99").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(service, times(1)).getMealBookingDetails(99L);
    }

    //deleteMealBooking
    @Test
    void whenDeleteBooking_thenReturnDeletedBooking() throws Exception {
        MealBooking booking = new MealBooking("1", LocalDate.now(), ReservationState.ACTIVE);
        booking.setId(1L);

        when(service.getMealBookingDetails(1L)).thenReturn(Optional.of(booking));
        doNothing().when(service).deleteMealBooking(any(MealBooking.class));

        mvc.perform(
            delete("/api/bookings/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(booking.getUserId())));

        verify(service, times(1)).getMealBookingDetails(1L);
        verify(service, times(1)).deleteMealBooking(any(MealBooking.class));
    }

    //changeMealBookingState
    @Test
    void whenUpdateBookingState_thenReturnUpdatedBooking() throws Exception {
        MealBooking booking = new MealBooking("1", LocalDate.now(), ReservationState.CANCELED);
        booking.setId(1L);

        when(service.changeMealBookingState(1L, ReservationState.CANCELED))
                .thenReturn(booking);

        mvc.perform(
            patch("/api/bookings/1/state").contentType(MediaType.APPLICATION_JSON)
            .content("\"CANCELED\""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(booking.getUserId())))
            .andExpect(jsonPath("$.state", is("CANCELED")));

        verify(service, times(1)).changeMealBookingState(1L, ReservationState.CANCELED);
    }
}