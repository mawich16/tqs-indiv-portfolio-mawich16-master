package tqs.lab6_2;

import java.time.LocalDate;


public class Utils {
    public static LocalDate localDateFromDateParts(String year, String month, String day) {
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    public static LocalDate isoTextToLocalDate(String date) {
        return LocalDate.of(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]), Integer.parseInt(date.split("-")[2]));
    }


}
