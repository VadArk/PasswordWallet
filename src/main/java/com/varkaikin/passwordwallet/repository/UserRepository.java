package com.varkaikin.passwordwallet.repository;
import com.varkaikin.passwordwallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   User findByLogin(String username);
}
