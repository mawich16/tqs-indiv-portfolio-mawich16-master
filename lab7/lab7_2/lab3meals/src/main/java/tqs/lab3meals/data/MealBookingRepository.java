package tqs.lab3meals.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MealBookingRepository extends JpaRepository<MealBooking, Long> {

    // methods like findById() findAll() delete() save() are already provided by JpaRepository
    @Query("SELECT mb FROM MealBooking mb WHERE mb.state = :state")
    List<MealBooking> findByState(@Param("state") ReservationState state);
}
