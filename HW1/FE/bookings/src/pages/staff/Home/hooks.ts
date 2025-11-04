import type { ReservationState } from "@/services/dataTypes";
import {
  getBookings,
  getBookingsByMunicipality,
  getBookingsByState,
} from "@/services/endpoints";
import { getMunicipalities } from "@/services/municipiosApi";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function useStaffHome(id: number) {
  const navigate = useNavigate();

  const [filter, setFilter] = useState<string>("all");
  const [filterOption, setFilterOption] = useState<string | undefined>(
    undefined
  );

  const { data: municipalities } = useQuery({
    queryKey: ["municipalities"],
    queryFn: getMunicipalities,
    enabled: filter === "municipality",
  });

  const { data, error, isLoading } = useQuery({
    queryKey: ["bookings"],
    queryFn: () => {
      if (filter === "state") {
        return getBookingsByState(filterOption as ReservationState).then(
          (bookings) => bookings
        );
      } else if (filter === "municipality" && filterOption) {
        return getBookingsByMunicipality(filterOption).then(
          (bookings) => bookings
        );
      } else {
        return getBookings();
      }
    },
    enabled: filter !== "none" && filterOption !== "",
  });

  function onClick(bookingId: number) {
    navigate(`/staff/booking-details/${bookingId}/${id}`);
  }

  const badgeColor = (state: ReservationState) => {
    return state === "INPROGRESS"
      ? "bg-blue-500"
      : state === "RECEIVED"
      ? "bg-black"
      : state === "CANCELED"
      ? "bg-red-500"
      : "bg-green-500";
  };

  return {
    navigate,
    data,
    error,
    isLoading,
    onClick,
    badgeColor,
    municipalities,
    setFilter,
    setFilterOption,
    filter,
    filterOption,
  };
}
