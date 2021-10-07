
package utils;


public class MD5 {
    
    public static String createMD5Password( String data, int count )  {
        String passString = convertToMD5(data);
        for (int i = 0; i < count - 1; i++) {
            passString = convertToMD5(passString);
        }
        return passString;
    } 

    
    public static String convertToMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
