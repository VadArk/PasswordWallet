package com.varkaikin.passwordwallet.service;


import com.varkaikin.passwordwallet.model.Log;
import com.varkaikin.passwordwallet.repository.LogRepository;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.LocalDateTime;
import static org.testng.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LogServiceImplementationTest {

    @Mock
    private LogRepository mockLogRepository;
    private LogServiceImplementation logServiceImplementationUnderTest;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        logServiceImplementationUnderTest = new LogServiceImplementation(mockLogRepository);
    }

    @Test
    public void getLoginStatusTest() {

        final Log log = new Log();
        log.setId(0L);
        log.setAttempt(0);
        log.setIpAddress("ipAddress");
        log.setLogin("login");
        log.setTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockLogRepository.findFirstByIpAddressOrderByTimeDesc("ipAddress")).thenReturn(log);

        final Log log1 = new Log();
        log1.setId(0L);
        log1.setAttempt(0);
        log1.setIpAddress("ipAddress");
        log1.setLogin("login");
        log1.setTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockLogRepository.save(any(Log.class))).thenReturn(log1);
        final int result = logServiceImplementationUnderTest.getLoginStatus("login", false, "remoteAddr");

        assertEquals(1, result);
    }

    @Test
    public void findLastSuccessfulTest() {

        final Log log = new Log();
        log.setId(0L);
        log.setAttempt(0);
        log.setIpAddress("ipAddress");
        log.setLogin("login");
        log.setTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockLogRepository.findLastSuccessful("ipAddress", "login")).thenReturn(log);
        final Log result = logServiceImplementationUnderTest.findLastSuccessful("ipAddress", "login");
        assertEquals(log, result);
    }

    @Test
    public void findLastUnSuccessfulTest() {
        final Log log = new Log();
        log.setId(0L);
        log.setAttempt(0);
        log.setIpAddress("ipAddress");
        log.setLogin("login");
        log.setTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockLogRepository.findLastUnSuccessful("ipAddress", "login")).thenReturn(log);
        final Log result = logServiceImplementationUnderTest.findLastUnSuccessful("ipAddress", "login");
        assertEquals(log, result);
    }

    @Test
    public void resetIpTest() {

        final Log log = new Log();
        log.setId(0L);
        log.setAttempt(0);
        log.setIpAddress("ipAddress");
        log.setLogin("login");
        log.setTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockLogRepository.findFirstByIpAddress("remoteAddr")).thenReturn(log);
        when(mockLogRepository.save(any(Log.class))).thenReturn(log);
        final Log result = logServiceImplementationUnderTest.resetIp("remoteAddr");
        assertEquals(log, result);
    }
}
