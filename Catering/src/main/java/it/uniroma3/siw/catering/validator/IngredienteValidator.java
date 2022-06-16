package it.uniroma3.siw.catering.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.service.IngredienteService;

@Component
public class IngredienteValidator implements Validator {
	
	@Autowired private IngredienteService ingredienteService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Ingrediente.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (this.ingredienteService.alreadyExists((Ingrediente) target)) {
			errors.reject("ingrediente.duplicato");
		}
	}

}
