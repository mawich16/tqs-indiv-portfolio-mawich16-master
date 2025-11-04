package tqs.lab3meals.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "mealBooking")
public class MealBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "UserId is mandatory")
    private String userId;

    @Column(nullable = false)
    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    @Column(nullable = false)
    @NotNull(message = "State is mandatory")
    private ReservationState state;

    public MealBooking() {
    }

    public MealBooking(String userId, LocalDate date, ReservationState state) {
        this.userId = userId;
        this.date = date;
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealBooking mb = (MealBooking) o;
        return id.equals(mb.id) && Objects.equals(userId, mb.userId) && Objects.equals(date, mb.date) && Objects.equals(state, mb.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, date, state);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

}
