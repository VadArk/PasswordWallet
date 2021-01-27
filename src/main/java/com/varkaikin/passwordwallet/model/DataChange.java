package com.varkaikin.passwordwallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "data_change")
@Getter
@Setter
public class DataChange {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="password_id", nullable=false)
    private Password password;

    @Column(name="previous_value_of_record")
    private String previousValueOfRecord ;

    @Column(name="present_value_of_record")
    private String presentValueOfRecord ;

    @Column(name = "time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Column(name="action_type")
    private String actionType;




}
