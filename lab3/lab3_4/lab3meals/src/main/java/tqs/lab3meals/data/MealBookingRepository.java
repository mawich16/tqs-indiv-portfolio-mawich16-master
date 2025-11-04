package tqs.lab3meals.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface MealBookingRepository extends JpaRepository<MealBooking, Long> {

    public Optional<MealBooking> findById(Long id);
    public List<MealBooking> findAll();
    public void delete(MealBooking mb);

}
