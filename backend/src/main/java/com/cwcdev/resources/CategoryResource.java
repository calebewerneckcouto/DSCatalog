package com.cwcdev.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cwcdev.entities.Category;

/*Complementa Controlador REST*/

@RestController
@RequestMapping(value="/categories")
public class CategoryResource {
	
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll(){
		List<Category> list = new ArrayList<Category>();
		list.add(new Category(1L,"Livros"));
		list.add(new Category(2L,"Periféricos"));
		return ResponseEntity.ok().body(list);
	}

}
