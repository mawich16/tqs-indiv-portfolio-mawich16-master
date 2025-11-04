import { Button } from "@/components/ui/button";
import useStaffBookingDetails from "./hooks";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import type { ReservationState } from "@/services/dataTypes";
import { useParams } from "react-router-dom";

export default function StaffBookingDetails() {
  const { id, token } = useParams<{ id: string; token: string }>();
  const { data, error, setState, state, mutate, navigate } =
    useStaffBookingDetails(Number(id));
  return (
    <>
      <h2 className="scroll-m-20 border-b pb-2 text-3xl font-semibold tracking-tight first:mt-0">
        Booking Details
      </h2>
      {error && (
        <p className="leading-7 [&:not(:first-child)]:mt-6 text-red-600">
          Error: {error.message}
        </p>
      )}
      {data && !error && (
        <>
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight mt-2">
            Municipality
          </h4>
          <p className="text-left leading-7">{data.municipality}</p>
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight mt-2">
            Date
          </h4>
          <p className="text-left leading-7">
            {`${new Date(data.date).getDay()}-${new Date(
              data.date
            ).getMonth()}-${new Date(data.date).getFullYear()}, ${new Date(
              data.date
            ).getHours()}:${new Date(data.date).getMinutes()}`}
          </p>
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight mt-2">
            State
          </h4>
          <Select
            onValueChange={(value) => setState(value as ReservationState)}
            value={state as string}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="Select an option..." />
            </SelectTrigger>
            <SelectContent className="max-h-[300px]">
              <SelectGroup>
                {["RECEIVED", "ASSIGNED", "CANCELED", "INPROGRESS"]?.map(
                  (state) => (
                    <SelectItem key={state} value={state}>
                      {state}
                    </SelectItem>
                  )
                )}
              </SelectGroup>
            </SelectContent>
          </Select>
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight mt-2">
            Details
          </h4>
          <p className="text-left leading-7 mb-3">{data.details}</p>
          <Button
            variant="default"
            disabled={!state || state == data.state}
            onClick={() => {
              state && mutate({ state, id: Number(token) });
              navigate(`/staff/home/${id}`);
            }}
          >
            Save Changes
          </Button>
        </>
      )}
    </>
  );
}
