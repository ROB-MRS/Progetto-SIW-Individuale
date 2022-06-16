package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.repository.BuffetRepository;

@Service
public class BuffetService {

	@Autowired private BuffetRepository buffetRepository;
	@Autowired private PiattoService piattoService;
	
	@Transactional
	public void save(Buffet buffet) {
		buffetRepository.save(buffet);
	}
	
	public Buffet findById(Long id) {
		return buffetRepository.findById(id).get();
	}
	
	public List<Buffet> findAll(){
		List<Buffet> buffets = new ArrayList<>();
		for(Buffet b : buffetRepository.findAll()) {
			buffets.add(b);
		}
		return buffets;
	}
	
	public List<Buffet> findAllByChef(Chef chef) {
		List<Buffet> buffets = new ArrayList<>();
		for(Buffet b: buffetRepository.findByChef(chef)) {
			buffets.add(b);
		}
		return buffets;
	}
	
	public void deleteBuffet(Long id) {
		List<Piatto> piatti = piattoService.findAllByBuffet(this.findById(id));
		for(Piatto p: piatti)
			piattoService.deletePiatto(p.getId());
		
		this.buffetRepository.deleteById(id);
	}

	public boolean alreadyExists(Buffet buffet) {
		return buffetRepository.existsByNomeAndChef(buffet.getNome(), buffet.getChef());
	}
}
