package problema8.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeConverter {

    public static Integer getAgeFromCNP(String cnp) {
        if (cnp == null || cnp.length() != 13 || !cnp.matches("\\d+")) {
            throw new IllegalArgumentException("CNP invalid");
        }

        int s = Character.getNumericValue(cnp.charAt(0));
        int year = Integer.parseInt(cnp.substring(1, 3));
        int month = Integer.parseInt(cnp.substring(3, 5));
        int day = Integer.parseInt(cnp.substring(5, 7));

        int fullYear;
        if (s == 1 || s == 2) {
            fullYear = 1900 + year;
        } else if (s == 5 || s == 6) {
            fullYear = 2000 + year;
        } else {
            throw new IllegalArgumentException("CNP cu secol necunoscut");
        }

        LocalDate birthDate = LocalDate.of(fullYear, month, day);
        LocalDate referenceDate = LocalDate.of(2021, 5, 10);

        return Period.between(birthDate, referenceDate).getYears();
    }
}
