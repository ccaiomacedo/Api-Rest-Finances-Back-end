package com.caiodev.Finances.repository;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaunchRepository extends JpaRepository<Launch,Long> {
}