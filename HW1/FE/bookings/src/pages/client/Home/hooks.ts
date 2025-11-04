import type { GetBooking, ReservationState } from "@/services/dataTypes";
import { getBookingsByUserId } from "@/services/endpoints";
import { getMunicipalities } from "@/services/municipiosApi";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function useHome(id: number) {
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
    queryKey: ["bookings", id, filter],
    queryFn: () => getBookingsByUserId(id),
  });

  function onClick(bookingId: number) {
    navigate(`/client/booking-details/${bookingId}`);
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

  const filteredData = () => {
    if (data) {
      if (filter === "all") return data;
      return data.filter(
        (item) => item[filter as keyof GetBooking] === filterOption
      );
    }

    return [];
  };

  return {
    navigate,
    filteredData,
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
