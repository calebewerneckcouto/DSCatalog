package com.cwcdev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cwcdev.entities.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
