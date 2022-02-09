package com.github.peixunxposed.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    public static String decrypt(String str, String str2) throws Exception {
        if (str2 == null) {
            try {
                System.out.print("Key为空null");
                return null;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } else if (str2.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        } else {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("UTF-8"), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(2, secretKeySpec);
            try {
                return new String(instance.doFinal(decrypt(str)));
            } catch (Exception e2) {
                System.out.println(e2.toString());
                return null;
            }
        }
    }

    public static String decrypt(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            str = hexString.length() == 1 ? str + "0" + hexString : str + hexString;
        }
        return str.toUpperCase();
    }

    public static byte[] decrypt(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length % 2 == 1) {
            return null;
        }
        int i = length / 2;
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 != i; i2++) {
            int i3 = i2 * 2;
            bArr[i2] = (byte) Integer.parseInt(str.substring(i3, i3 + 2), 16);
        }
        return bArr;
    }

    public static String encrypt(String str, String str2) throws Exception {
        if (str2 == null) {
            System.out.print("Key为空null");
            return null;
        } else if (str2.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        } else {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("UTF-8"), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(1, secretKeySpec);
            return decrypt(instance.doFinal(str.getBytes())).toLowerCase();
        }
    }

    public static void main(String[] strArr) throws Exception {
        System.out.println(encrypt("123", "1234564564564565"));
        System.out.println(decrypt("75e9adc004b8454664369da330ee2afa", "1234564564564565"));
    }

}
