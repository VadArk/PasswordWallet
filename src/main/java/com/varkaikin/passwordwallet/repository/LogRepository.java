package com.varkaikin.passwordwallet.repository;

import com.varkaikin.passwordwallet.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    Log findFirstByIpAddressOrderByTimeDesc(String ipAddress);

    @Query("SELECT l FROM Log l WHERE l.time= " +
            "(SELECT MAX(l2.time) FROM Log l2 WHERE l2.login = ?2 AND l2.ipAddress = ?1 AND l2.attempt = 0)")
    Log findLastSuccessful(String ipAddress, String login);

    @Query("SELECT l FROM Log l WHERE l.time= " +
            "(SELECT MAX(l2.time) FROM Log l2 WHERE l2.login = ?2 AND l2.ipAddress = ?1 AND l2.attempt > 0)")
    Log findLastUnSuccessful(String ipAddress, String login);


    Log findFirstByIpAddress(String remoteAddr);
}
