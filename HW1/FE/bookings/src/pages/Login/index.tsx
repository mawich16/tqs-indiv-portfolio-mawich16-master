import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";
import useLogin from "./hooks";

export default function Login() {
  const { form, onSubmit, isPending, error, navigate } = useLogin();

  return (
    <Card className="w-full p-8 mx-auto mt-20">
      <CardHeader className="mb-8 text-2xl font-bold text-center">
        Login
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Username</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage className="text-left" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input type="password" {...field} />
                  </FormControl>
                  <FormMessage className="text-left" />
                </FormItem>
              )}
            />
            <div className="flex justify-between">
              <Button type="submit" variant={"default"} disabled={isPending}>
                {isPending ? "Logging in..." : "Login"}
              </Button>
              <Button
                variant="secondary"
                onClick={() => navigate("/new-account")}
              >
                New Account
              </Button>
            </div>
          </form>
        </Form>
      </CardContent>
      {error && (
        <CardFooter className="text-red-600">Error: {error.message}</CardFooter>
      )}
    </Card>
  );
}
