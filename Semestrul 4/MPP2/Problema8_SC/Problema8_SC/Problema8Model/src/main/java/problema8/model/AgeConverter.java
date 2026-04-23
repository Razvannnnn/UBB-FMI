package problema8.model;

public class AgeConverter {
    public static int getAgeFromCNP(String cnp) {
        int year = Integer.parseInt(cnp.substring(1, 3));
        int month = Integer.parseInt(cnp.substring(3, 5));
        int day = Integer.parseInt(cnp.substring(5, 7));

        int currentYear = 2021;
        int currentMonth = 5;
        int currentDay = 10;

        int age = currentYear - year;
        if (month > currentMonth || (month == currentMonth && day > currentDay)) {
            age--;
        }

        return age;
    }
}
