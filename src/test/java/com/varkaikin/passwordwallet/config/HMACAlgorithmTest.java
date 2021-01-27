package com.varkaikin.passwordwallet.config;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class HMACAlgorithmTest {



    @Test
    public void testCalculateHMAC() {


        assertEquals("tYUxKs3TjsE/E7tMujWnVHPzK2rkoDA5JoFb1D16JjFRaysDHzTYntqFPpSNUFfeVKiAwWaXJC2+ahrZlLxOXQ==",
                HMACAlgorithm.calculateHMAC("text", "key"));
    }
}
