import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { login } from "@/services/endpoints";
import { useMutation } from "@tanstack/react-query";
import type { PostLogin, PostUserResponse } from "@/services/dataTypes";
import { useNavigate } from "react-router-dom";

export default function useLogin() {
  const navigate = useNavigate();

  const { data, error, mutate, isPending } = useMutation<
    PostUserResponse,
    Error,
    PostLogin
  >({
    mutationFn: (payload: PostLogin) => login(payload),
  });

  const formSchema = z.object({
    username: z.string().min(1, {
      message: "Username cannot be empty",
    }),
    password: z.string().min(1, {
      message: "Password cannot be empty.",
    }),
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
      username: "",
      password: "",
    },
  });

  return { form, onSubmit, isPending, data, error, navigate };
}
