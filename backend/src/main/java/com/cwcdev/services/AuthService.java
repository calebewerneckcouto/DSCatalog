package com.cwcdev.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cwcdev.dto.EmailDTO;
import com.cwcdev.entities.PasswordRecover;
import com.cwcdev.entities.User;
import com.cwcdev.repositories.PasswordRecoverRepository;
import com.cwcdev.repositories.UserRepository;
import com.cwcdev.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;
@Service
public class AuthService {
	
	@Value("${email.password-recover.token.minutes}")
	private long tokenMinutes;
	
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordRecoverRepository passwordRecoverRepository;
	
	@Autowired
	private EmailService emailService;
   
	@Transactional
	public void createRecoverToken(EmailDTO body) {
		
		User user = userRepository.findByEmail(body.getEmail());
		
		if(user == null) {
			throw new ResourceNotFoundException("Email não encontrado");
		}
		
		String token = UUID.randomUUID().toString();
		PasswordRecover entity =  new PasswordRecover();
		entity.setEmail(body.getEmail());
		entity.setToken(token);
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes*60));
		entity=passwordRecoverRepository.save(entity);
		
		String text =  "Acesse o link para definir uma nova senha\n\n"
				+ recoverUri +token + ". Validade de " + tokenMinutes + " minutos";
		
		emailService.sendEmail(body.getEmail(), "Recuperação de Senha", text);
	}

}
