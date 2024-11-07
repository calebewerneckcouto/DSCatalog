package com.cwcdev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cwcdev.entities.PasswordRecover;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {
    
}
