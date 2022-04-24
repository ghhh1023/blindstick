package com.blindstick.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HexUtil {

    /**
     * ascii转16进制
     *
     * @param str 待转字符串
     * @return
     */
    public static String convertStringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
            hex.append(Integer.toHexString(chars[i]));
        return hex.toString();
    }

    /**
     * 16进制转ascii
     *
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }


    public static String IntToHexString(int num) {
        String hexString = Integer.toHexString(num);
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    public static String receiveHexToString(byte[] by) {
        try {
            String str = bytes2Str(by);
            assert str != null;
            str = str.toLowerCase();
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("接受数据转化为16进制字符串异常");
            return null;
        }
    }

    public static String bytes2Str(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 将16进制字符串转为10进制数据
     *
     * @param hexs
     * @return
     */
    public static int decodeHex(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }

    /**
     * 二进制数组转换16进制字符串
     *
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    /**
     * 16进制字符串转换字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexString2Bytes(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static String makeChecksum(String hexdata) {
        if (hexdata == null || "".equals(hexdata)) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num += 2;
        }
        return hexInt(total);
    }

    private static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        return format(a) + format(b);
    }

    private static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }

    public static String addHexToCheck(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String check = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(check, 16);
            int a = Integer.parseInt("33", 16);
            String b = format(decimal + a);
            if (b.length() > 2) {
                b = b.substring(b.length() - 2);
            }
            sb.append(b);
        }
        return sb.toString();
    }

    public static String reduceHexToCheck(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String check = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(check, 16);
            int a = Integer.parseInt("33", 16);
            String b = format(decimal - a);
            if (b.length() > 2) {
                b = b.substring(b.length() - 2);
            }
            sb.append(b);
        }
        return sb.toString();
    }


    public static String heighTolow(String adress) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = adress.length(); i > 1; i -= 2) {
            String check = adress.substring(i - 2, i);
            stringBuilder.append(check);
        }
        return stringBuilder.toString();
    }

    public static String moneyToHexString(String money) {
        String moneyHex = Integer.toHexString(Integer.parseInt(money));
        String moneyHexFormat = addZeroForNum(moneyHex, 8);
        return heighTolow(moneyHexFormat);
    }

    public static String subHex(String hex, int start, int end) throws IllegalArgumentException {
        return hex.substring(start, end);
    }

    public static String subHex(String hex, int start) throws IllegalArgumentException {
        return hex.substring(start);
    }

    public static String getStr16ToCRC(String str16) {
        str16 = str16.toUpperCase();
        int length = str16.length() / 2;
        char[] hexChars = str16.toCharArray();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            bytes[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        int CRC = 65535;
        int POLYNOMIAL = 40961;
        for (byte aByte : bytes) {
            CRC ^= aByte & 0xFF;
            for (int k = 0; k < 8; k++) {
                if ((CRC & 0x1) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        String crcInt = Integer.toHexString(CRC);
        if (crcInt.length() == 3) {
            crcInt = "0" + crcInt;
        }
        String a = heighTolow(crcInt);
        return a;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    public static String fromHex(String hex) {
        /*兼容带有\x的十六进制串*/
        hex = hex.replace("\\x", "");
        char[] data = hex.toCharArray();
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("字符个数应该为偶数");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return new String(out);
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    //使用1字节就可以表示b
    public static String numToHex8(int b) {
        return String.format("%02x", b);//2表示需要两个16进行数
    }

    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }

    //需要使用4字节表示b
    public static String numToHex32(int b) {
        return String.format("%08x", b);
    }

    /**
     * 高低位转换
     *
     * @param hex
     * @return
     */
    public static String reverseHex(final String hex) {
        final char[] charArray = hex.toCharArray();
        final int length = charArray.length;
        final int times = length / 2;
        for (int c1i = 0; c1i < times; c1i += 2) {
            final int c2i = c1i + 1;
            final char c1 = charArray[c1i];
            final char c2 = charArray[c2i];
            final int c3i = length - c1i - 2;
            final int c4i = length - c1i - 1;
            charArray[c1i] = charArray[c3i];
            charArray[c2i] = charArray[c4i];
            charArray[c3i] = c1;
            charArray[c4i] = c2;
        }
        return new String(charArray);
    }

    public static String getDateTime() {

        Date d = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = time.format(d);
        return date;
    }

    /**
     * UTF-8编码 转换为对应的 汉字
     *
     * URLEncoder.encode("上海", "UTF-8") ---> %E4%B8%8A%E6%B5%B7
     * URLDecoder.decode("%E4%B8%8A%E6%B5%B7", "UTF-8") --> 上 海
     *
     * convertUTF8ToString("E4B88AE6B5B7")
     * E4B88AE6B5B7 --> 上海
     *
     * @param s
     * @return
     */
    public static String convertUTF8ToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }

        try {
            s = s.toUpperCase();

            int total = s.length() / 2;
            int pos = 0;

            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {

                int start = i * 2;

                buffer[i] = (byte) Integer.parseInt(
                        s.substring(start, start + 2), 16);
                pos++;
            }

            return new String(buffer, 0, pos, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     *
     * @param s	原串
     * @return
     */
    public static String convertStringToUTF8(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    sb.append(c);
                } else {
                    byte[] b;

                    b = Character.toString(c).getBytes("utf-8");

                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        if (k < 0)
                            k += 256;
                        sb.append(Integer.toHexString(k).toUpperCase());
                        // sb.append("%" +Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return sb.toString();
    }
}



