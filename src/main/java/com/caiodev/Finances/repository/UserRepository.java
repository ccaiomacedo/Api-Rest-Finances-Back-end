package com.caiodev.Finances.repository;

import com.caiodev.Finances.entity.UserR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserR,Long> {

    boolean existsByEmail(String email);

    Optional<UserR> findByEmail(String email);

}
