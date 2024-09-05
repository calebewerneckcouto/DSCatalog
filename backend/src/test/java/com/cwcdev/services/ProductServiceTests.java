package com.cwcdev.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cwcdev.entities.Product;
import com.cwcdev.repositories.ProductRepository;
import com.cwcdev.services.exceptions.DatabaseException;
import com.cwcdev.services.exceptions.ResourceNotFoundException;

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

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonexistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	


	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonexistingId = 2L;
		dependentId = 3L;
		
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		
		Mockito.when(repository.findById(nonexistingId)).thenReturn(Optional.empty());
		
		
		
		/*Criando os mocks para testar os metodos*/
		
		doNothing().when(repository).deleteById(existingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonexistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonexistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);


	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonexistingId);
		});

		
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}

}
