package com.varkaikin.passwordwallet.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "password")
@Getter
@Setter
public class Password {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name="login")
    private String login;

    @Column(name="is_deleted")
    private Boolean isDeleted = false;

    @Column(name="password")
    private String password;

    @Column(name="web_address")
    private String web_address;

    @Column(name="description")
    private String description;

    @Column(name="owner_id")
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy = "user")
    private Set<DataChange> dataChanges = new HashSet<>();

    public void addDataChange(DataChange dataChange) {
        this.dataChanges.add(dataChange);
        dataChange.setPassword(this);
    }

    @Override
    public String toString() {
        return "{" +
                " \"id\" : " + id  +
                ", \"login\": \"" + login + '\"' +
                ", \"isDeleted\": " + isDeleted +
                ", \"password\": \"" + password + '\"' +
                ", \"web_address\": \"" + web_address + '\"' +
                ", \"description\": \"" + description + '\"' +
                ", \"ownerId\": " + ownerId +
                '}';
    }
}
