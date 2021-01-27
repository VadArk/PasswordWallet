package com.varkaikin.passwordwallet.service;

import com.varkaikin.passwordwallet.model.Log;
import com.varkaikin.passwordwallet.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class LogServiceImplementation implements LogService {

    private final LogRepository logRepository;

    public LogServiceImplementation(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Override
    public Integer getLoginStatus(String login, Boolean result, String remoteAddr) {

        Log logByIp = logRepository.findFirstByIpAddressOrderByTimeDesc(remoteAddr);

        Log log = new Log();
        log.setTime(LocalDateTime.now());
        log.setIpAddress(remoteAddr);
        log.setLogin(login);

        if (logByIp != null) {
            if (logByIp.getAttempt() == 4) {
                return 4;
            } else if (logByIp.getAttempt() == 0) {

                if (result) {
                    log.setAttempt(0);
                    logRepository.save(log);
                    return 0;
                } else {
                    log.setAttempt(1);
                    logRepository.save(log);
                    return 1;
                }


            } else {
                if (timeChek(logByIp)) {
                    if (result) {

                        log.setAttempt(0);
                        logRepository.save(log);
                        return 0;
                    } else {

                        if (logByIp.getAttempt() == 3) {

                            log.setAttempt(4);
                            logRepository.save(log);
                            return 4;
                        } else {
                            log.setAttempt(logByIp.getAttempt() + 1);
                            logRepository.save(log);
                            return log.getAttempt();

                        }
                    }

                } else {
                    return logByIp.getAttempt();
                }
            }

        } else {

            return createLog(login, result, remoteAddr);
        }

    }

    @Override
    public Log findLastSuccessful(String ipAddress, String login) {
        return logRepository.findLastSuccessful(ipAddress, login);
    }

    @Override
    public Log findLastUnSuccessful(String ipAddress, String login) {
        return logRepository.findLastUnSuccessful(ipAddress, login);
    }

    @Override
    public Log resetIp(String remoteAddr) {
        if (logRepository.findFirstByIpAddress(remoteAddr) != null) {
            Log log = new Log();
            log.setIpAddress(remoteAddr);
            log.setTime(LocalDateTime.now());
            log.setLogin("login-reset");
            log.setAttempt(0);
            return logRepository.save(log);
        }
        else return null;
    }

    private Boolean timeChek(Log logByIp) {
        LocalDateTime time = logByIp.getTime();
        int attempt = logByIp.getAttempt();
        if (attempt == 1 && LocalDateTime.now().isAfter(time.plusSeconds(5))) {
            return true;
        } else if (attempt == 2 && LocalDateTime.now().isAfter(time.plusSeconds(10))) {
            return true;
        } else if (attempt == 3 && LocalDateTime.now().isAfter(time.plusMinutes(2))) {
            return true;
        } else return false;

    }

    private Integer createLog(String login, Boolean result, String remoteAddr) {
        Log log = new Log();
        log.setIpAddress(remoteAddr);
        log.setTime(LocalDateTime.now());
        log.setLogin(login);
        if (result) {
            log.setAttempt(0);
            logRepository.save(log);
            return 0;
        } else {
            log.setAttempt(1);
            logRepository.save(log);
            return 1;
        }


    }


}
