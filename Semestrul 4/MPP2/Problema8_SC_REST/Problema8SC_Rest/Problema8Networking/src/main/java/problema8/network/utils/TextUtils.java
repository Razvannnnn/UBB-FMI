package problema8.network.utils;

public class TextUtils {
    public static String simpleEncode(String text) {
        char[] chars = text.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            chars[i]+=3;
        }
        return new String(chars);
    }

    public static String simpleDecode(String text) {
        char[] chars = text.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            chars[i]-=3;
        }
        return new String(chars);
    }
}
