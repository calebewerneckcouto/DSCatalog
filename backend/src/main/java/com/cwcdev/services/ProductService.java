package com.cwcdev.services;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cwcdev.dto.CategoryDTO;
import com.cwcdev.dto.ProductDTO;
import com.cwcdev.entities.Category;
import com.cwcdev.entities.Product;
import com.cwcdev.projetctions.ProductProjection;
import com.cwcdev.repositories.CategoryRepository;
import com.cwcdev.repositories.ProductRepository;
import com.cwcdev.services.exceptions.DatabaseException;
import com.cwcdev.services.exceptions.ResourceNotFoundException;
import com.cwcdev.util.Utils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	@Autowired
	private CategoryRepository categoryRepository;

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));

	}

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save((entity));
		return new ProductDTO(entity);

	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {

		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {

			/* getReferenceById nao busca no banco.... sem tocar no banco de dados */
			Category category = categoryRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(category);

		}

	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(String name, String categoryId, Pageable pageable) {

		List<Long> categoryIds = Arrays.asList();
		if (!"0".equals(categoryId)) {
			categoryIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
		}
		Page<ProductProjection> page = repository.searchProducts(categoryIds, name.trim(), pageable);
		List<Long> productIds = page.map(x -> x.getId()).toList();

		List<Product> entities = repository.searchProductsWithCategories(productIds);
		entities = (List<Product>) Utils.replace(page.getContent(),entities);
		List<ProductDTO> dtos = entities.stream().map(p -> new ProductDTO(p, p.getCategories())).toList();
		
		Page<ProductDTO> pageDto = new PageImpl<ProductDTO>(dtos, page.getPageable(),page.getTotalElements());
		return pageDto;
	}

}
