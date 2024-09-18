package com.cwcdev.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.cwcdev.dto.UserUpdateDTO;
import com.cwcdev.entities.User;
import com.cwcdev.repositories.UserRepository;
import com.cwcdev.resources.Exceptions.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserRepository repository;

	@Override
	public void initialize(UserUpdateValid ann) {
	}

	// testando se o metodo e valido ou não...
	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
        @SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long userId = Long.parseLong(uriVars.get("id"));

		List<FieldMessage> list = new ArrayList<>();
		
		User user = repository.findByEmail(dto.getEmail());

	   if (user != null && userId != user.getId()) {
			list.add(new FieldMessage("email", "Esse email ja existe"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}