package tqs.hw1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import tqs.hw1.data.Booking;
import tqs.hw1.data.BookingRepository;
import tqs.hw1.data.ReservationState;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource( locations = "application-integrationtest.properties")
class BookingRepositoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository repo;

    private Booking booking1;
    private Booking booking2;
    private Booking booking3;

    @BeforeEach
    void setUp() {
        booking1 = new Booking("Porto", LocalDateTime.of(2025, 11, 10, 14, 0),
                "1 mattress", ReservationState.ASSIGNED, 1L);
        booking1.setStaffID(10L);

        booking2 = new Booking("Lisboa", LocalDateTime.of(2025, 11, 15, 10, 30),
                "1 chair", ReservationState.CANCELED, 2L);
        booking2.setUserID(2L);
        booking2.setStaffID(10L);

        booking3 = new Booking("Aveiro", LocalDateTime.of(2025, 11, 20, 16, 0),
                "1 table", ReservationState.ASSIGNED, 1L);
        booking3.setStaffID(20L);

        repo.save(booking1);
        repo.save(booking2);
        repo.save(booking3);
    }

    @AfterEach
    void resetDb() {
        repo.deleteAll();
    }

    @Test
    void whenFindByMunicipality_thenReturnBookingList() {
        List<Booking> found = repo.findByMunicipality("Porto");

        assertThat(found).hasSize(1);
        assertThat(found).extracting(Booking::getMunicipality)
                .containsOnly("Porto");
        assertThat(found).contains(booking1);
    }

    @Test
    void whenFindByNonExistentMunicipality_thenReturnEmptyList() {
        List<Booking> found = repo.findByMunicipality("Coimbra");

        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByDateBetween_thenReturnBookingsInRange() {
        LocalDateTime startDate = LocalDateTime.of(2025, 11, 9, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 11, 16, 0, 0);

        List<Booking> found = repo.findByDateBetween(startDate, endDate);

        assertThat(found).hasSize(2).contains(booking1, booking2).doesNotContain(booking3);
    }

    @Test
    void whenFindByDateBetweenNoMatches_thenReturnEmptyList() {
        LocalDateTime startDate = LocalDateTime.of(2025, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 31, 0, 0);

        List<Booking> found = repo.findByDateBetween(startDate, endDate);

        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByState_thenReturnBookingsWithState() {
        List<Booking> assignedBookings = repo.findByState(ReservationState.ASSIGNED);
        List<Booking> canceledBookings = repo.findByState(ReservationState.CANCELED);
        List<Booking> receivedBookings = repo.findByState(ReservationState.RECEIVED);

        assertThat(assignedBookings).hasSize(2).contains(booking1, booking3);
        assertThat(canceledBookings).hasSize(1).contains(booking2);
        assertThat(receivedBookings).isEmpty();
    }

    @Test
    void whenFindByUserID_thenReturnUserBookings() {
        List<Booking> user1Bookings = repo.findByUserID(1L);
        List<Booking> user2Bookings = repo.findByUserID(2L);

        assertThat(user1Bookings).hasSize(2).contains(booking1, booking3);
        assertThat(user2Bookings).hasSize(1).contains(booking2);
    }

    @Test
    void whenFindByNonExistentUserID_thenReturnEmptyList() {
        List<Booking> found = repo.findByUserID(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByStaffID_thenReturnStaffBookings() {
        List<Booking> staff10Bookings = repo.findByStaffID(10L);
        List<Booking> staff20Bookings = repo.findByStaffID(20L);

        assertThat(staff10Bookings).hasSize(2).contains(booking1, booking2);
        assertThat(staff20Bookings).hasSize(1).contains(booking3);
    }

    @Test
    void whenSaveBooking_thenBookingIsPersisted() {
        Booking newBooking = new Booking("Braga", LocalDateTime.of(2025, 11, 25, 11, 0), "1 mattress", ReservationState.RECEIVED, 3L);

        Booking saved = repo.save(newBooking);

        assertThat(saved).isNotNull();
        assertThat(saved.getToken()).isNotNull();
        
        Booking found = restTemplate.getForObject("/api/bookings/" + saved.getToken(), Booking.class);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    void whenDeleteBooking_thenBookingIsRemoved() {
        Long bookingId = booking1.getToken();

        repo.delete(booking1);

        Booking found = restTemplate.getForObject("/api/bookings/" + bookingId, Booking.class);
        assertThat(found).isNull();
    }

    @Test
    void whenFindAll_thenReturnAllBookings() {
        List<Booking> allBookings = repo.findAll();

        assertThat(allBookings).hasSize(3).contains(booking1, booking2, booking3);
    }

    @Test
    void whenFindById_thenReturnBooking() {
        Booking found = repo.findById(booking1.getToken()).orElse(null);

        assertThat(found).isNotNull().isEqualTo(booking1);
        assertThat(found.getMunicipality()).isEqualTo("Porto");
    }
}