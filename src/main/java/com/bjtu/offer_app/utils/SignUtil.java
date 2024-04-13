package com.bjtu.offer_app.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.Arrays;

public class SignUtil {

    private static final String TOKEN = "offerShow"; // 替换为你的微信Token

    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[] { TOKEN, timestamp, nonce };
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();
        for (String anArr : arr) {
            content.append(anArr);
        }

        String tmpStr = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将sha1加密后的字符串与signature对比
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (byte aByteArray : byteArray) {
            strDigest += byteToHexStr(aByteArray);
        }
        return strDigest;
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        return new String(tempArr);
    }
}
