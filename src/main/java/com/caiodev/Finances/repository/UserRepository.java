package com.caiodev.Finances.repository;

import com.caiodev.Finances.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);

}
