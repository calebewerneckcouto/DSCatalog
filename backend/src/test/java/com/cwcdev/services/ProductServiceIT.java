package com.cwcdev.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.cwcdev.dto.ProductDTO;
import com.cwcdev.repositories.ProductRepository;
import com.cwcdev.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

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

@SpringBootTest
@Transactional
public class ProductServiceIT {
	@Autowired
	private ProductService service;

	@Autowired
	private ProductRepository repository;

	private long existingId;
	private long nonexistingId;
	private long countTotalProducts;

	@BeforeEach
	void setup() throws Exception {

		existingId = 1L;
		nonexistingId = 1000L;
		countTotalProducts = 25;

	}

	@Test
	public void deleteShouldDeleteResourceWhenIdExist() {
		service.delete(existingId);

		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonexistingId);
		});
	}

	@Test
	public void findAllpagedShouldReturnPageWhenPage0Size10() {

		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0,result.getNumber());
		Assertions.assertEquals(10,result.getSize());
		Assertions.assertEquals(countTotalProducts,result.getTotalElements());
		
	}
	
	
	@Test
	public void findAllpagedShouldReturnEmptyPageWhenPageDoesnotExist() {

		PageRequest pageRequest = PageRequest.of(50, 10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
		
		
	}
	
	
	
	@Test
	public void findAllpagedShouldReturnSortedPageWhenSortByName() {

		PageRequest pageRequest = PageRequest.of(0, 10,Sort.by("name"));
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro",result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer",result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa",result.getContent().get(2).getName());
		
		
	}
	
	
	
	
}
