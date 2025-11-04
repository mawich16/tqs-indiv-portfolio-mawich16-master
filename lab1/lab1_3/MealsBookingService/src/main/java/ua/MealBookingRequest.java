package ua;

import java.time.LocalDate;

enum ReservationState {
    ACTIVE,
    CANCELED,
    USED
}

class MealBookingRequest {
    private final String userId;
    private final LocalDate date;
    private ReservationState state;

    public MealBookingRequest(String userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
        this.state = ReservationState.ACTIVE;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }
}
