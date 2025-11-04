import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery } from "@tanstack/react-query";
import { postBooking } from "@/services/endpoints";
import type { PostBooking, GetBooking } from "@/services/dataTypes";
import { getMunicipalities } from "@/services/municipiosApi";
import { useNavigate } from "react-router-dom";

export default function useNewBooking(id: number) {
  const navigate = useNavigate();

  const { error, mutate, isPending } = useMutation<
    GetBooking,
    Error,
    PostBooking
  >({
    mutationFn: (payload: PostBooking) => postBooking(payload),
  });

  const { data: municipalities } = useQuery({
    queryKey: ["municipalities"],
    queryFn: getMunicipalities,
  });

  const formSchema = z.object({
    municipality: z.string().min(1, {
      message: "Municipality cannot be empty",
    }),
    details: z.string().min(1, {
      message: "Please provide details about the service",
    }),
    weight: z
      .number()
      .min(1, {
        message: "Introduce the total weight value of the objects",
      })
      .max(300, {
        message: "Services above 300kg aren't available",
      }),
  });

  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      municipality: "",
      details: "",
      weight: 0,
    },
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    mutate({
      date: new Date(),
      details: `${values.details} ${values.weight}kg`,
      municipality: values.municipality,
      state: "RECEIVED",
      userID: id,
    });
    navigate(`/client/home/${id}`);
  }
  return { error, form, isPending, onSubmit, municipalities };
}
