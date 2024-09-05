package com.cwcdev.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cwcdev.dto.ProductDTO;
import com.cwcdev.factory.Factory;
import com.cwcdev.services.ProductService;
import com.cwcdev.services.exceptions.DatabaseException;
import com.cwcdev.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	private PageImpl<ProductDTO> page;
	@Autowired
	private ObjectMapper mapper;

	private ProductDTO dto;

	private long existingId;
	private long nonexistingId;
	private long dependedId;

	@BeforeEach
	void setup() throws Exception {

		existingId = 1L;
		nonexistingId = 2L;
		dependedId = 3L;
		dto = Factory.createProductDTO();
		page = new PageImpl<>(List.of(dto));
		when(productService.findAllPaged(any())).thenReturn(page);

		when(productService.findById(existingId)).thenReturn(dto);
		when(productService.findById(nonexistingId)).thenThrow(ResourceNotFoundException.class);

		when(productService.update(eq(existingId), any())).thenReturn(dto);
		when(productService.update(eq(nonexistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(productService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonexistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependedId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/products").accept(org.springframework.http.MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/products/{id}", existingId).accept(org.springframework.http.MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(
				get("/products/{id}", nonexistingId).accept(org.springframework.http.MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void updateShouldreturnProductWhenIdExists() throws Exception {

		String jsonBody = mapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(put("/products/{id}", existingId).content(jsonBody)
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.accept(org.springframework.http.MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void updateShouldreturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		String jsonBody = mapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(put("/products/{id}", nonexistingId).content(jsonBody)
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.accept(org.springframework.http.MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
}
