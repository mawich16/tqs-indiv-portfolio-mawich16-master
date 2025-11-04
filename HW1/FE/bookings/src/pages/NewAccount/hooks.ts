import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { postUser } from "@/services/endpoints";
import { useMutation } from "@tanstack/react-query";
import type { PostUser, PostUserResponse } from "@/services/dataTypes";
import { useNavigate } from "react-router-dom";

export default function useNewAccount() {
  const navigate = useNavigate();
  const { data, error, mutate, isPending } = useMutation<
    PostUserResponse,
    Error,
    PostUser
  >({
    mutationFn: (payload: PostUser) => postUser(payload),
  });

  const formSchema = z.object({
    username: z.string().min(1, {
      message: "Username cannot be empty",
    }),
    password: z.string().min(1, {
      message: "Password cannot be empty.",
    }),
    isStaff: z.boolean(),
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    mutate(values);
    data &&
      (data?.isStaff
        ? navigate(`/staff/home/${data.id}`)
        : navigate(`/client/home/${data.id}`));
  }

  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      isStaff: false,
      username: "",
      password: "",
    },
  });

  return { form, onSubmit, isPending, data, error, navigate };
}
