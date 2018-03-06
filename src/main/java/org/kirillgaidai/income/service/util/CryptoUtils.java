package org.kirillgaidai.income.service.util;

import org.springframework.security.core.token.Sha512DigestUtils;

import java.util.Scanner;

public class CryptoUtils {

    final private static String SALT = "Hello, World!";

    private CryptoUtils() {
        throw new UnsupportedOperationException();
    }

    private static String addSalt(String str) {
        StringBuilder result = new StringBuilder();
        int saltIndex = 0;
        for (int strIndex = 0; strIndex < str.length(); strIndex++) {
            result.append(str.charAt(strIndex));
            result.append(SALT.charAt(saltIndex));
            saltIndex++;
            if (saltIndex == SALT.length()) {
                saltIndex = 0;
            }
        }
        return result.toString();
    }

    public static String encodeString(String str) {
        return Sha512DigestUtils.shaHex(addSalt(str));
    }

    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            String password = scanner.nextLine();
            System.out.println(addSalt(password));
            System.out.println(encodeString(password));
        }
    }

}
