import api from "./api";
import type {
  PostLogin,
  GetBooking,
  PostBooking,
  PostUser,
  PostUserResponse,
  PutBookingState,
  ReservationState,
} from "./dataTypes";

export const login = (data: PostLogin): Promise<PostUserResponse> =>
  api.post("/users/login", data).then((response) => response.data);

export const postUser = (data: PostUser): Promise<PostUserResponse> =>
  api.post("/users", data).then((response) => response.data);

export const postBooking = (data: PostBooking): Promise<GetBooking> =>
  api.post("/bookings", data).then((response) => response.data);

export const getBookings = (): Promise<GetBooking[]> =>
  api.get("/bookings").then((response) => response.data);

export const putBookingState = (
  state: ReservationState,
  id: number
): Promise<GetBooking> =>
  api
    .put(`/bookings/${id}/state`, { state } as PutBookingState)
    .then((response) => response.data);

export const getBookingById = (id: number): Promise<GetBooking> =>
  api.get(`/bookings/${id}`).then((response) => response.data);

export const getBookingsByUserId = (userID: number): Promise<GetBooking[]> =>
  api.get(`/bookings/user/${userID}`).then((response) => response.data);

export const getBookingsByStaffId = (staffID: number): Promise<GetBooking[]> =>
  api.get(`/bookings/staff/${staffID}`).then((response) => response.data);

export const getBookingsByState = (
  state: ReservationState
): Promise<GetBooking[]> =>
  api.get(`/bookings/state/${state}`).then((response) => response.data);

export const getBookingsByMunicipality = (
  municipality: string
): Promise<GetBooking[]> =>
  api
    .get(`/bookings/municipality/${municipality}`)
    .then((response) => response.data);

export const deleteBooking = (id: number): Promise<void> =>
  api.delete(`/bookings/${id}`).then(() => {});
