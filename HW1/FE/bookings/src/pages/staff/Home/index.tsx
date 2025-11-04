import { Button } from "@/components/ui/button";
import {
  Item,
  ItemContent,
  ItemTitle,
  ItemDescription,
  ItemActions,
} from "@/components/ui/item";
import useStaffHome from "./hooks";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectTrigger,
  SelectValue,
  SelectContent,
  SelectGroup,
  SelectItem,
} from "@/components/ui/select";
import { useParams } from "react-router-dom";

export default function StaffHome() {
  const { id } = useParams<{ id: string }>();

  const {
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
    navigate,
  } = useStaffHome(Number(id));

  return (
    <>
      <h2 className="scroll-m-20 border-b pb-2 text-3xl font-semibold tracking-tight first:mt-0">
        My Bookings
      </h2>
      {isLoading && (
        <p className="leading-7 not-first:mt-6">Loading bookings...</p>
      )}
      {error && (
        <p className="leading-7 not-first:mt-6 text-red-600">
          I'm sorry, an error occurred while fetching your bookings.
        </p>
      )}
      {data?.length === 0 && !isLoading && !error && (
        <p className="leading-7 not-first:mt-6">You have no bookings.</p>
      )}
      {data && data?.length > 0 && (
        <div className="flex flex-col">
          <div className="flex gap-4">
            <Select
              value={filter}
              onValueChange={(value) => {
                setFilter(value);
                setFilterOption(undefined);
              }}
            >
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="Filter by..." />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem value="all">See All</SelectItem>
                  <SelectItem value="state">State</SelectItem>
                  <SelectItem value="municipality">Municipality</SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>
            {filter !== "all" && (
              <Select
                value={filterOption}
                onValueChange={(value) => {
                  setFilterOption(value);
                }}
              >
                <SelectTrigger className="w-[180px]">
                  <SelectValue placeholder="Select an option..." />
                </SelectTrigger>
                <SelectContent className="max-h-[300px]">
                  <SelectGroup>
                    {filter === "state" &&
                      ["RECEIVED", "ASSIGNED", "CANCELED", "INPROGRESS"].map(
                        (state) => (
                          <SelectItem key={state} value={state}>
                            {state}
                          </SelectItem>
                        )
                      )}
                    {filter === "municipality" &&
                      municipalities?.map((municipality) => (
                        <SelectItem key={municipality} value={municipality}>
                          {municipality}
                        </SelectItem>
                      ))}
                  </SelectGroup>
                </SelectContent>
              </Select>
            )}
          </div>
          <div className="flex flex-col scrollbar-hide overflow-y-auto">
            {data.map((booking, index) => (
              <Item
                variant="muted"
                key={"booking" + index}
                className="mt-4"
                onClick={() => onClick(booking.token)}
              >
                <ItemContent>
                  <ItemTitle>{booking.municipality}</ItemTitle>
                  <ItemDescription className="w-fit">
                    {booking.details}
                  </ItemDescription>
                </ItemContent>
                <ItemActions>
                  <Badge
                    variant="secondary"
                    className={`${badgeColor(booking.state)} text-white`}
                  >
                    {booking.state}
                  </Badge>
                </ItemActions>
              </Item>
            ))}
          </div>
        </div>
      )}
      <Button
        className="fixed bottom-2 left-2"
        variant="secondary"
        onClick={() => navigate("/")}
      >
        Logout
      </Button>
    </>
  );
}
