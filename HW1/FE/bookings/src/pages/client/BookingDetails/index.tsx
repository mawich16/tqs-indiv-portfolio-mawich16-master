import { Button } from "@/components/ui/button";
import useBookingDetails from "./hooks";
import { useParams } from "react-router-dom";

export default function BookingDetails() {
  const { id } = useParams<{ id: string }>();
  const { data, error, cancelBooking, navigate } = useBookingDetails(
    Number(id)
  );
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
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight">
            Municipality
          </h4>
          <p className="leading-7">{data.municipality}</p>
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight">
            Date
          </h4>
          <p className="text-left  leading-7">
            {`${new Date(data.date).getDay()}-${new Date(
              data.date
            ).getMonth()}-${new Date(data.date).getFullYear()}, ${new Date(
              data.date
            ).getHours()}:${new Date(data.date).getMinutes()}`}
          </p>
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight">
            State
          </h4>
          <p className="text-left leading-7">{data.state}</p>
          <h4 className="text-left scroll-m-20 text-xl font-semibold tracking-tight">
            Details
          </h4>
          <p className="text-left leading-7 mb-3">{data.details}</p>
          <div className="flex justify-between mt-8">
            <Button
              variant="default"
              onClick={() => {
                cancelBooking(Number(id));
                navigate(`/client/home/${id}`);
              }}
            >
              Go Back
            </Button>
            <Button
              variant="destructive"
              onClick={() => navigate(`/client/home/${id}`)}
            >
              Cancel Booking
            </Button>
          </div>
        </>
      )}
    </>
  );
}
