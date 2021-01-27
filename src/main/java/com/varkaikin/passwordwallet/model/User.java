package com.varkaikin.passwordwallet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;


@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password_hash")
    private String password_hash;

    @Column(name = "salt")
    private String salt;

    @Column(name = "isPasswordKeptAsHash")
    private Boolean isPasswordKeptAsHash;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Password> passwords = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<DataChange> dataChanges = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<FunctionRun> functionRuns = new HashSet<>();

    public void addPassword(Password password) {
        this.passwords.add(password);
        password.setUser(this);
    }

    public void addDataChange(DataChange dataChange) {
        this.dataChanges.add(dataChange);
        dataChange.setUser(this);
    }

    public void addFunctionRun(FunctionRun functionRun) {
        this.functionRuns.add(functionRun);
        functionRun.setUser(this);
    }






}
