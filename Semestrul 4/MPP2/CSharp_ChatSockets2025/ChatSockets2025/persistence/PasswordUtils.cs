using System.Security.Cryptography;
using System.Text;

namespace chat.persistence;

public class PasswordUtils
{
    public static string HashText(string input)
    {
        var inputBytes = Encoding.UTF8.GetBytes(input);
        var inputHash = SHA256.HashData(inputBytes);
        return Convert.ToHexString(inputHash);
    }
}