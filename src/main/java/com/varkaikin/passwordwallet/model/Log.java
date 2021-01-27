package com.varkaikin.passwordwallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "log")
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "attempt")
    private int attempt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "login")
    private String login;

    @Column(name = "time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;




}
