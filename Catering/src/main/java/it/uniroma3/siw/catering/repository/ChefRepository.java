package it.uniroma3.siw.catering.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Chef;

public interface ChefRepository extends CrudRepository<Chef,Long> {

	public boolean existsByNomeAndCognome(String nome, String cognome);
	
}
