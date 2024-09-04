package com.cwcdev.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cwcdev.entities.Product;
import com.cwcdev.factory.Factory;

/*
 * @SpringBootTest   -   Carrega contexto da aplicação (teste de integração)
 * 
 * @SpringBootTest
 * @AutoConfigureMockMvc   -  Trata as requisições sem subir o servidor (teste de integração e web)
 * 
 * 
 * @WebMvcTest(Classe.class)  -  Carrega o contexto, porém somente da camada web (teste de unidade:controlador)
 * 
 * 
 * @ExtendWith(SpringExtension.class)   - Não Carrega o contexto, mas permite usar os recursos do Spring com Junit (teste de unidade:service/component)
 * 
 * 
 * @DataJpaTest   -  Carrega somente os componentes relacionandos ao Spring Data Jpa. Cada teste é transacional e dá rollback ao final ( teste de unidade: repository)
 * */

@DataJpaTest
public class ProductRepositoryTests {
	@Autowired
	private ProductRepository productRepository;
	
	
	private long existingId ;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createproduct();
		product.setId(null);
		
		product =  productRepository.save(product);
		
		Assertions.assertNotNull(product.getId());
		
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		
	}
	
	

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

	
		productRepository.deleteById(existingId);

		Optional<Product> result = productRepository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

}
