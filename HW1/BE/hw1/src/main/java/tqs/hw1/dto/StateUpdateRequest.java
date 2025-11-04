package tqs.hw1.dto;

import tqs.hw1.data.ReservationState;

public class StateUpdateRequest {
    private ReservationState state;

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }
}
