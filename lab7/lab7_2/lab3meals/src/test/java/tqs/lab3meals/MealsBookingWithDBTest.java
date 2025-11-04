package tqs.lab3meals;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqs.lab3meals.Lab3mealsApplication;
import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.data.ReservationState;

import java.time.LocalDate;
import java.util.List;

import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = Lab3mealsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "application-integrationtest.properties")
class MealsBookingWithDBTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MealBookingRepository repo;

    @AfterEach
     void resetDb() {
        repo.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateBooking() throws Exception {
        MealBooking booking1 = new MealBooking("1", LocalDate.of(2025, 10, 20), ReservationState.ACTIVE);

        mvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(booking1)))
                .andExpect(status().isCreated());

        List<MealBooking> found = repo.findAll();
        assertThat(found).extracting(MealBooking::getUserId).containsOnly("1");
    }

    @Test
    void givenBookings_whenGetBookings_thenStatus200() throws Exception {
        MealBooking booking1 = new MealBooking("1", LocalDate.of(2025, 10, 20), ReservationState.ACTIVE);
        MealBooking booking2 = new MealBooking("2", LocalDate.of(2025, 10, 21), ReservationState.USED);
        
        repo.save(booking1);
        repo.save(booking2);

        mvc.perform(get("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is("1")))
                .andExpect(jsonPath("$[1].userId", is("2")));
    }

    
}
