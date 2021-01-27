package com.varkaikin.passwordwallet.config;
import org.testng.annotations.Test;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.testng.Assert.assertEquals;

public class PasswordAlgorithmTest {


    @Test
    public void testEncrypt() throws Exception {
        final Key key = PassAlgor.generateKey("password");
        final String result = PassAlgor.encrypt("data", key);
        assertEquals("C/+sg8Yg7BJ4yVK4Q9ZiXA==", result);
    }

    @Test
    public void testDecrypt() throws Exception {

        final Key key = PassAlgor.generateKey("password");
        final String result = PassAlgor.decrypt("C/+sg8Yg7BJ4yVK4Q9ZiXA==", key);
        assertEquals("data", result);
    }

    @Test
    public void testCalculateMD5() throws NoSuchAlgorithmException {
        String text = "text";
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(text.getBytes());

        assertEquals(messageDigest, PassAlgor.calculateMD5(text));
    }
}
