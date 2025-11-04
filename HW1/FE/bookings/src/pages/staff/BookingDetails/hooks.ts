import type { GetBooking, ReservationState } from "@/services/dataTypes";
import { getBookingById, putBookingState } from "@/services/endpoints";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function useStaffBookingDetails(id: number) {
  const navigate = useNavigate();
  const [state, setState] = useState<ReservationState | undefined>(undefined);

  const { data, error } = useQuery({
    queryKey: ["booking", id],
    queryFn: () => {
      return getBookingById(id).then((res) => {
        setState(res.state);
        return res;
      });
    },
  });

  const { mutate } = useMutation<
    GetBooking,
    Error,
    {
      state: ReservationState;
      id: number;
    }
  >({
    mutationFn: (payload: { state: ReservationState; id: number }) =>
      putBookingState(payload.state, id),
  });

  return { data, error, setState, state, mutate, navigate };
}
