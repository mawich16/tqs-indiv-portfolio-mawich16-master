import { Button } from "@/components/ui/button";
import {
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
  Form,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import useNewBooking from "./hooks";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { useParams } from "react-router-dom";

export default function NewBooking() {
  const { id } = useParams<{ id: string }>();
  const { error, form, isPending, onSubmit, municipalities } = useNewBooking(
    Number(id)
  );
  return (
    <>
      <h2 className="scroll-m-20 border-b pb-2 text-3xl font-semibold tracking-tight first:mt-0">
        New Booking
      </h2>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 mt-2">
          <FormField
            control={form.control}
            name="municipality"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Municipality</FormLabel>
                <FormControl>
                  <Select
                    {...field}
                    onValueChange={(value) =>
                      form.setValue("municipality", value)
                    }
                  >
                    <SelectTrigger className="w-full">
                      <SelectValue placeholder="Select an option..." />
                    </SelectTrigger>
                    <SelectContent className="max-h-[300px]">
                      <SelectGroup>
                        {municipalities?.map((municipality) => (
                          <SelectItem key={municipality} value={municipality}>
                            {municipality}
                          </SelectItem>
                        ))}
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                </FormControl>
                <FormMessage className="text-left" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="weight"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Estimated Weight</FormLabel>
                <FormControl>
                  <Input
                    type="number"
                    min={0}
                    value={field.value ?? ""}
                    onChange={(e) =>
                      field.onChange(
                        e.target.value === ""
                          ? undefined
                          : Number(e.target.value)
                      )
                    }
                  />
                </FormControl>
                <FormMessage className="text-left" />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="details"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Details</FormLabel>
                <FormControl>
                  <Textarea
                    placeholder="Provide more details about the service..."
                    {...field}
                  />
                </FormControl>
                <FormMessage className="text-left" />
              </FormItem>
            )}
          />
          <div className="flex justify-between">
            <Button
              variant="secondary"
              onClick={() => {
                form.reset();
              }}
            >
              Clear
            </Button>
            <Button type="submit" variant={"default"} disabled={isPending}>
              {isPending ? "Sending..." : "Send"}
            </Button>
          </div>
        </form>
      </Form>
      {error && (
        <p className="leading-7 [&:not(:first-child)]:mt-6 text-red-600">
          Error: {error.message}
        </p>
      )}
    </>
  );
}
