package com.cwcdev.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwcdev.dto.CategoryDTO;
import com.cwcdev.entities.Category;
import com.cwcdev.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public List<CategoryDTO>findAll(){
		List<Category> list = repository.findAll();
		List<CategoryDTO> listDTO = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	    return listDTO;
	
	}
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj =   repository.findById(id);
		Category entity = obj.get();
		return new CategoryDTO(entity);
	}

}
