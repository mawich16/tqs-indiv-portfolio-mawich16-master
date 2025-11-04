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
import useNewAccount from "./hooks";
import { Checkbox } from "@/components/ui/checkbox";

export default function NewAccount() {
  const { form, onSubmit, isPending, error } = useNewAccount();

  return (
    <Card className="w-full p-8 mx-auto mt-20">
      <CardHeader className="mb-8 text-2xl font-bold text-center">
        New Account
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
                  <FormMessage />
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
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="isStaff"
              render={({ field }) => (
                <FormItem className="flex flex-row">
                  <FormControl>
                    <Checkbox
                      name={field.name}
                      checked={field.value?.valueOf() || false}
                      onCheckedChange={(checked) => {
                        if (checked) {
                          form.setValue("isStaff", true);
                        } else {
                          form.setValue("isStaff", false);
                        }
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                  <FormLabel>New Staff Account</FormLabel>
                </FormItem>
              )}
            />
            <div className="flex justify-between">
              <Button type="submit" variant={"default"} disabled={isPending}>
                {isPending ? "Creating account..." : "Create Account"}
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
