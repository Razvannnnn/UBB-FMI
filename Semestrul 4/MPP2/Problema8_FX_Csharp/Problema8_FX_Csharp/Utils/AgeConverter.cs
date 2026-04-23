namespace Problema8_FX_Csharp.Utils;

public class AgeConverter
{
    public static int getAgeFromCNP(String CNP)
    {
        int year = int.Parse(CNP.Substring(1, 2));
        int month = int.Parse(CNP.Substring(3, 2));
        int day = int.Parse(CNP.Substring(5, 2));
        
        int currentYear = DateTime.Now.Year % 100;
        int currentMonth = DateTime.Now.Month;
        int currentDay = DateTime.Now.Day;
        
        int age = currentYear - year;
        if (month > currentMonth || (month == currentMonth && day > currentDay))
        {
            age--;
        }
        
        return age;
    }
}