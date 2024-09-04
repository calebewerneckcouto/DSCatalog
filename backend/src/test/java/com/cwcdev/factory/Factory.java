package com.cwcdev.factory;

import java.time.Instant;

import com.cwcdev.dto.ProductDTO;
import com.cwcdev.entities.Category;
import com.cwcdev.entities.Product;

public class Factory {

	public static Product createproduct() {

		Product product = new Product(1L, "Phone", "Good Phone", 800.00, "https://img.com/img.png",
				Instant.parse("2020-10-20T03:00:00Z"));
		
		product.getCategories().add(new Category(2L,"Eletronics"));

		return product;

	}
	
	
	public static ProductDTO createProductDTO() {
		Product product =  createproduct();
		return new ProductDTO(product,product.getCategories());
	}
}
