namespace chat.networking.jsonprotocol;

public class TextUtils
{
   public  static string SimpleEncode(string input)
    {
        char[] chars = input.ToCharArray();
        for (int i = 0; i < chars.Length; i++)
        {
            char c = (chars[i])++;
            c = (chars[i])++;
            c = (chars[i])++;
        }
        return new string(chars);
    }
 
   public  static string SimpleDecode(string input)
    {
        char[] chars = input.ToCharArray();
        for (int i = 0; i < chars.Length; i++)
        {
            char c = (chars[i])--;
            c = (chars[i])--;
            c = (chars[i])--;
        }
        return new string(chars);
    }
    
}