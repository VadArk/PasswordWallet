package com.varkaikin.passwordwallet.config;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class SHAAlgorithmTest {

    @Test
    public void testCalculateHashSHA512() {

        assertEquals("4675ae43b7613bb0d6c8e1e9296458777234cf8f1e7055fff80c399970b7a384075236cfb8aeaa42c3fa6ec7acfb7b9eea7ac67c4ed70854de5a7051b4bfee6d",
                SHAAlgorithm.calculateHashSHA512("pepper", "salt", "text"));
    }

}
