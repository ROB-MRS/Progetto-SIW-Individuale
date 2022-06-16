package it.uniroma3.siw.catering.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.PiattoService;

@Component
public class PiattoValidator implements Validator {
	
	@Autowired PiattoService piattoService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Piatto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.piattoService.alredyExists((Piatto) target)) {
			errors.reject("piatto.duplicato");
		}
	}
	
	
}
