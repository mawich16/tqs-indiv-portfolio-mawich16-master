package tqs.lab3meals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.data.ReservationState;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource( locations = "application-integrationtest.properties")
class MealsBookingITTest {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MealBookingRepository repo;

    @BeforeEach
    void setUp() {
        repo.save(new MealBooking("1", LocalDate.of(2025, 10, 20), ReservationState.ACTIVE));
    }

    @AfterEach
    void resetDb() {
        repo.deleteAll();
    }

    @Test
     void whenCreateMealBookingWithSuccess() {
        MealBooking booking2 = new MealBooking("2", LocalDate.of(2025, 10, 21), ReservationState.USED);

        restTemplate.postForEntity("/api/bookings", booking2, MealBooking.class);

        List<MealBooking> found = repo.findAll();
        assertThat(found).extracting(MealBooking::getUserId).contains("2");
    }

    @Test
    void givenMealBookings_whenGetMealBookings_thenStatus200()  {
        MealBooking booking2 = new MealBooking("2", LocalDate.of(2025, 10, 21), ReservationState.USED);
        MealBooking booking3 = new MealBooking("3", LocalDate.of(2025, 10, 20), ReservationState.ACTIVE);
        
        repo.save(booking2);
        repo.save(booking3);

        ResponseEntity<List<MealBooking>> response = restTemplate
                .exchange("/api/bookings", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(MealBooking::getUserId).containsExactly("1", "2", "3");

    }
}
