package tqs.hw1.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long token;

    @Column(nullable = false)
    @NotBlank(message = "municipality is mandatory")
    private String municipality;

    @Column(nullable = false)
    @NotNull(message = "timestamp is mandatory")
    private LocalDateTime date;

    @Column(nullable = false)
    @NotNull(message = "state is mandatory")
    private ReservationState state;

    @Column(nullable = false)
    @NotNull(message = "details is mandatory")
    private String details;

    @Column(nullable = false)
    @NotNull(message = "userID is mandatory")
    private Long userID;

    @Column(nullable = true)
    private Long staffID;

    public Booking() {
    }

    public Booking(String municipality, LocalDateTime date, String details, ReservationState state, Long userID) {
        this.date = date;
        this.state = state;
        this.municipality = municipality;
        this.details = details;
        this.userID = userID;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking sb = (Booking) o;
        return token.equals(sb.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    public Long getToken() {
        return token;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public String getDetails() {
        return details;
    }  

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

}
