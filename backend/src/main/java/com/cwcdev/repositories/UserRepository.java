package com.cwcdev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cwcdev.dto.CategoryDTO;
import com.cwcdev.entities.Category;
import com.cwcdev.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}