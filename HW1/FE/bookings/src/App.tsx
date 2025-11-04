import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import Login from "./pages/Login";
import NewAccount from "./pages/NewAccount";
import Home from "./pages/client/Home";
import StaffHome from "./pages/staff/Home";
import NewBooking from "./pages/client/NewBooking";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import BookingDetails from "./pages/client/BookingDetails";
import StaffBookingDetails from "./pages/staff/BookingDetails";

function App() {
  const internalClient = new QueryClient();

  return (
    <QueryClientProvider client={internalClient}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/new-account" element={<NewAccount />} />
          <Route path="/client/home/:id" element={<Home />} />
          <Route
            path="/client/booking-details/:id"
            element={<BookingDetails />}
          />
          <Route path="/client/new-booking/:id" element={<NewBooking />} />
          <Route path="/staff/home/:id" element={<StaffHome />} />
          <Route
            path="/staff/booking-details/:id/:token"
            element={<StaffBookingDetails />}
          />
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
