package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnavneet.projects.ai_app_builder.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

}
