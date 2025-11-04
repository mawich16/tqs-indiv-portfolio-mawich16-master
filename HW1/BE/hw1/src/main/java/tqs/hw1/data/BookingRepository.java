package tqs.hw1.data;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByMunicipality(String municipality);
    List<Booking> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Booking> findByState(ReservationState state);
    List<Booking> findByUserID(Long userID);
    List<Booking> findByStaffID(Long staffID);
}