export type ReservationState =
  | "RECEIVED"
  | "ASSIGNED"
  | "CANCELED"
  | "INPROGRESS";

export interface PostUser {
  username: string;
  password: string;
  isStaff: boolean;
}

export interface PostUserResponse {
  username: string;
  password: string;
  isStaff: boolean;
  id: number;
}

export interface PostLogin {
  username: string;
  password: string;
}

export interface PutBookingState {
  state: ReservationState;
}

export interface GetBooking {
  token: number;
  municipality: string;
  date: string;
  state: ReservationState;
  details: string;
  userID: number;
  staffID?: number;
}

export interface PostBooking {
  municipality: string;
  date: Date;
  state: ReservationState;
  details: string;
  userID: number;
}
