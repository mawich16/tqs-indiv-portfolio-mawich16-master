package tqs.lab3meals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import tqs.lab3meals.data.MealBooking;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.data.ReservationState;

import java.time.LocalDate;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "application-integrationtest.properties")
class MealsBookingITTest {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private MealBookingRepository repo;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomServerPort;

        repo.save(new MealBooking("1", LocalDate.of(2025, 10, 20), ReservationState.ACTIVE));
    }

    @AfterEach
    void resetDb() {
        repo.deleteAll();
    }

    @Test
     void whenCreateMealBookingWithSuccess() {
        MealBooking booking2 = new MealBooking("2", LocalDate.of(2025, 10, 21), ReservationState.USED);

        given()
            .contentType(ContentType.JSON)
            .body(booking2)
        .when()
            .post("/api/bookings")
        .then()
            .statusCode(201);

        long count = repo.findAll().stream()
                .filter(b -> b.getUserId().equals("2"))
                .count();
        
        assert count == 1;
    }

    @Test
    void givenMealBookings_whenGetMealBookings_thenStatus200()  {
        MealBooking booking2 = new MealBooking("2", LocalDate.of(2025, 10, 21), ReservationState.USED);
        MealBooking booking3 = new MealBooking("3", LocalDate.of(2025, 10, 20), ReservationState.ACTIVE);
        
        repo.save(booking2);
        repo.save(booking3);

       given()
        .when()
            .get("/api/bookings")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", equalTo(3))
            .body("userId", hasItems("1", "2", "3"));
    }
}
