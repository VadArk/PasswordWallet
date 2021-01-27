package com.varkaikin.passwordwallet.repository;

import com.varkaikin.passwordwallet.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
    public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findAllByUser_LoginAndIsDeletedIsFalse(String login, Pageable pageable);
    List<Password> findAllByUser_LoginAndIsDeletedIsFalse(String login);
    List<Password> findAllByUser_Login(String login);
    Password findByUser_LoginAndIdAndOwnerIdIsNull(String login, Long id);

    List<Password> findAllByOwnerId(Long id);
}
