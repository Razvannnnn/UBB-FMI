namespace Problema8SC_CSharp.Model;

public class AgeConverter
{
    public static int GetAgeFromCNP(string cnp)
    {
        if (string.IsNullOrEmpty(cnp) || cnp.Length != 13 || !long.TryParse(cnp, out _))
        {
            throw new ArgumentException("CNP invalid");
        }

        int s = int.Parse(cnp.Substring(0, 1));
        int year = int.Parse(cnp.Substring(1, 2));
        int month = int.Parse(cnp.Substring(3, 2));
        int day = int.Parse(cnp.Substring(5, 2));

        int fullYear;
        if (s == 1 || s == 2)
        {
            fullYear = 1900 + year;
        }
        else if (s == 5 || s == 6)
        {
            fullYear = 2000 + year;
        }
        else
        {
            throw new ArgumentException("CNP cu secol necunoscut");
        }

        DateTime birthDate;
        try
        {
            birthDate = new DateTime(fullYear, month, day);
        }
        catch (ArgumentOutOfRangeException)
        {
            throw new ArgumentException("Data de naștere invalidă în CNP");
        }

        DateTime referenceDate = new DateTime(2021, 5, 10);
        int age = referenceDate.Year - birthDate.Year;
        if (birthDate > referenceDate.AddYears(-age)) age--;

        return age;
    }
}