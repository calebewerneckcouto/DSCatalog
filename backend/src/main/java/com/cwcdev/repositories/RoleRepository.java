package com.cwcdev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cwcdev.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	
	Role findByAuthority(String authority);

}
