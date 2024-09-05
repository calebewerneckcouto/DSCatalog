package com.cwcdev.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cwcdev.dto.ProductDTO;
import com.cwcdev.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@AutoConfigureMockMvc
@org.springframework.transaction.annotation.Transactional
public class ProductResourceIT {
	
	@Autowired
	private MockMvc mockMvc;

	private long existingId;
	private long nonexistingId;
	private long countTotalProducts;

	@BeforeEach
	void setup() throws Exception {

		existingId = 1L;
		nonexistingId = 2L;
		countTotalProducts = 25;
		
	}
	
	
	@Test
	public void findAllShouldReturnSortedPageWhenSorteByName() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/products?page=0&size=12&sort=name,asc").accept(org.springframework.http.MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect( jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect( jsonPath("$.content").exists());
		result.andExpect( jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect( jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect( jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}

}
