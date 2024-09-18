package com.cwcdev.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cwcdev.dto.RoleDTO;
import com.cwcdev.dto.UserDTO;
import com.cwcdev.dto.UserInsertDTO;
import com.cwcdev.dto.UserUpdateDTO;
import com.cwcdev.entities.Role;
import com.cwcdev.entities.User;
import com.cwcdev.repositories.RoleRepository;
import com.cwcdev.repositories.UserRepository;
import com.cwcdev.services.exceptions.DatabaseException;
import com.cwcdev.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository rolerepository;
	
	

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));

	}

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save((entity));
		return new UserDTO(entity);

	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
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

	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		
		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {

			/* getReferenceById nao busca no banco.... sem tocar no banco de dados */
			Role role = rolerepository.getReferenceById(roleDto.getId());
			entity.getRoles().add(role);

		}

	}

}
