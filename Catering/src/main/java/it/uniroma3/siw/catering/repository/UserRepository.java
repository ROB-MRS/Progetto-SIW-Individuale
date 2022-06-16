package it.uniroma3.siw.catering.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}