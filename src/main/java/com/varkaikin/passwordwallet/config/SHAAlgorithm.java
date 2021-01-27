package com.varkaikin.passwordwallet.config;

import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class SHAAlgorithm {

    public static String calculateHashSHA512(String pepper, String salt, String text) {

        return calculateSHA512(pepper + salt + text);
    }


    private static String calculateSHA512(String text) {
        try {
            //get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //calculate message digest of the input string  - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            // return the HashText
            return hashtext;
        }
        // If wrong message digest algorithm was specified
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String generateSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }
}
