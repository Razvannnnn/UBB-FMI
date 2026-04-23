package ro.mpp2025.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AgeConvertor {
    public static int getAgeFromCNP(String cnp) {
        if (cnp == null || cnp.length() != 13 || !cnp.matches("\\d{13}")) {
            throw new IllegalArgumentException("CNP invalid.");
        }

        // get YY, MM, DD
        int year = 2000 + Integer.parseInt(cnp.substring(1, 3));
        int month = Integer.parseInt(cnp.substring(3, 5));
        int day = Integer.parseInt(cnp.substring(5, 7));

        // get birthdate
        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();

        // calculate age
        return (int) ChronoUnit.YEARS.between(birthDate, today);
    }
}
