import { deleteBooking, getBookingById } from "@/services/endpoints";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";

export default function useBookingDetails(id: number) {
  const navigate = useNavigate();

  const { data, error } = useQuery({
    queryKey: ["booking", id],
    queryFn: () => getBookingById(id),
  });

  const { mutate: cancelBooking } = useMutation<void, Error, number>({
    mutationFn: (id: number) => deleteBooking(id),
  });

  return { data, error, cancelBooking, navigate };
}
