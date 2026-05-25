package com.sbnavneet.projects.ai_app_builder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnavneet.projects.ai_app_builder.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
