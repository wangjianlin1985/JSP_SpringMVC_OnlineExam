// 
// 
// 

package exam.util;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public abstract class StringUtil
{
    public static String md5(final String str) {
        final StringBuilder result = new StringBuilder("");
        if (DataUtil.isValid(str)) {
            final char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            final byte[] origin = str.getBytes();
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            final byte[] after = md.digest(origin);
            byte[] array;
            for (int length = (array = after).length, i = 0; i < length; ++i) {
                final byte b = array[i];
                result.append(chars[b >> 4 & 0xF]);
                result.append(chars[b & 0xF]);
            }
        }
        return result.toString();
    }
    
    public static String htmlEncode(final String html) {
        if (DataUtil.isValid(html)) {
            final StringBuilder result = new StringBuilder();
            for (int i = 0, l = html.length(); i < l; ++i) {
                final char c = html.charAt(i);
                switch (c) {
                    case '&': {
                        result.append("&amp;");
                        break;
                    }
                    case '<': {
                        result.append("&lt;");
                        break;
                    }
                    case '>': {
                        result.append("&gt;");
                        break;
                    }
                    case '\"': {
                        result.append("&quot;");
                        break;
                    }
                    case ' ': {
                        result.append("&nbsp;");
                        break;
                    }
                    default: {
                        result.append(c);
                        break;
                    }
                }
            }
            return result.toString();
        }
        return "";
    }
}
