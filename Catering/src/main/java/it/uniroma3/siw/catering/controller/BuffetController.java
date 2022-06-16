package it.uniroma3.siw.catering.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;
import it.uniroma3.siw.catering.validator.BuffetValidator;

@Controller
public class BuffetController {

	@Autowired private BuffetService buffetService;
	@Autowired private ChefService chefService;
	@Autowired private BuffetValidator buffetValidator;

	/* accesso al managment dei buffet */
	@GetMapping("/admin/buffet_managment")
	public String getBuffetManagment(Model model) {
		model.addAttribute("buffets", this.buffetService.findAll());
		return "/admin/buffet_managment.html";
	}

	//=======================================================================
	//****CREAZIONE NUOVO BUFFET****//
	
	/* accesso alla form per la creazione di un nuovo buffet per lo chef il quale id e'passato per parametro */
	@GetMapping("/admin/buffet_managment/add/{chef_id}")
	public String getBuffetForm(@PathVariable Long chef_id, Model model) {
		Buffet nuovoBuffet = new Buffet();
		model.addAttribute("buffet", nuovoBuffet);
		model.addAttribute("chef_id", chef_id);
		return "/admin/buffet_form.html";
	}
	
	/* metodo che inserisce il buffet nella base di dati */
	@PostMapping("/admin/buffet_managment/add/{chef_id}")
	public String addBuffet(@PathVariable Long chef_id, @Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResults, Model model) {
		this.buffetValidator.validate(buffet, bindingResults);
		if(!bindingResults.hasErrors()) {
			Chef chef = this.chefService.findById(chef_id);
			chef.addBuffet(buffet);
			buffet.setChef(chef);
			this.buffetService.save(buffet);
			model.addAttribute("buffet", model);
			return "redirect:/admin/buffet_managment";
		}
		else
			return "/admin/buffet_form.html";
	}
	
	//=========================================================
	//****ELIMINAZIONE BUFFET****//
	
	/* metodo per eliminare un buffet dalla base di dati*/
	@GetMapping("/admin/buffet_managment/delete/{buffet_id}")
	public String deleteBuffet(@PathVariable Long buffet_id) {
		this.buffetService.deleteBuffet(buffet_id);
		return "redirect:/admin/buffet_managment";
	}
	
	/*======================================================*/
	//****ACCESSO ALLE INFORMAZIONI****//
	
	/* metodo che mostra il buffet selezionato */
	@GetMapping("/user/all_buffets/buffet/{id}")
	public String getBuffet(@PathVariable Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "/user/buffet.html";
	}
	
}
